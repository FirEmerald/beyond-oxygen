package com.sierravanguard.beyond_oxygen.mixin;

import com.sierravanguard.beyond_oxygen.extensions.IEntityExtension;
import com.sierravanguard.beyond_oxygen.network.NetworkHandler;
import com.sierravanguard.beyond_oxygen.utils.HermeticArea;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntityExtension {
    @Shadow private Level level;

    @Shadow public abstract double getX();
    @Shadow public abstract double getY();
    @Shadow public abstract double getZ();

    @Shadow public abstract void setSwimming(boolean swimming);
    @Shadow protected boolean wasEyeInWater;
    @Shadow protected boolean wasTouchingWater;
    @Shadow @Final private Set<TagKey<Fluid>> fluidOnEyes;
    @Shadow(remap = false) private FluidType forgeFluidTypeOnEyes;
    @Shadow public abstract boolean isInWater();

    @Unique
    private boolean neo$isInSealedArea = false;
    @Unique
    private final Set<HermeticArea> beyond_oxygen$areas = new HashSet<>();
    @Unique
    private int ticksSinceSync = 0;

    @Unique
    private Entity beyond_oxygen$self() {
        return (Entity) (Object) this;
    }

    @Inject(method = "baseTick", at = @At("HEAD"))
    private void onBaseTick(CallbackInfo ci) {
        if (level.isClientSide()) return;
        Entity self = beyond_oxygen$self();
        boolean wasInSealedArea = neo$isInSealedArea;
        if (beyond_oxygen$areas.isEmpty()) {
            neo$isInSealedArea = false;
        } else {
            List<HermeticArea> toRemove = new ArrayList<>();
            beyond_oxygen$areas.forEach(area -> {
                if (!area.contains(self)) toRemove.add(area);
            });
            toRemove.forEach(area -> area.removeEntity(self, beyond_oxygen$areas));
            neo$isInSealedArea = !beyond_oxygen$areas.isEmpty();
        }
        if (wasInSealedArea != neo$isInSealedArea || ticksSinceSync >= 200) {
            NetworkHandler.sendSealedAreaStatusToClients(self, neo$isInSealedArea);
            ticksSinceSync = 0;
        } else ticksSinceSync++;
    }

    @Override
    public Collection<HermeticArea> beyond_oxygen$getAreasIn() {
        return beyond_oxygen$areas;
    }

    @Override
    public boolean beyond_oxygen$isInSealedArea() {
        return neo$isInSealedArea;
    }

    @Override
    public void beyond_oxygen$setIsInSealedArea(boolean value) {
        this.neo$isInSealedArea = value;
    }

    @WrapMethod(method = "updateFluidHeightAndDoFluidPushing()V", remap = false)
    private void onUpdateFluidHeightAndDoFluidPushing(Operation<Void> original) {
        if (beyond_oxygen$isInSealedArea()) {
            this.wasTouchingWater = false;
            return;
        }
        original.call();
    }

    @WrapMethod(method = "updateFluidOnEyes")
    private void onUpdateFluidOnEyes(Operation<Void> original) {
        if (beyond_oxygen$isInSealedArea()) {
            this.wasEyeInWater = false;
            this.fluidOnEyes.clear();
            this.forgeFluidTypeOnEyes = ForgeMod.EMPTY_TYPE.get();
            return;
        }
        original.call();
    }

    @WrapMethod(method = "updateSwimming")
    private void onUpdateSwimming(Operation<Void> original) {
        if (beyond_oxygen$isInSealedArea()) {
            this.setSwimming(false);
            return;
        }
        original.call();
    }

    @WrapMethod(method = "isInBubbleColumn")
    private boolean onIsInBubbleColumn(Operation<Boolean> original) {
        if (beyond_oxygen$isInSealedArea()) return false;
        return original.call();
    }
}
