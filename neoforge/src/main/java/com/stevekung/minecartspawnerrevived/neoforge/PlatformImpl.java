package com.stevekung.minecartspawnerrevived.neoforge;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.minecart.MinecartSpawner;

public class PlatformImpl
{
    public static void sendPacketOnInteract(MinecartSpawner entity)
    {
        if (entity.level() instanceof ServerLevel serverLevel)
        {
            for (var serverPlayer : serverLevel.getPlayers(serverPlayer -> serverPlayer.isAlive() && serverPlayer.distanceTo(entity) < 256.0F))
            {
                RequestSpawnDataPacketNeoForge.sendSpawnDataPacket(serverPlayer, entity.getId(), entity.getSpawner().getOrCreateNextSpawnData(entity.level(), entity.level().getRandom(), entity.blockPosition()));
            }
        }
    }
}