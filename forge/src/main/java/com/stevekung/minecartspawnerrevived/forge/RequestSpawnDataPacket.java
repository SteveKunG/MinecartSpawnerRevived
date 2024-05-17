package com.stevekung.minecartspawnerrevived.forge;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class RequestSpawnDataPacket implements CustomPacketPayload
{
    private final int entityId;

    public RequestSpawnDataPacket(int entityId)
    {
        this.entityId = entityId;
    }

    public RequestSpawnDataPacket(FriendlyByteBuf buffer)
    {
        this.entityId = buffer.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buffer)
    {
        buffer.writeInt(this.entityId);
    }

    @Override
    public ResourceLocation id()
    {
        return MinecartSpawnerRevived.REQUEST_SPAWNDATA;
    }

    public void handle(CustomPayloadEvent.Context context)
    {
        context.enqueueWork(() ->
        {
            var player = context.getSender();
            var spawner = (MinecartSpawner) player.level().getEntity(this.entityId);

            if (spawner != null)
            {
                var level = spawner.level();
                sendSpawnDataPacket(player, this.entityId, spawner.getSpawner().getOrCreateNextSpawnData(level, level.getRandom(), spawner.blockPosition()));
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