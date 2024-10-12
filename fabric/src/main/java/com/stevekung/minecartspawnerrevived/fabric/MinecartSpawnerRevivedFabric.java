package com.stevekung.minecartspawnerrevived.fabric;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import com.stevekung.minecartspawnerrevived.RequestSpawnDataPacket;
import com.stevekung.minecartspawnerrevived.SendSpawnDataPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;

public class MinecartSpawnerRevivedFabric
{
    public static void init()
    {
        MinecartSpawnerRevived.init();
        PayloadTypeRegistry.playC2S().register(RequestSpawnDataPacket.TYPE, RequestSpawnDataPacket.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(RequestSpawnDataPacket.TYPE, MinecartSpawnerRevivedFabric::requestSpawnData);
    }

    public static void requestSpawnData(RequestSpawnDataPacket packet, ServerPlayNetworking.Context context)
    {
        var entityId = packet.entityId();
        var player = context.player();

        // Make sure to run on the server thread because we use level.getRandom() to get SpawnData from the server side. This will prevent "Accessing LegacyRandomSource from multiple threads" error.
        player.getServer().execute(() ->
        {
            var spawner = (MinecartSpawner) player.level().getEntity(entityId);

            if (spawner != null)
            {
                var level = spawner.level();
                sendSpawnDataPacket(player, entityId, spawner.getSpawner().getOrCreateNextSpawnData(level, level.getRandom(), spawner.blockPosition()));
            }
        });
    }

    public static void sendSpawnDataPacket(ServerPlayer player, int entityId, SpawnData spawnData)
    {
        // If an entity to spawn is empty, ignore it.
        if (spawnData.entityToSpawn().isEmpty())
        {
            return;
        }

        var compound = new CompoundTag();
        compound.put(BaseSpawner.SPAWN_DATA_TAG, SpawnData.CODEC.encodeStart(NbtOps.INSTANCE, spawnData).result().orElseThrow(() -> new IllegalStateException("Invalid SpawnData")));

        if (ServerPlayNetworking.canSend(player, MinecartSpawnerRevived.SEND_SPAWNDATA))
        {
            ServerPlayNetworking.send(player, new SendSpawnDataPacket(entityId, compound));
        }
    }
}