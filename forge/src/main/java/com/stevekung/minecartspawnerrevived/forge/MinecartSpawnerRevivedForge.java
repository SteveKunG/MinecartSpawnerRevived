package com.stevekung.minecartspawnerrevived.forge;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(MinecartSpawnerRevived.MOD_ID)
public class MinecartSpawnerRevivedForge
{
    private static final String PROTOCOL_VERSION = "1";
    private static int ID = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MinecartSpawnerRevived.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public MinecartSpawnerRevivedForge(FMLJavaModLoadingContext context)
    {
        MinecartSpawnerRevived.init();
        var modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        INSTANCE.messageBuilder(RequestSpawnDataPacket.class, nextID()).encoder(RequestSpawnDataPacket::toBytes).decoder(RequestSpawnDataPacket::new).consumerMainThread(RequestSpawnDataPacket::handle).add();
        INSTANCE.messageBuilder(SendSpawnDataPacket.class, nextID()).encoder(SendSpawnDataPacket::toBytes).decoder(SendSpawnDataPacket::new).consumerMainThread(SendSpawnDataPacket::handle).add();
    }

    public static void sendToClient(Object packet, ServerPlayer player)
    {
        var connection = player.connection.connection;

        if (INSTANCE.isRemotePresent(connection))
        {
            INSTANCE.sendTo(packet, connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public static void sendToServer(Object packet)
    {
        INSTANCE.sendToServer(packet);
    }

    private static int nextID()
    {
        return ID++;
    }
}