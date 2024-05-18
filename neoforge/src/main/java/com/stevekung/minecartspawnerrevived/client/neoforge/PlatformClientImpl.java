package com.stevekung.minecartspawnerrevived.client.neoforge;

import com.stevekung.minecartspawnerrevived.RequestSpawnDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;

public class PlatformClientImpl
{
    public static void sendSpawnDataRequestOnLoad(int entityId)
    {
        if (Minecraft.getInstance().getConnection() != null)
        {
            Minecraft.getInstance().player.connection.send(new ServerboundCustomPayloadPacket(new RequestSpawnDataPacket(entityId)));
        }
    }
}