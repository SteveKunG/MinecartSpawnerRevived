package com.stevekung.msr.client.renderer;

import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.Level;

public interface SpawnerClientTicker
{
    void msr$clientTick(Level level, MinecartSpawner spawner);
}