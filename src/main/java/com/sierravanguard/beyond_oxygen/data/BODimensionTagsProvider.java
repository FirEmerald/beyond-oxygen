package com.sierravanguard.beyond_oxygen.data;

import com.sierravanguard.beyond_oxygen.BeyondOxygen;
import com.sierravanguard.beyond_oxygen.tags.BODimensionTags;
import com.sierravanguard.beyond_oxygen.utils.ResourceLocations;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
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
                .addOptional(ResourceLocations.cosmicHorizons("solar_system"))
                .addOptional(ResourceLocations.cosmicHorizons("mercury_wasteland"))
                .addOptional(ResourceLocations.cosmicHorizons("venuslands"))
                .addOptional(ResourceLocations.cosmicHorizons("earth_moon"))
                .addOptional(ResourceLocations.cosmicHorizons("marslands"))
                .addOptional(ResourceLocations.cosmicHorizons("jupiterlands"))
                .addOptional(ResourceLocations.cosmicHorizons("europa_lands"))
                .addOptional(ResourceLocations.cosmicHorizons("saturn_lands"))
                .addOptional(ResourceLocations.cosmicHorizons("uranus_lands"))
                .addOptional(ResourceLocations.cosmicHorizons("neptune_lands"))
                .addOptional(ResourceLocations.cosmicHorizons("plutowastelands"))

                .addOptional(ResourceLocations.cosmicHorizons("alpha_system"))
                .addOptional(ResourceLocations.cosmicHorizons("b_1400_centauri"))
                .addOptional(ResourceLocations.cosmicHorizons("gaia_bh_1"))
                .addOptional(ResourceLocations.cosmicHorizons("glacio_lands"))
                .addOptional(ResourceLocations.cosmicHorizons("j_1407blands"))
                .addOptional(ResourceLocations.cosmicHorizons("j_1900"));

        this.tag(BODimensionTags.COLD)
                .addOptional(ResourceLocations.cosmicHorizons("plutowastelands"))
                .addOptional(ResourceLocations.cosmicHorizons("glacio_lands"));

        this.tag(BODimensionTags.HOT)
                .addOptional(ResourceLocations.cosmicHorizons("mercury_wasteland"))
                .addOptional(ResourceLocations.cosmicHorizons("venuslands"));

    }
}
