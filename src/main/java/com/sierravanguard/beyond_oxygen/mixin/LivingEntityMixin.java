package com.sierravanguard.beyond_oxygen.mixin;

import com.sierravanguard.beyond_oxygen.compat.CompatLoader;
import com.sierravanguard.beyond_oxygen.compat.CompatUtils;
import com.sierravanguard.beyond_oxygen.extensions.ILivingEntityExtension;
import com.sierravanguard.beyond_oxygen.network.NetworkHandler;
import com.sierravanguard.beyond_oxygen.registry.BODamageSources;
import com.sierravanguard.beyond_oxygen.registry.BODimensions;
import com.sierravanguard.beyond_oxygen.registry.BOEffects;
import com.sierravanguard.beyond_oxygen.tags.BOEntityTypeTags;
import com.sierravanguard.beyond_oxygen.utils.*;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ILivingEntityExtension {
    @Unique
    private LivingEntity beyond_oxygen$self() {
        return (LivingEntity) (Object) this;
    }

    @Unique
    private int beyond_oxygen$vacuumDamageCooldown = 0;

    private LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void beyond_oxygen$tick() {
        if (level().isClientSide()) return;
        LivingEntity self = beyond_oxygen$self();
        OxygenManager.consumeOxygen(self);
        if (beyond_oxygen$vacuumDamageCooldown > 0) {
            beyond_oxygen$vacuumDamageCooldown--;
        }
        if (!self.hasEffect(BOEffects.OXYGEN_SATURATION.get())) {
            if (BODimensions.isUnbreathable(level())
                    && !self.getType().is(BOEntityTypeTags.SURVIVES_VACUUM)
                    && !OxygenHelper.isInBreathableEnvironment(self)
                    && !self.isUnderWater()) {
                if (beyond_oxygen$vacuumDamageCooldown <= 0) {
                    applyDamageWithMessage(self, BODamageSources.vacuum(), 5f);
                    beyond_oxygen$vacuumDamageCooldown = 20;
                }
                beyond_oxygen$vacuumDamageCooldown--;
            }
        }

        if (!CompatLoader.COLD_SWEAT.isLoaded()) {
            boolean blockedByThermalController = beyond_oxygen$getAreasIn().stream().anyMatch(HermeticArea::hasActiveTemperatureRegulator);
            if (!blockedByThermalController) {
                if (BODimensions.isHot(level())
                        && !self.getType().is(BOEntityTypeTags.SURVIVES_HOT)
                        && !SpaceSuitHandler.isWearingFullThermalSuit(self)) {
                    applyDamageWithMessage(self, BODamageSources.burn(), 5f);
                }
                if (BODimensions.isCold(level())
                        && !self.getType().is(BOEntityTypeTags.SURVIVES_COLD)
                        && !SpaceSuitHandler.isWearingFullCryoSuit(self)) {
                    applyDamageWithMessage(self, BODamageSources.freeze(), 5f);
                }
            }
        }

        if ((!(self instanceof WaterAnimal) || self.isInWaterOrBubble()) && self.hasEffect(BOEffects.OXYGEN_SATURATION.get())) {
            self.setAirSupply(self.getMaxAirSupply());
        }
    }

    private static void applyDamageWithMessage(LivingEntity player, DamageSource source, float amount) {
        if (player.level().isClientSide()) return;
        BODamageSources.applyCustomDamage(player, source, amount);
        DamageType type = source.getMsgId() != null ? source.type() : null;
        if (type != null) {
            player.getCombatTracker().recordDamage(source, amount);
        }
    }

    @Inject(method = "increaseAirSupply", at = @At("HEAD"), cancellable = true)
    private void beyondoxygen$preventAirRefillInVacuum(int airIncrement, CallbackInfoReturnable<Integer> cir) {
        LivingEntity self = beyond_oxygen$self();
        if (self.hasEffect(BOEffects.OXYGEN_SATURATION.get()) || beyond_oxygen$getAreasIn().stream().anyMatch(HermeticArea::hasAir)) return;
        BlockPos headPos = BlockPos.containing(self.getX(), self.getEyeY(), self.getZ());
        boolean headInWater = self.level().getFluidState(headPos).is(FluidTags.WATER);
        if (headInWater) {
            cir.setReturnValue(self.getAirSupply());
        }
    }

    @Inject(method = "baseTick", at = @At("HEAD"))
    private void beyondoxygen$decrementAirInVacuum(CallbackInfo ci) {
        LivingEntity self = beyond_oxygen$self();
        if (self.hasEffect(BOEffects.OXYGEN_SATURATION.get()) || beyond_oxygen$getAreasIn().stream().anyMatch(HermeticArea::hasAir)) return;
        int air = self.getAirSupply() - 1;
        self.setAirSupply(air);
        if (air == -20) {
            self.setAirSupply(0);
            self.hurt(self.damageSources().drown(), 2.0F);
        }
    }
}


