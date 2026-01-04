package com.sierravanguard.beyond_oxygen.data;

import com.sierravanguard.beyond_oxygen.BeyondOxygen;
import com.sierravanguard.beyond_oxygen.tags.BODimensionTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BODimensionTagsProvider extends DataRegistryTagsProvider<Level> {
    public BODimensionTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, Registries.DIMENSION, lookupProvider, BeyondOxygen.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider lookupProvider) {
        this.tag(BODimensionTags.UNBREATHABLE)
                .add(ResourceKey.create(Registries.DIMENSION, new ResourceLocation("minecraft", "the_end")))

                .addOptional(new ResourceLocation("cosmos", "solar_system"))
                .addOptional(new ResourceLocation("cosmos", "mercury_wasteland"))
                .addOptional(new ResourceLocation("cosmos", "venuslands"))
                .addOptional(new ResourceLocation("cosmos", "earth_moon"))
                .addOptional(new ResourceLocation("cosmos", "marslands"))
                .addOptional(new ResourceLocation("cosmos", "jupiterlands"))
                .addOptional(new ResourceLocation("cosmos", "europa_lands"))
                .addOptional(new ResourceLocation("cosmos", "saturn_lands"))
                .addOptional(new ResourceLocation("cosmos", "uranus_lands"))
                .addOptional(new ResourceLocation("cosmos", "neptune_lands"))
                .addOptional(new ResourceLocation("cosmos", "plutowastelands"))

                .addOptional(new ResourceLocation("cosmos", "alpha_system"))
                .addOptional(new ResourceLocation("cosmos", "b_1400_centauri"))
                .addOptional(new ResourceLocation("cosmos", "gaia_bh_1"))
                .addOptional(new ResourceLocation("cosmos", "glacio_lands"))
                .addOptional(new ResourceLocation("cosmos", "j_1407blands"))
                .addOptional(new ResourceLocation("cosmos", "j_1900"));

        this.tag(BODimensionTags.COLD)
                .add(ResourceKey.create(Registries.DIMENSION, new ResourceLocation("minecraft", "the_end")))
                .addOptional(new ResourceLocation("cosmos", "plutowastelands"))
                .addOptional(new ResourceLocation("cosmos", "glacio_lands"));

        this.tag(BODimensionTags.HOT)
                .add(ResourceKey.create(Registries.DIMENSION, new ResourceLocation("minecraft", "the_nether")))
                .addOptional(new ResourceLocation("cosmos", "mercury_wasteland"))
                .addOptional(new ResourceLocation("cosmos", "venuslands"));

    }
}
