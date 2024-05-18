package com.stevekung.minecartspawnerrevived.neoforge;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import com.stevekung.minecartspawnerrevived.RequestSpawnDataPacket;
import com.stevekung.minecartspawnerrevived.SendSpawnDataPacket;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(MinecartSpawnerRevived.MOD_ID)
public class MinecartSpawnerRevivedNeoForge
{
    private static final String PROTOCOL_VERSION = "1";

    public MinecartSpawnerRevivedNeoForge(IEventBus modEventBus)
    {
        MinecartSpawnerRevived.init();
        modEventBus.addListener(this::register);
    }

    @SubscribeEvent
    public void register(RegisterPayloadHandlersEvent event)
    {
        var registrar = event.registrar(MinecartSpawnerRevived.MOD_ID).versioned(PROTOCOL_VERSION).optional();
        registrar.playToServer(RequestSpawnDataPacket.TYPE, RequestSpawnDataPacket.CODEC, RequestSpawnDataPacketNeoForge::handle);
        registrar.playToClient(SendSpawnDataPacket.TYPE, SendSpawnDataPacket.CODEC, SendSpawnDataPacketNeoForge::handle);
    }
}