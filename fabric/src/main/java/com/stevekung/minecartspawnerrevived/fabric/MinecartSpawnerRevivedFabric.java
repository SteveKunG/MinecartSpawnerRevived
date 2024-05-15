package com.stevekung.minecartspawnerrevived.fabric;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;

public class MinecartSpawnerRevivedFabric
{
    public static void init()
    {
        MinecartSpawnerRevived.init();
        ServerPlayNetworking.registerGlobalReceiver(MinecartSpawnerRevived.REQUEST_SPAWNDATA, MinecartSpawnerRevivedFabric::requestSpawnData);
    }

    public static void requestSpawnData(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender)
    {
        var entityId = buf.readVarInt();

        // Make sure to run on the server thread because we use level.getRandom() to get SpawnData from the server side. This will prevent "Accessing LegacyRandomSource from multiple threads" error.
        server.execute(() ->
        {
            var spawner = (MinecartSpawner)player.level().getEntity(entityId);

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

        var packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeInt(entityId);
        var compound = new CompoundTag();
        compound.put(BaseSpawner.SPAWN_DATA_TAG, SpawnData.CODEC.encodeStart(NbtOps.INSTANCE, spawnData).result().orElseThrow(() -> new IllegalStateException("Invalid SpawnData")));
        packetByteBuf.writeNbt(compound);
        ServerPlayNetworking.send(player, MinecartSpawnerRevived.SEND_SPAWNDATA, packetByteBuf);
    }
}