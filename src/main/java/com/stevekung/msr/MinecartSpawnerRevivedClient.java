package com.stevekung.msr;

import java.util.function.Function;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;

public class MinecartSpawnerRevivedClient
{
    public static void init()
    {
        ClientPlayNetworking.registerGlobalReceiver(MinecartSpawnerRevived.SEND_SPAWNDATA, MinecartSpawnerRevivedClient::setSpawnerDisplay);
    }

    public static void sendSpawnDataRequest(int entityId)
    {
        var buff = PacketByteBufs.create();
        buff.writeVarInt(entityId);
        ClientPlayNetworking.send(MinecartSpawnerRevived.REQUEST_SPAWNDATA, buff);
    }

    public static void setSpawnerDisplay(Minecraft minecraft, ClientPacketListener listener, FriendlyByteBuf buf, PacketSender responseSender)
    {
        var entityId = buf.readInt();
        var compoundTag = buf.readNbt();
        var level = minecraft.level;

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
    }
}