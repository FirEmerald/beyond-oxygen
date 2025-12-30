package com.sierravanguard.beyond_oxygen.network.toclient;

import com.sierravanguard.beyond_oxygen.network.AbstractPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public abstract class ToClientPacket extends AbstractPacket {
    @Override
    public boolean isDirectionValid(NetworkDirection direction) {
        return direction == NetworkDirection.PLAY_TO_CLIENT;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        handleClient(context);
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void handleClient(NetworkEvent.Context context);
}
