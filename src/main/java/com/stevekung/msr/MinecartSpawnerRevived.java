package com.stevekung.msr;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;

public class MinecartSpawnerRevived
{
    public static final String MOD_ID = "minecart_spawner_revived";
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Use to request SpawnData from the client side. Then sent the SpawnData into the server.
     */
    public static final ResourceLocation REQUEST_SPAWNDATA = new ResourceLocation(MOD_ID, "request_spawndata");

    /**
     * Use to send SpawnData to the client side to set spawner display.
     */
    public static final ResourceLocation SEND_SPAWNDATA = new ResourceLocation(MOD_ID, "send_spawndata");

    public static void init()
    {
        LOGGER.info("MinecartSpawnerRevived loaded, #PlsAddMinecartSpawnerItem!");
        ServerPlayNetworking.registerGlobalReceiver(MinecartSpawnerRevived.REQUEST_SPAWNDATA, MinecartSpawnerRevived::requestSpawnData);
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