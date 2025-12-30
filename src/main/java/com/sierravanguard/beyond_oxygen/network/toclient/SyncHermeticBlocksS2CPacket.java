package com.sierravanguard.beyond_oxygen.network.toclient;

import com.sierravanguard.beyond_oxygen.client.HermeticAreaClientManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;

public class SyncHermeticBlocksS2CPacket extends ToClientPacket {
    private final long areaId;
    private final Long shipId; 
    private final Set<Vec3> blocks;

    public SyncHermeticBlocksS2CPacket(long areaId, Set<Vec3> blocks, Long shipId) {
        this.areaId = areaId;
        this.blocks = blocks;
        this.shipId = shipId;
    }

    public SyncHermeticBlocksS2CPacket(FriendlyByteBuf buffer) {
        areaId = buffer.readVarLong();
        shipId = buffer.readBoolean() ? buffer.readVarLong() : null;
        blocks = buffer.readCollection(HashSet::new, buf -> new Vec3(
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble()));
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeVarLong(areaId);
        if (shipId != null) {
            buffer.writeBoolean(true);
            buffer.writeVarLong(shipId);
        } else {
            buffer.writeBoolean(false);
        }
        buffer.writeCollection(blocks, (buf, pos) -> {
            buffer.writeDouble(pos.x);
            buffer.writeDouble(pos.y);
            buffer.writeDouble(pos.z);
        });
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleClient(NetworkEvent.Context context) {
        context.enqueueWork(() -> HermeticAreaClientManager.registerHermeticBlocks(areaId, blocks, shipId));
    }
}
