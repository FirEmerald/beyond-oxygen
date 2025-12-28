package com.sierravanguard.beyond_oxygen.network;

import com.sierravanguard.beyond_oxygen.extensions.IEntityExtension;
import com.sierravanguard.beyond_oxygen.extensions.ILivingEntityExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncEntitySealedAreaStatusPacket {
    private final int entityId;
    private final boolean isInSealedArea;

    public SyncEntitySealedAreaStatusPacket(int entityId, boolean isInSealedArea) {
        this.entityId = entityId;
        this.isInSealedArea = isInSealedArea;
    }

    public static void encode(SyncEntitySealedAreaStatusPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.entityId);
        buf.writeBoolean(msg.isInSealedArea);
    }

    public static SyncEntitySealedAreaStatusPacket decode(FriendlyByteBuf buf) {
        return new SyncEntitySealedAreaStatusPacket(buf.readVarInt(), buf.readBoolean());
    }

    public static void handle(SyncEntitySealedAreaStatusPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                Level level = Minecraft.getInstance().level;
                if (level != null) {
                    Entity entity = level.getEntity(msg.entityId);
                    if (entity instanceof IEntityExtension extension) extension.beyond_oxygen$setIsInSealedArea(msg.isInSealedArea);
                }
            });
        }
        context.setPacketHandled(true);
    }


}
