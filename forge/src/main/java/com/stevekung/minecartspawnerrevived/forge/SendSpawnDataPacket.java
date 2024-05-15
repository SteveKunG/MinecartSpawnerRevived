package com.stevekung.minecartspawnerrevived.forge;

import java.util.function.Function;
import java.util.function.Supplier;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;
import net.minecraftforge.network.NetworkEvent;

public class SendSpawnDataPacket
{
    private final int entityId;
    private final CompoundTag compoundTag;

    public SendSpawnDataPacket(int entityId, CompoundTag compoundTag)
    {
        this.entityId = entityId;
        this.compoundTag = compoundTag;
    }

    public SendSpawnDataPacket(FriendlyByteBuf buf)
    {
        entityId = buf.readInt();
        compoundTag = buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeInt(entityId);
        buf.writeNbt(compoundTag);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            var level = ctx.get().getSender().level();
            var spawner = (MinecartSpawner) level.getEntity(entityId);

            if (spawner == null || compoundTag == null)
            {
                return;
            }

            var spawnData = SpawnData.CODEC.parse(NbtOps.INSTANCE, compoundTag.getCompound(BaseSpawner.SPAWN_DATA_TAG)).resultOrPartial(string -> MinecartSpawnerRevived.LOGGER.warn("Invalid SpawnData: {}", string)).orElseGet(SpawnData::new);
            spawner.getSpawner().displayEntity = EntityType.loadEntityRecursive(spawnData.entityToSpawn(), level, Function.identity());
        });
    }
}