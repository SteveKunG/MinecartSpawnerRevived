package com.stevekung.minecartspawnerrevived.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import com.stevekung.minecartspawnerrevived.client.renderer.MinecartSpawnerRenderer;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.MinecartSpawner;

/**
 * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-65065">MC-65065</a></p>
 *
 * <p>Re-added mob renderer for the Spawner Minecart. Mojang removing this for some reason idk.</p>
 */
@Mixin(EntityRenderers.class)
public class MixinEntityRenderers
{
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/entity/EntityRenderers.register(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/client/renderer/entity/EntityRendererProvider;)V"), index = 1, slice = @Slice(from = @At(value = "FIELD", target = "net/minecraft/world/entity/EntityType.SPAWNER_MINECART:Lnet/minecraft/world/entity/EntityType;"), to = @At(value = "FIELD", target = "net/minecraft/world/entity/EntityType.SPECTRAL_ARROW:Lnet/minecraft/world/entity/EntityType;")))
    private static <T extends Entity> EntityRendererProvider<MinecartSpawner> msr$fixSpawnerMinecartRenderer(EntityRendererProvider<T> original)
    {
        return context -> new MinecartSpawnerRenderer(context, ModelLayers.SPAWNER_MINECART);
    }
}