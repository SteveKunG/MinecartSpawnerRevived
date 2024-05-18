package com.stevekung.minecartspawnerrevived.forge;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.MinecartSpawner;

public class PlatformImpl
{
    public static void sendPacketOnInteract(MinecartSpawner entity)
    {
        if (entity.level() instanceof ServerLevel serverLevel)
        {
            for (var serverPlayer : serverLevel.getPlayers(serverPlayer -> serverPlayer.isAlive() && serverPlayer.distanceTo(entity) < 256.0F))
            {
                RequestSpawnDataPacketForge.sendSpawnDataPacket(serverPlayer, entity.getId(), entity.getSpawner().getOrCreateNextSpawnData(entity.level(), entity.level().getRandom(), entity.blockPosition()));
            }
        }
    }
}