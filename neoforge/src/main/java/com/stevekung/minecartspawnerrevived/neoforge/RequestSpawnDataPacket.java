package com.stevekung.minecartspawnerrevived.neoforge;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record RequestSpawnDataPacket(int entityId) implements CustomPacketPayload
{
    public RequestSpawnDataPacket(FriendlyByteBuf buffer)
    {
        this(buffer.readInt());
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

    public void handle(PlayPayloadContext context)
    {
        context.workHandler().execute(() ->
        {
            var optionalPlayer = context.player();

            if (optionalPlayer.isPresent() && optionalPlayer.get() instanceof ServerPlayer player)
            {
                var spawner = (MinecartSpawner) player.level().getEntity(this.entityId);

                if (spawner != null)
                {
                    var level = spawner.level();
                    sendSpawnDataPacket(player, this.entityId, spawner.getSpawner().getOrCreateNextSpawnData(level, level.getRandom(), spawner.blockPosition()));
                }
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
        player.connection.send(new ClientboundCustomPayloadPacket(new SendSpawnDataPacket(entityId, compound)));
    }
}