package com.sierravanguard.beyond_oxygen.network;

import com.sierravanguard.beyond_oxygen.extensions.IEntityExtension;
import com.sierravanguard.beyond_oxygen.extensions.ILivingEntityExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncSealedAreaStatusPacket {
    private final boolean isInSealedArea;

    public SyncSealedAreaStatusPacket(boolean isInSealedArea) {
        this.isInSealedArea = isInSealedArea;
    }

    public static void encode(SyncSealedAreaStatusPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.isInSealedArea);
    }

    public static SyncSealedAreaStatusPacket decode(FriendlyByteBuf buf) {
        return new SyncSealedAreaStatusPacket(buf.readBoolean());
    }

    public static void handle(SyncSealedAreaStatusPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().player instanceof IEntityExtension extension) extension.beyond_oxygen$setIsInSealedArea(msg.isInSealedArea);
            });
        }
        context.setPacketHandled(true);
    }


}
