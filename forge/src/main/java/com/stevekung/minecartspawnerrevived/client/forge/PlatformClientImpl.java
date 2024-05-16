package com.stevekung.minecartspawnerrevived.client.forge;

import com.stevekung.minecartspawnerrevived.forge.MinecartSpawnerRevivedForge;
import com.stevekung.minecartspawnerrevived.forge.RequestSpawnDataPacket;
import net.minecraft.client.Minecraft;

public class PlatformClientImpl
{
    public static void sendSpawnDataRequestOnLoad(int entityId)
    {
        if (Minecraft.getInstance().getConnection() != null)
        {
            MinecartSpawnerRevivedForge.sendToServer(new RequestSpawnDataPacket(entityId));
        }
    }
}