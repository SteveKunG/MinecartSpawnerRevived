package com.stevekung.minecartspawnerrevived.client.forge;

import com.stevekung.minecartspawnerrevived.forge.MinecartSpawnerRevivedForge;
import com.stevekung.minecartspawnerrevived.forge.RequestSpawnDataPacket;
import net.minecraft.client.Minecraft;

public class PlatformClientImpl
{
    public static void sendSpawnDataRequestOnLoad(int entityId)
    {
        var connection = Minecraft.getInstance().getConnection();

        if (connection != null && MinecartSpawnerRevivedForge.INSTANCE.isRemotePresent(connection.getConnection()))
        {
            MinecartSpawnerRevivedForge.sendToServer(new RequestSpawnDataPacket(entityId));
        }
    }
}