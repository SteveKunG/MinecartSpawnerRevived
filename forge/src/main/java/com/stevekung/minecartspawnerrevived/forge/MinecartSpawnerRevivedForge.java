package com.stevekung.minecartspawnerrevived.forge;

import java.util.function.Function;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(MinecartSpawnerRevived.MOD_ID)
public class MinecartSpawnerRevivedForge
{
    private static final String PROTOCOL_VERSION = "1";
    private static int ID = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MinecartSpawnerRevived.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public MinecartSpawnerRevivedForge()
    {
        MinecartSpawnerRevived.init();

        INSTANCE.messageBuilder(RequestSpawnDataPacket.class, nextID()).encoder(RequestSpawnDataPacket::toBytes).decoder(RequestSpawnDataPacket::new).consumerMainThread(RequestSpawnDataPacket::handle).add();
    }

    public static void sendToClient(Object packet, ServerPlayer player)
    {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet)
    {
        INSTANCE.sendToServer(packet);
    }

    private static int nextID()
    {
        return ID++;
    }

    @Mod.EventBusSubscriber(modid = MinecartSpawnerRevived.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            INSTANCE.messageBuilder(SendSpawnDataPacket.class, nextID()).encoder(SendSpawnDataPacket::toBytes).decoder(SendSpawnDataPacket::new).consumerMainThread((sendSpawnDataPacket, contextSupplier) -> ClientModEvents.handleSendSpawnDataPacket(sendSpawnDataPacket.getEntityId(), sendSpawnDataPacket.getCompoundTag())).add();
        }

        private static void handleSendSpawnDataPacket(int entityId, CompoundTag compoundTag)
        {
            var minecraft = Minecraft.getInstance();
            var level = minecraft.level;

            minecraft.execute(() ->
            {
                if (level != null)
                {
                    var spawner = (MinecartSpawner) level.getEntity(entityId);

                    if (spawner == null || compoundTag == null)
                    {
                        return;
                    }

                    var spawnData = SpawnData.CODEC.parse(NbtOps.INSTANCE, compoundTag.getCompound(BaseSpawner.SPAWN_DATA_TAG)).resultOrPartial(string -> MinecartSpawnerRevived.LOGGER.warn("Invalid SpawnData: {}", string)).orElseGet(SpawnData::new);
                    spawner.getSpawner().displayEntity = EntityType.loadEntityRecursive(spawnData.entityToSpawn(), level, Function.identity());
                }
            });
        }
    }
}