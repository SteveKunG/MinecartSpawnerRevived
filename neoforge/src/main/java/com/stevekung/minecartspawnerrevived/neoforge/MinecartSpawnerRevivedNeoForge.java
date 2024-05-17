package com.stevekung.minecartspawnerrevived.neoforge;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;

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
    public void register(RegisterPayloadHandlerEvent event)
    {
        var registrar = event.registrar(MinecartSpawnerRevived.MOD_ID).versioned(PROTOCOL_VERSION).optional();
        registrar.play(MinecartSpawnerRevived.REQUEST_SPAWNDATA, RequestSpawnDataPacket::new, handler -> handler.server(RequestSpawnDataPacket::handle));
        registrar.play(MinecartSpawnerRevived.SEND_SPAWNDATA, SendSpawnDataPacket::new, handler -> handler.client(SendSpawnDataPacket::handle));
    }
}