package com.sierravanguard.beyond_oxygen.network.toclient;

import com.sierravanguard.beyond_oxygen.extensions.IEntityExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class SyncSealedAreaStatusPacket extends ToClientPacket {
    private final boolean isInSealedArea;

    public SyncSealedAreaStatusPacket(boolean isInSealedArea) {
        this.isInSealedArea = isInSealedArea;
    }

    public SyncSealedAreaStatusPacket(FriendlyByteBuf buffer) {
        this.isInSealedArea = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(isInSealedArea);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleClient(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().player instanceof IEntityExtension extension) extension.beyond_oxygen$setIsInSealedArea(isInSealedArea);
        });
    }
}
