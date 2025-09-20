package com.stevekung.minecartspawnerrevived.client;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.world.entity.Entity;

public class SpawnerMinecartRenderState extends MinecartRenderState
{
    @Nullable
    public EntityRenderState displayEntity;
    public float spin;
    public float scale;
}