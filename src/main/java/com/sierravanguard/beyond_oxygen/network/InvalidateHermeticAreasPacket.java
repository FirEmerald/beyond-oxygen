package com.sierravanguard.beyond_oxygen.network;

import com.sierravanguard.beyond_oxygen.BeyondOxygen;
import com.sierravanguard.beyond_oxygen.client.HermeticAreaClientManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

 
public class InvalidateHermeticAreasPacket {
    private final long areaId;
    private final boolean clearAll;

    public InvalidateHermeticAreasPacket(long areaId, boolean clearAll) {
        this.areaId = areaId;
        this.clearAll = clearAll;
    }

    public static void encode(InvalidateHermeticAreasPacket msg, FriendlyByteBuf buf) {
        buf.writeLong(msg.areaId);
        buf.writeBoolean(msg.clearAll);
    }

    public static InvalidateHermeticAreasPacket decode(FriendlyByteBuf buf) {
        return new InvalidateHermeticAreasPacket(buf.readLong(), buf.readBoolean());
    }

    public static void handle(InvalidateHermeticAreasPacket msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {
                if (msg.clearAll) {
                    HermeticAreaClientManager.clear();
                    BeyondOxygen.LOGGER.info("Cleared all client hermetic areas.");
                } else {
                    HermeticAreaClientManager.clearArea(msg.areaId);
                    BeyondOxygen.LOGGER.info("Cleared client hermetic area for areaId {}", msg.areaId);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
