package com.stevekung.minecartspawnerrevived;

import net.minecraft.world.entity.vehicle.minecart.MinecartSpawner;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class Platform
{
    @ExpectPlatform
    public static void sendPacketOnInteract(MinecartSpawner entity)
    {
        throw new AssertionError();
    }
}