package com.stevekung.minecartspawnerrevived.neoforge;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RequestSpawnDataPacket(int entityId) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<RequestSpawnDataPacket> TYPE = new CustomPacketPayload.Type<>(MinecartSpawnerRevived.REQUEST_SPAWNDATA);
    public static final StreamCodec<FriendlyByteBuf, RequestSpawnDataPacket> CODEC = CustomPacketPayload.codec(RequestSpawnDataPacket::write, RequestSpawnDataPacket::new);

    public RequestSpawnDataPacket(FriendlyByteBuf buffer)
    {
        this(buffer.readInt());
    }

    public void write(FriendlyByteBuf buffer)
    {
        buffer.writeInt(this.entityId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle(IPayloadContext context)
    {
        context.enqueueWork(() ->
        {
            if (context.player() instanceof ServerPlayer player)
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