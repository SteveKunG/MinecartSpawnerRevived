package com.stevekung.minecartspawnerrevived.forge;

import com.stevekung.minecartspawnerrevived.RequestSpawnDataPacket;
import com.stevekung.minecartspawnerrevived.SendSpawnDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class RequestSpawnDataPacketForge
{
    public static void handle(RequestSpawnDataPacket packet, CustomPayloadEvent.Context context)
    {
        context.enqueueWork(() ->
        {
            var player = context.getSender();
            var spawner = (MinecartSpawner) player.level().getEntity(packet.entityId());

            if (spawner != null)
            {
                var level = spawner.level();
                sendSpawnDataPacket(player, packet.entityId(), spawner.getSpawner().getOrCreateNextSpawnData(level, level.getRandom(), spawner.blockPosition()));
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
        MinecartSpawnerRevivedForge.sendToClient(new SendSpawnDataPacket(entityId, compound), player);
    }
}