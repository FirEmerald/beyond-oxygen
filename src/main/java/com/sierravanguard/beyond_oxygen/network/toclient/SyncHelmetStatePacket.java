package com.sierravanguard.beyond_oxygen.network.toclient;

import com.sierravanguard.beyond_oxygen.capabilities.HelmetState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class SyncHelmetStatePacket extends ToClientPacket {
    private final boolean open;

    public SyncHelmetStatePacket(boolean open) {
        this.open = open;
    }

    public SyncHelmetStatePacket(FriendlyByteBuf buffer) {
        this.open = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(open);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleClient(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;
            HelmetState.get(player).ifPresent(state -> state.setOpen(open));
        });
    }
}
