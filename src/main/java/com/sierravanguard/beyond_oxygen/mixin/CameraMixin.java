package com.sierravanguard.beyond_oxygen.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.sierravanguard.beyond_oxygen.extensions.IEntityExtension;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(Camera.class)
public abstract class CameraMixin {
    @Unique private int neo$sealedGraceTicks = 0;
    @Unique private static final int GRACE_PERIOD = 5;

    @Inject(method = "tick()V", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player instanceof IEntityExtension extension) {

            if (extension.beyond_oxygen$isInSealedArea()) {
                neo$sealedGraceTicks = GRACE_PERIOD;
            } else if (neo$sealedGraceTicks > 0) {
                neo$sealedGraceTicks--;
            }
        }
    }


    @WrapMethod(method = "getFluidInCamera()Lnet/minecraft/world/level/material/FogType;")
    private FogType onGetFluidInCamera(Operation<FogType> original) {
        if (neo$sealedGraceTicks > 0) {
            return FogType.NONE;
        }
        return original.call();
    }

}
