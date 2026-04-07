package com.sierravanguard.beyond_oxygen.cap;

import com.sierravanguard.beyond_oxygen.registry.BOFluids;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import org.jetbrains.annotations.NotNull;

public class OxygenTankCap extends FluidHandlerItemStack {
    public OxygenTankCap(@NotNull ItemStack container, int capacity) {
        super(container, capacity);
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        return BOFluids.isOxygen(fluid);
    }
}