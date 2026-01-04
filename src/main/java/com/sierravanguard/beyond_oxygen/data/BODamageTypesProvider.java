package com.sierravanguard.beyond_oxygen.data;

import com.sierravanguard.beyond_oxygen.registry.BODamageSources;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class BODamageTypesProvider {
    public static void register(BootstapContext<DamageType> bootstrap) {
        bootstrap.register(BODamageSources.VACUUM_KEY, new DamageType(
                "beyond_oxygen.vacuum",
                DamageScaling.NEVER,
                0.0f,
                DamageEffects.DROWNING
        ));
        bootstrap.register(BODamageSources.BURN_KEY, new DamageType(
                "beyond_oxygen.burn",
                DamageScaling.NEVER,
                0.5f,
                DamageEffects.BURNING
        ));
        bootstrap.register(BODamageSources.FREEZE_KEY, new DamageType(
                "beyond_oxygen.freeze",
                DamageScaling.NEVER,
                0.5f,
                DamageEffects.FREEZING
        ));
        bootstrap.register(BODamageSources.HURT_KEY, new DamageType(
                "beyond_oxygen.hurt",
                DamageScaling.NEVER,
                0.0f
        ));
    }
}
