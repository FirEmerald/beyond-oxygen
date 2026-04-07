package com.sierravanguard.beyond_oxygen.tags;

import com.sierravanguard.beyond_oxygen.BeyondOxygen;
import com.sierravanguard.beyond_oxygen.utils.ResourceLocations;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class BOEntityTypeTags {
    public static final TagKey<EntityType<?>> SURVIVES_VACUUM = beyondOxygen("survives_vacuum");
    public static final TagKey<EntityType<?>> SURVIVES_COLD = beyondOxygen("survives_cold");
    public static final TagKey<EntityType<?>> SURVIVES_HOT = beyondOxygen("survives_hot");

    private static TagKey<EntityType<?>> beyondOxygen(String path) {
        return create(ResourceLocations.beyondOxygen(path));
    }

    private static TagKey<EntityType<?>> create(ResourceLocation location) {
        return TagKey.create(Registries.ENTITY_TYPE, location);
    }
}
