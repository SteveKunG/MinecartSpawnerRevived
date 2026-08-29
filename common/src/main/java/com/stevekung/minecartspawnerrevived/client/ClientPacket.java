package com.stevekung.minecartspawnerrevived.client;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;

public class ClientPacket
{
    public static void setSpawnerDisplay(int entityId, CompoundTag compoundTag)
    {
        var minecraft = Minecraft.getInstance();
        var level = minecraft.level;

        minecraft.execute(() ->
        {
            if (level == null || !(level.getEntity(entityId) instanceof MinecartSpawner spawner))
            {
                return;
            }

            var spawnData = SpawnData.CODEC.parse(NbtOps.INSTANCE, compoundTag.getCompound(BaseSpawner.SPAWN_DATA_TAG).orElseThrow()).resultOrPartial(string -> MinecartSpawnerRevived.LOGGER.warn("Invalid SpawnData: {}", string)).orElseGet(SpawnData::new);
            spawner.getSpawner().displayEntity = EntityType.loadEntityRecursive(spawnData.entityToSpawn(), level, new EntitySpawnRequest(EntitySpawnReason.SPAWNER, true), BaseSpawner.SET_DISPLAY_ENTITY_ID);
        });
    }
}