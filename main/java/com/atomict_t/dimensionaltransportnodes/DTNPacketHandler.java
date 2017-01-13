package com.atomict_t.dimensionaltransportnodes;

import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.packets.DTNodeTileEntityPacket;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class DTNPacketHandler {
    private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    public DTNPacketHandler() {
    }

    public static int nextID() {
        return packetId++;
    }

    public static void registerMessages(String channelName) {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        registerMessages();
    }

    public static void registerMessages() {
        // Register messages which are sent from the client to the server here:
        INSTANCE.registerMessage(DTNodeTileEntityPacket.Handler.class, DTNodeTileEntityPacket.class, nextID(), Side.SERVER);
    }
}
