package com.stevekung.minecartspawnerrevived.client;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;

public class SpawnerMinecartRenderState extends MinecartRenderState
{
    @Nullable
    public EntityRenderState displayEntity;
    public float spin;
    public float scale;
}