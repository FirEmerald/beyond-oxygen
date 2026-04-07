package com.sierravanguard.beyond_oxygen.mixin;

import com.sierravanguard.beyond_oxygen.compat.CompatLoader;
import com.sierravanguard.beyond_oxygen.extensions.ILivingEntityExtension;
import com.sierravanguard.beyond_oxygen.registry.BODamageSources;
import com.sierravanguard.beyond_oxygen.registry.BODimensions;
import com.sierravanguard.beyond_oxygen.tags.BOEntityTypeTags;
import com.sierravanguard.beyond_oxygen.utils.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

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
        if (getAirSupply() <= 0 && BODimensions.isUnbreathable(level())) {
            if (beyond_oxygen$vacuumDamageCooldown <= 0) {
                beyondoxygen$applyDamageWithMessage(self, BODamageSources.vacuum(), 5f);
                beyond_oxygen$vacuumDamageCooldown = 20;
            }
        }

        if (!CompatLoader.COLD_SWEAT.isLoaded()) {
            boolean blockedByThermalController = beyond_oxygen$getAreasIn().stream().anyMatch(HermeticArea::hasActiveTemperatureRegulator);
            if (!blockedByThermalController) {
                if (BODimensions.isHot(level())
                        && !getType().is(BOEntityTypeTags.SURVIVES_HOT)
                        && !SpaceSuitHandler.isWearingFullThermalSuit(self)) {
                    beyondoxygen$applyDamageWithMessage(self, BODamageSources.burn(), 5f);
                }
                if (BODimensions.isCold(level())
                        && !getType().is(BOEntityTypeTags.SURVIVES_COLD)
                        && !SpaceSuitHandler.isWearingFullCryoSuit(self)) {
                    beyondoxygen$applyDamageWithMessage(self, BODamageSources.freeze(), 5f);
                }
            }
        }
    }

    @Unique
    private static void beyondoxygen$applyDamageWithMessage(LivingEntity player, DamageSource source, float amount) {
        if (player.level().isClientSide()) return;
        BODamageSources.applyCustomDamage(player, source, amount);
        DamageType type = source.getMsgId() != null ? source.type() : null;
        if (type != null) {
            player.getCombatTracker().recordDamage(source, amount);
        }
    }
}


