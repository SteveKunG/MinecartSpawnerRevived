package com.stevekung.minecartspawnerrevived.forge;

import com.stevekung.minecartspawnerrevived.SendSpawnDataPacket;
import com.stevekung.minecartspawnerrevived.client.ClientPacket;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class SendSpawnDataPacketForge
{
    public static void handle(SendSpawnDataPacket packet, CustomPayloadEvent.Context context)
    {
        context.enqueueWork(() -> ClientPacket.setSpawnerDisplay(packet.entityId(), packet.spawnDataTag()));
    }
}