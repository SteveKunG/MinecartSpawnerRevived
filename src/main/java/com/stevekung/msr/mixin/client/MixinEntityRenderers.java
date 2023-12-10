package com.stevekung.msr.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.stevekung.msr.client.renderer.MinecartSpawnerRenderer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.MinecartRenderer;

/**
 * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-65065">MC-65065</a></p>
 *
 * <p>Re-added mob renderer for the Spawner Minecart. Mojang removing this for some reason idk.</p>
 */
@Mixin(EntityRenderers.class)
public class MixinEntityRenderers
{
    @Redirect(method = "method_32181", at = @At(value = "NEW", target = "net/minecraft/client/renderer/entity/MinecartRenderer"))
    private static MinecartRenderer<?> msr$fixSpawnerMinecartRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation)
    {
        return new MinecartSpawnerRenderer(context, modelLayerLocation);
    }
}