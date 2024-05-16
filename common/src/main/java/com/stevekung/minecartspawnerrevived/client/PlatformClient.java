package com.stevekung.minecartspawnerrevived.client;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class PlatformClient
{
    @ExpectPlatform
    public static void sendSpawnDataRequestOnLoad(int entityId)
    {
        throw new AssertionError();
    }
}