package com.stevekung.minecartspawnerrevived;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.vehicle.MinecartSpawner;

public class Platform
{
    @ExpectPlatform
    public static void sendPacketOnInteract(MinecartSpawner entity)
    {
        throw new AssertionError();
    }
}