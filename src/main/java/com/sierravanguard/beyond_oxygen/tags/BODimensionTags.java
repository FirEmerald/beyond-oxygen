package com.sierravanguard.beyond_oxygen.tags;

import com.sierravanguard.beyond_oxygen.utils.ResourceLocations;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;

public class BODimensionTags {
    public static final TagKey<Level> UNBREATHABLE = beyondOxygen("unbreathable");
    public static final TagKey<Level> COLD = beyondOxygen("cold");
    public static final TagKey<Level> HOT = beyondOxygen("hot");

    private static TagKey<Level> beyondOxygen(String path) {
        return create(ResourceLocations.beyondOxygen(path));
    }

    private static TagKey<Level> create(ResourceLocation location) {
        return TagKey.create(Registries.DIMENSION, location);
    }
}
