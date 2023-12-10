package com.stevekung.msr.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.util.Mth;
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
        var baseSpawner = minecartSpawner.getSpawner();
        var entity = baseSpawner.getOrCreateDisplayEntity(minecartSpawner.level(), minecartSpawner.level().getRandom(), minecartSpawner.blockPosition());

        if (entity != null)
        {
            var f = 0.53125F;
            var g = Math.max(entity.getBbWidth(), entity.getBbHeight());

            if (g > 1.0)
            {
                f /= g;
            }

            poseStack.translate(0.0F, 0.4F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees((float)(Mth.lerp(partialTicks, baseSpawner.getoSpin(), baseSpawner.getSpin()) * 10.0F)));
            poseStack.translate(0.0F, -0.2F, 0.0F);
            poseStack.mulPose(Axis.XP.rotationDegrees(-30.0F));
            poseStack.scale(f, f, f);
            this.entityRenderer.render(entity, 0.0, 0.0, 0.0, 0.0F, partialTicks, poseStack, bufferSource, packedLight);
        }

        poseStack.popPose();
    }
}