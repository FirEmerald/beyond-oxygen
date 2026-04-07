package com.sierravanguard.beyond_oxygen.utils;

import com.sierravanguard.beyond_oxygen.BeyondOxygen;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocations {
    public static ResourceLocation beyondOxygen(String path) {
        return ResourceLocation.fromNamespaceAndPath(BeyondOxygen.MODID, path);
    }

    public static ResourceLocation cosmicHorizons(String path) {
        return ResourceLocation.fromNamespaceAndPath("cosmos", path);
    }

    public static ResourceLocation curios(String path) {
        return ResourceLocation.fromNamespaceAndPath("curios", path);
    }

    public static ResourceLocation forge(String path) {
        return ResourceLocation.fromNamespaceAndPath("forge", path);
    }

    public static ResourceLocation common(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }
}
