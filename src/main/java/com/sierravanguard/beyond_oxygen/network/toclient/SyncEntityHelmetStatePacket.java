package com.sierravanguard.beyond_oxygen.network.toclient;

import com.sierravanguard.beyond_oxygen.capabilities.HelmetState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class SyncEntityHelmetStatePacket extends ToClientPacket {
    private final int entityId;
    private final boolean open;

    public SyncEntityHelmetStatePacket(int entityId, boolean open) {
        this.entityId = entityId;
        this.open = open;
    }

    public SyncEntityHelmetStatePacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readVarInt();
        this.open = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeBoolean(open);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleClient(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level == null) return;
            Entity entity = level.getEntity(entityId);
            if (!(entity instanceof LivingEntity livingEntity)) return;
            HelmetState.get(livingEntity).ifPresent(state -> {
                state.setOpen(open);
            });
        });
    }
}
