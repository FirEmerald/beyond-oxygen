package com.sierravanguard.beyond_oxygen.network;

import com.sierravanguard.beyond_oxygen.BeyondOxygen;
import com.sierravanguard.beyond_oxygen.network.toclient.*;
import com.sierravanguard.beyond_oxygen.network.toserver.BubbleRadiusPacket;
import com.sierravanguard.beyond_oxygen.network.toserver.SetHelmetOpenPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "3";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(BeyondOxygen.MODID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;
    public static int nextID() {
        return packetId++;
    }

    public static void register() {
        register(SetHelmetOpenPacket.class, SetHelmetOpenPacket::new);
        register(SyncHelmetStatePacket.class, SyncHelmetStatePacket::new);
        register(SyncEntityHelmetStatePacket.class, SyncEntityHelmetStatePacket::new);
        register(SyncSealedAreaStatusPacket.class, SyncSealedAreaStatusPacket::new);
        register(SyncEntitySealedAreaStatusPacket.class, SyncEntitySealedAreaStatusPacket::new);
        register(BubbleRadiusPacket.class, BubbleRadiusPacket::new);
        register(VentInfoMessage.class, VentInfoMessage::new);
        register(SyncHermeticBlocksS2CPacket.class, SyncHermeticBlocksS2CPacket::new);
        register(InvalidateHermeticAreasPacket.class, InvalidateHermeticAreasPacket::new);
    }

    private static <T extends AbstractPacket> void register(Class<T> clazz, Function<FriendlyByteBuf, T> fromNetwork) {
        CHANNEL.registerMessage(nextID(), clazz, AbstractPacket::write, fromNetwork, AbstractPacket::handle);
    }

    public static void sendSetHelmetOpenPacket(boolean open) {
        CHANNEL.sendToServer(new SetHelmetOpenPacket(open));
    }

    public static void sendSealedAreaStatusToClients(Entity entity, boolean isInSealedArea) {
        if (entity instanceof ServerPlayer player) {
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                    new SyncSealedAreaStatusPacket(isInSealedArea));
        }
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                new SyncEntitySealedAreaStatusPacket(entity.getId(), isInSealedArea));
    }

    public static void sendSealedAreaStatusToClient(ServerPlayer player, Entity entity, boolean isInSealedArea) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                new SyncEntitySealedAreaStatusPacket(entity.getId(), isInSealedArea));
    }

    public static void sendToAllPlayers(Object pkt) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), pkt);
    }
    public static void sendInvalidateHermeticAreas(long areaId, boolean clearAll) {
        InvalidateHermeticAreasPacket pkt = new InvalidateHermeticAreasPacket(areaId, clearAll);
        CHANNEL.send(PacketDistributor.ALL.noArg(), pkt);
    }

}
