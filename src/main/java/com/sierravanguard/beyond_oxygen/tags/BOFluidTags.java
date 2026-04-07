package com.sierravanguard.beyond_oxygen.tags;

import com.sierravanguard.beyond_oxygen.utils.ResourceLocations;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class BOFluidTags {
    public static final TagKey<Fluid> OXYGEN = FluidTags.create(ResourceLocations.common("oxygen"));
    public static final TagKey<Fluid> FORGE_OXYGEN = FluidTags.create(ResourceLocations.forge("oxygen"));
}
