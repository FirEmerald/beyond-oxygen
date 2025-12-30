package com.sierravanguard.beyond_oxygen.network;

import com.sierravanguard.beyond_oxygen.BeyondOxygen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class AbstractPacket {
    public abstract void write(FriendlyByteBuf buffer);

    public abstract boolean isDirectionValid(NetworkDirection direction);

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (isDirectionValid(context.getDirection())) handle(context);
        else BeyondOxygen.LOGGER.error("Received invalid packet {} for direction {}", this, context.getDirection());
        context.setPacketHandled(true);
    }

    public abstract void handle(NetworkEvent.Context context);
}
