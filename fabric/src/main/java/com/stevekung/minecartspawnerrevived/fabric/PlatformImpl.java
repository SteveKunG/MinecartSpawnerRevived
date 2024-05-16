package com.stevekung.minecartspawnerrevived.fabric;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.MinecartSpawner;

public class PlatformImpl
{
    public static void sendPacketOnInteract(MinecartSpawner entity)
    {
        if (entity.level() instanceof ServerLevel)
        {
            for (var serverPlayer : PlayerLookup.tracking(entity))
            {
                MinecartSpawnerRevivedFabric.sendSpawnDataPacket(serverPlayer, entity.getId(), entity.getSpawner().getOrCreateNextSpawnData(entity.level(), entity.level().getRandom(), entity.blockPosition()));
            }
        }
    }
}