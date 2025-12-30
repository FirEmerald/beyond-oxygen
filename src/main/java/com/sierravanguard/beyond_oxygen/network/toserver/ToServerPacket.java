package com.sierravanguard.beyond_oxygen.network.toserver;

import com.sierravanguard.beyond_oxygen.network.AbstractPacket;
import net.minecraftforge.network.NetworkDirection;

public abstract class ToServerPacket extends AbstractPacket {
    @Override
    public boolean isDirectionValid(NetworkDirection direction) {
        return direction == NetworkDirection.PLAY_TO_SERVER;
    }
}
