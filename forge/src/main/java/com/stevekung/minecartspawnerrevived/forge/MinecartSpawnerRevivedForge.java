package com.stevekung.minecartspawnerrevived.forge;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import com.stevekung.minecartspawnerrevived.RequestSpawnDataPacket;
import com.stevekung.minecartspawnerrevived.SendSpawnDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

@Mod(MinecartSpawnerRevived.MOD_ID)
public class MinecartSpawnerRevivedForge
{
    private static final int PROTOCOL_VERSION = 1;
    private static int ID = 0;
    public static final SimpleChannel INSTANCE = ChannelBuilder.named(new ResourceLocation(MinecartSpawnerRevived.MOD_ID, "main")).clientAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION)).serverAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION)).networkProtocolVersion(PROTOCOL_VERSION).simpleChannel();

    public MinecartSpawnerRevivedForge()
    {
        MinecartSpawnerRevived.init();
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        INSTANCE.messageBuilder(RequestSpawnDataPacket.class, nextID()).encoder(RequestSpawnDataPacket::write).decoder(RequestSpawnDataPacket::new).consumerMainThread(RequestSpawnDataPacketForge::handle).add();
        INSTANCE.messageBuilder(SendSpawnDataPacket.class, nextID()).encoder(SendSpawnDataPacket::write).decoder(SendSpawnDataPacket::new).consumerMainThread(SendSpawnDataPacketForge::handle).add();
    }

    public static void sendToClient(Object packet, ServerPlayer player)
    {
        INSTANCE.send(packet, PacketDistributor.PLAYER.with(player));
    }

    public static void sendToServer(Object packet)
    {
        INSTANCE.send(packet, PacketDistributor.SERVER.noArg());
    }

    private static int nextID()
    {
        return ID++;
    }
}