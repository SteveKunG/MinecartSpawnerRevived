package com.stevekung.minecartspawnerrevived.forge;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;
import net.minecraftforge.network.NetworkEvent;

public class RequestSpawnDataPacket
{
    private final int entityId;

    public RequestSpawnDataPacket(int entityId)
    {
        this.entityId = entityId;
    }

    public RequestSpawnDataPacket(FriendlyByteBuf buf)
    {
        entityId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeInt(entityId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            var player = ctx.get().getSender();
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
        MinecartSpawnerRevivedForge.sendToClient(new SendSpawnDataPacket(entityId, compound), player);
    }
}