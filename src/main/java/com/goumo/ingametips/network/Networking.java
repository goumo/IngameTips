package com.goumo.ingametips.network;

import com.goumo.ingametips.IngameTips;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel CHANNEL;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static void register() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(IngameTips.MOD_ID, "ingametips"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );

        CHANNEL.registerMessage(ID++,
                TipPacket.class,
                TipPacket::encode,
                TipPacket::new,
                TipPacket::handler
        );

        CHANNEL.registerMessage(ID++,
                CustomTipPacket.class,
                CustomTipPacket::encode,
                CustomTipPacket::new,
                CustomTipPacket::handler
        );
    }

    public static void send(ServerPlayer player, String ID) {
        CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new TipPacket(ID)
        );
    }

    public static void sendCustom(ServerPlayer player,String title, String content, int visibleTime, boolean history) {
        CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new CustomTipPacket(title, content, visibleTime, history)
        );
    }
}

