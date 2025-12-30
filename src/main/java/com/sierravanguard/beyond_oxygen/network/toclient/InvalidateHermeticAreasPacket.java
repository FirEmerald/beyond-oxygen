package com.sierravanguard.beyond_oxygen.network.toclient;

import com.sierravanguard.beyond_oxygen.BeyondOxygen;
import com.sierravanguard.beyond_oxygen.client.HermeticAreaClientManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

 
public class InvalidateHermeticAreasPacket extends ToClientPacket {
    private final long areaId;
    private final boolean clearAll;

    public InvalidateHermeticAreasPacket(long areaId, boolean clearAll) {
        this.areaId = areaId;
        this.clearAll = clearAll;
    }

    public InvalidateHermeticAreasPacket(FriendlyByteBuf buffer) {
        this.areaId = buffer.readVarLong();
        this.clearAll = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeVarLong(areaId);
        buffer.writeBoolean(clearAll);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleClient(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {
                if (clearAll) {
                    HermeticAreaClientManager.clear();
                    BeyondOxygen.LOGGER.info("Cleared all client hermetic areas.");
                } else {
                    HermeticAreaClientManager.clearArea(areaId);
                    BeyondOxygen.LOGGER.info("Cleared client hermetic area for areaId {}", areaId);
                }
            }
        });
    }
}
