package com.stevekung.minecartspawnerrevived.client.fabric;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import com.stevekung.minecartspawnerrevived.fabric.MinecartSpawnerRevivedClientFabric;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class PlatformClientImpl
{
    public static void sendSpawnDataRequestOnLoad(int entityId)
    {
        if (ClientPlayNetworking.canSend(MinecartSpawnerRevived.REQUEST_SPAWNDATA))
        {
            MinecartSpawnerRevivedClientFabric.sendSpawnDataRequest(entityId);
        }
    }
}