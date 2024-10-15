package com.stevekung.minecartspawnerrevived.fabric;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import com.stevekung.minecartspawnerrevived.RequestSpawnDataPacket;
import com.stevekung.minecartspawnerrevived.SendSpawnDataPacket;
import com.stevekung.minecartspawnerrevived.client.ClientPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class MinecartSpawnerRevivedClientFabric
{
    public static void init()
    {
        ClientPlayNetworking.registerGlobalReceiver(SendSpawnDataPacket.TYPE, MinecartSpawnerRevivedClientFabric::setSpawnerDisplay);
    }

    public static void sendSpawnDataRequest(int entityId)
    {
        if (ClientPlayNetworking.canSend(MinecartSpawnerRevived.REQUEST_SPAWNDATA))
        {
            ClientPlayNetworking.send(new RequestSpawnDataPacket(entityId));
        }
    }

    public static void setSpawnerDisplay(SendSpawnDataPacket packet, ClientPlayNetworking.Context context)
    {
        ClientPacket.setSpawnerDisplay(packet.entityId(), packet.spawnDataTag());
    }
}