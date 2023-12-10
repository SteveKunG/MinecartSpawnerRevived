package com.stevekung.msr.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.SpawnerRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.world.entity.vehicle.MinecartSpawner;

/**
 * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-65065">MC-65065</a></p>
 *
 * <p>Re-added mob renderer for the Spawner Minecart.</p>
 */
public class MinecartSpawnerRenderer extends MinecartRenderer<MinecartSpawner>
{
    private final EntityRenderDispatcher entityRenderer;

    public MinecartSpawnerRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation)
    {
        super(context, modelLayerLocation);
        this.entityRenderer = context.getEntityRenderDispatcher();
    }

    @Override
    public void render(MinecartSpawner minecartSpawner, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
    {
        super.render(minecartSpawner, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.pushPose();
        poseStack.translate(-0.5F, 0.0F, -0.5F);
        var baseSpawner = minecartSpawner.getSpawner();
        var entity = baseSpawner.getOrCreateDisplayEntity(minecartSpawner.level(), minecartSpawner.blockPosition());

        if (entity != null)
        {
            SpawnerRenderer.renderEntityInSpawner(partialTicks, poseStack, bufferSource, packedLight, entity, this.entityRenderer, baseSpawner.getoSpin(), baseSpawner.getSpin());
        }

        poseStack.popPose();
    }
}