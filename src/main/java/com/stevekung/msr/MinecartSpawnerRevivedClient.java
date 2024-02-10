package com.stevekung.msr;

import java.util.function.Function;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;

public class MinecartSpawnerRevivedClient
{
    public static void init()
    {
        PayloadTypeRegistry.playS2C().register(SendSpawnDataPacket.TYPE, SendSpawnDataPacket.CODEC);

        ClientPlayNetworking.registerGlobalReceiver(SendSpawnDataPacket.TYPE, MinecartSpawnerRevivedClient::setSpawnerDisplay);
    }

    public static void sendSpawnDataRequest(int entityId)
    {
        ClientPlayNetworking.send(new RequestSpawnDataPacket(entityId));
    }

    public static void setSpawnerDisplay(SendSpawnDataPacket packet, ClientPlayNetworking.Context context)
    {
        var entityId = packet.entityId();
        var compoundTag = packet.spawnDataTag();
        var level = context.client().level;

        context.client().execute(() ->
        {
            if (level != null)
            {
                var spawner = (MinecartSpawner)level.getEntity(entityId);

                if (spawner == null || compoundTag == null)
                {
                    return;
                }

                var spawnData = SpawnData.CODEC.parse(NbtOps.INSTANCE, compoundTag.getCompound(BaseSpawner.SPAWN_DATA_TAG)).resultOrPartial(string -> MinecartSpawnerRevived.LOGGER.warn("Invalid SpawnData: {}", string)).orElseGet(SpawnData::new);
                spawner.getSpawner().displayEntity = EntityType.loadEntityRecursive(spawnData.entityToSpawn(), level, Function.identity());
            }
        });
    }
}