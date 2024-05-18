package com.stevekung.minecartspawnerrevived.neoforge;

import com.stevekung.minecartspawnerrevived.SendSpawnDataPacket;
import com.stevekung.minecartspawnerrevived.client.ClientPacket;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SendSpawnDataPacketNeoForge
{
    public static void handle(SendSpawnDataPacket packet, IPayloadContext context)
    {
        context.enqueueWork(() -> ClientPacket.setSpawnerDisplay(packet.entityId(), packet.spawnDataTag()));
    }
}