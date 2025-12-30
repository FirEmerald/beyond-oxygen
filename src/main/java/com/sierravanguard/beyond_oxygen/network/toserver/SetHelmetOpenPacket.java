package com.sierravanguard.beyond_oxygen.network.toserver;

import com.sierravanguard.beyond_oxygen.capabilities.HelmetState;
import com.sierravanguard.beyond_oxygen.network.NetworkHandler;
import com.sierravanguard.beyond_oxygen.network.toclient.SyncHelmetStatePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import org.valkyrienskies.core.impl.shadow.Fr;

import java.util.function.Supplier;

public class SetHelmetOpenPacket extends ToServerPacket {
    private final boolean open;

    public SetHelmetOpenPacket(boolean open) {
        this.open = open;
    }

    public SetHelmetOpenPacket(FriendlyByteBuf buffer) {
        this.open = buffer.readBoolean();
    }


    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(open);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            Player sender = context.getSender();
            if (sender == null) return;

            HelmetState.get(sender).ifPresent(state -> {
                if (state.isOpen() != open) {
                    state.setOpen(open, false);
                    NetworkHandler.CHANNEL.sendTo(
                            new SyncHelmetStatePacket(open),
                            context.getNetworkManager(),
                            NetworkDirection.PLAY_TO_CLIENT
                    );
                }
            });
        });
    }
}
