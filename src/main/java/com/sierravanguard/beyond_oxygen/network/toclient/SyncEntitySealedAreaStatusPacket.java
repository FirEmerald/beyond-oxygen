package com.sierravanguard.beyond_oxygen.network.toclient;

import com.sierravanguard.beyond_oxygen.extensions.IEntityExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class SyncEntitySealedAreaStatusPacket extends ToClientPacket {
    private final int entityId;
    private final boolean isInSealedArea;

    public SyncEntitySealedAreaStatusPacket(int entityId, boolean isInSealedArea) {
        this.entityId = entityId;
        this.isInSealedArea = isInSealedArea;
    }

    public SyncEntitySealedAreaStatusPacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readVarInt();
        this.isInSealedArea = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeBoolean(isInSealedArea);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleClient(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                Entity entity = level.getEntity(entityId);
                if (entity instanceof IEntityExtension extension) extension.beyond_oxygen$setIsInSealedArea(isInSealedArea);
            }
        });
    }
}
