package com.sierravanguard.beyond_oxygen.data;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagManager;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public abstract class DataRegistryTagsProvider<T> implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;
    private final CompletableFuture<Void> contentsDone = new CompletableFuture<>();
    private final CompletableFuture<DataRegistryTagsProvider.TagLookup<T>> parentProvider;
    protected final ResourceKey<? extends Registry<T>> registryKey;
    protected final Map<ResourceLocation, TagBuilder> builders = Maps.newLinkedHashMap();
    protected final String modId;
    @Nullable
    protected final net.minecraftforge.common.data.ExistingFileHelper existingFileHelper;
    private final net.minecraftforge.common.data.ExistingFileHelper.IResourceType resourceType;
    private final net.minecraftforge.common.data.ExistingFileHelper.IResourceType elementResourceType; // FORGE: Resource type for validating required references to datapack registry elements.

    protected DataRegistryTagsProvider(PackOutput pOutput, ResourceKey<? extends Registry<T>> pRegistryKey, CompletableFuture<HolderLookup.Provider> pLookupProvider, String modId, @org.jetbrains.annotations.Nullable net.minecraftforge.common.data.ExistingFileHelper existingFileHelper) {
        this(pOutput, pRegistryKey, pLookupProvider, CompletableFuture.completedFuture(DataRegistryTagsProvider.TagLookup.empty()), modId, existingFileHelper);
    }

    protected DataRegistryTagsProvider(PackOutput pOutput, ResourceKey<? extends Registry<T>> pRegistryKey, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<DataRegistryTagsProvider.TagLookup<T>> pParentProvider, String modId, @org.jetbrains.annotations.Nullable net.minecraftforge.common.data.ExistingFileHelper existingFileHelper) {
        this.pathProvider = pOutput.createPathProvider(PackOutput.Target.DATA_PACK, TagManager.getTagDir(pRegistryKey));
        this.registryKey = pRegistryKey;
        this.parentProvider = pParentProvider;
        this.lookupProvider = pLookupProvider;
        this.modId = modId;
        this.existingFileHelper = existingFileHelper;
        this.resourceType = new net.minecraftforge.common.data.ExistingFileHelper.ResourceType(net.minecraft.server.packs.PackType.SERVER_DATA, ".json", TagManager.getTagDir(pRegistryKey));
        this.elementResourceType = new net.minecraftforge.common.data.ExistingFileHelper.ResourceType(net.minecraft.server.packs.PackType.SERVER_DATA, ".json", net.minecraftforge.common.ForgeHooks.prefixNamespace(pRegistryKey.location()));
    }

    @Nullable
    protected Path getPath(ResourceLocation id) {
        return this.pathProvider.json(id);
    }

    public String getName() {
        return "Tags for " + this.registryKey.location() + " mod id " + this.modId;
    }

    protected abstract void addTags(HolderLookup.Provider pProvider);

    public CompletableFuture<?> run(CachedOutput output) {
        record CombinedData<T>(HolderLookup.Provider contents, DataRegistryTagsProvider.TagLookup<T> parent) {
        }
        return this.createContentsProvider().thenApply(provider -> {
            this.contentsDone.complete(null);
            return provider;
        }).thenCombineAsync(this.parentProvider, (provider, tagLookup) -> new CombinedData<>(provider, tagLookup)).thenCompose((data) -> {
            Predicate<ResourceLocation> elementPredicate = id -> true;
            Predicate<ResourceLocation> tagPredicate = id -> this.builders.containsKey(id) || data.parent.contains(TagKey.create(this.registryKey, id));
            return CompletableFuture.allOf(this.builders.entrySet().stream().map(entry -> {
                ResourceLocation id = entry.getKey();
                TagBuilder tagBuilder = entry.getValue();
                List<TagEntry> addedTags = tagBuilder.build();
                List<TagEntry> missingTags = java.util.stream.Stream.concat(addedTags.stream(), tagBuilder.getRemoveEntries()).filter((tagEntry) -> !tagEntry.verifyIfPresent(elementPredicate, tagPredicate)).filter(this::missing).toList(); // Forge: Add validation via existing resources
                if (!missingTags.isEmpty()) {
                    throw new IllegalArgumentException(String.format(Locale.ROOT, "Couldn't define tag %s as it is missing following references: %s", id, missingTags.stream().map(Objects::toString).collect(Collectors.joining(","))));
                } else {
                    List<TagEntry> removed = tagBuilder.getRemoveEntries().toList();
                    JsonElement json = TagFile.CODEC.encodeStart(JsonOps.INSTANCE, new TagFile(addedTags, tagBuilder.isReplace(), removed)).getOrThrow(false, LOGGER::error);
                    Path path = this.getPath(id);
                    if (path == null) return CompletableFuture.completedFuture(null); // Forge: Allow running this data provider without writing it. Recipe provider needs valid tags.
                    return DataProvider.saveStable(output, json, path);
                }
            }).toArray(CompletableFuture[]::new));
        });
    }

    private boolean missing(TagEntry reference) {
        // Optional tags should not be validated

        if (reference.isRequired()) {
            return existingFileHelper == null || !existingFileHelper.exists(reference.getId(), reference.isTag() ? resourceType : elementResourceType);
        }
        return false;
    }

    protected DataRegistryTagsProvider.TagAppender<T> tag(TagKey<T> tagKey) {
        TagBuilder tagBuilder = this.getOrCreateRawBuilder(tagKey);
        return new DataRegistryTagsProvider.TagAppender<>(tagBuilder, modId);
    }

    protected TagBuilder getOrCreateRawBuilder(TagKey<T> tagKey) {
        return this.builders.computeIfAbsent(tagKey.location(), (id) -> {
            if (existingFileHelper != null) {
                existingFileHelper.trackGenerated(id, resourceType);
            }
            return TagBuilder.create();
        });
    }

    public CompletableFuture<DataRegistryTagsProvider.TagLookup<T>> contentsGetter() {
        return this.contentsDone.thenApply(unused -> tagKey -> Optional.ofNullable(this.builders.get(tagKey.location())));
    }

    protected CompletableFuture<HolderLookup.Provider> createContentsProvider() {
        return this.lookupProvider.thenApply((provider) -> {
            this.builders.clear();
            this.addTags(provider);
            return provider;
        });
    }

    public static class TagAppender<T> implements net.minecraftforge.common.extensions.IForgeTagAppender<T> {
        private final TagBuilder builder;
        private final String modId;

        protected TagAppender(TagBuilder tagBuilder, String modId) {
            this.builder = tagBuilder;
            this.modId = modId;
        }

        public final DataRegistryTagsProvider.TagAppender<T> add(ResourceKey<T> key) {
            this.builder.addElement(key.location());
            return this;
        }

        @SafeVarargs
        public final DataRegistryTagsProvider.TagAppender<T> add(ResourceKey<T>... toAdd) {
            for (ResourceKey<T> resourcekey : toAdd) {
                this.builder.addElement(resourcekey.location());
            }
            return this;
        }

        public DataRegistryTagsProvider.TagAppender<T> addOptional(ResourceLocation id) {
            this.builder.addOptionalElement(id);
            return this;
        }

        public DataRegistryTagsProvider.TagAppender<T> addTag(TagKey<T> tagKey) {
            this.builder.addTag(tagKey.location());
            return this;
        }

        public DataRegistryTagsProvider.TagAppender<T> addOptionalTag(ResourceLocation id) {
            this.builder.addOptionalTag(id);
            return this;
        }

        public DataRegistryTagsProvider.TagAppender<T> add(TagEntry tag) {
            builder.add(tag);
            return this;
        }

        public TagBuilder getInternalBuilder() {
            return builder;
        }

        public String getModID() {
            return modId;
        }
    }

    @FunctionalInterface
    public interface TagLookup<T> extends Function<TagKey<T>, Optional<TagBuilder>> {
        static <T> DataRegistryTagsProvider.TagLookup<T> empty() {
            return (tagKey) -> Optional.empty();
        }

        default boolean contains(TagKey<T> tagKey) {
            return this.apply(tagKey).isPresent();
        }
    }
}
