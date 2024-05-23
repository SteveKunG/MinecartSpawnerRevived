package com.stevekung.minecartspawnerrevived;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;

public class MinecartSpawnerRevived
{
    public static final String MOD_ID = "minecartspawnerrevived";
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Use to request SpawnData from the client side. Then sent the SpawnData into the server.
     */
    public static final ResourceLocation REQUEST_SPAWNDATA = ResourceLocation.fromNamespaceAndPath(MOD_ID, "request_spawndata");

    /**
     * Use to send SpawnData to the client side to set spawner display.
     */
    public static final ResourceLocation SEND_SPAWNDATA = ResourceLocation.fromNamespaceAndPath(MOD_ID, "send_spawndata");

    public static void init()
    {
        LOGGER.info("MinecartSpawnerRevived loaded, #PleaseAddSpawnerMinecartItem!");
    }
}