package com.sierravanguard.beyond_oxygen.network.toserver;

import com.sierravanguard.beyond_oxygen.BOConfig;
import com.sierravanguard.beyond_oxygen.blocks.entity.BubbleGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BubbleRadiusPacket extends ToServerPacket {
    private final BlockPos pos;
    private final boolean increase;

    public BubbleRadiusPacket(BlockPos pos, boolean increase) {
        this.pos = pos;
        this.increase = increase;
    }

    public BubbleRadiusPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.increase = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeBoolean(increase);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.level().getBlockEntity(pos) instanceof BubbleGeneratorBlockEntity be) {
                float step = 0.5f;
                float newRadius = be.controlledMaxRadius + (increase ? step : -step);
                newRadius = clamp(newRadius, 0.5f, BOConfig.getBubbleMaxRadius());

                be.controlledMaxRadius = newRadius;

                be.setChanged();
                be.getLevel().sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 3);
            }
        });
    }

    private static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}
