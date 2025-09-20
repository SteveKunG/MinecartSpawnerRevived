package com.stevekung.minecartspawnerrevived.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.minecartspawnerrevived.client.SpawnerMinecartRenderState;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.SpawnerRenderer;
import net.minecraft.client.renderer.entity.AbstractMinecartRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.MinecartSpawner;

/**
 * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-65065">MC-65065</a></p>
 *
 * <p>Re-added mob renderer for the Spawner Minecart.</p>
 */
public class MinecartSpawnerRenderer extends AbstractMinecartRenderer<MinecartSpawner, SpawnerMinecartRenderState>
{
    private final EntityRenderDispatcher entityRenderer;

    public MinecartSpawnerRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation)
    {
        super(context, modelLayerLocation);
        this.entityRenderer = context.getEntityRenderDispatcher();
    }

    @Override
    public void submit(SpawnerMinecartRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState)
    {
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
        poseStack.pushPose();
        poseStack.translate(-0.5F, 0.0F, -0.5F);
        var entityRenderState = renderState.displayEntity;

        if (entityRenderState != null)
        {
            SpawnerRenderer.submitEntityInSpawner(poseStack, submitNodeCollector, entityRenderState, this.entityRenderer, renderState.spin, renderState.scale, cameraRenderState);
        }

        poseStack.popPose();
    }

    @Override
    public SpawnerMinecartRenderState createRenderState()
    {
        return new SpawnerMinecartRenderState();
    }

    @Override
    public void extractRenderState(MinecartSpawner spawner, SpawnerMinecartRenderState renderState, float partialTicks)
    {
        super.extractRenderState(spawner, renderState, partialTicks);
        var baseSpawner = spawner.getSpawner();
        var entity = baseSpawner.getOrCreateDisplayEntity(spawner.level(), spawner.blockPosition());

        if (entity != null)
        {
            renderState.displayEntity = this.entityRenderer.extractEntity(baseSpawner.getOrCreateDisplayEntity(spawner.level(), spawner.blockPosition()), partialTicks);
            renderState.spin = (float) Mth.lerp(partialTicks, baseSpawner.getOSpin(), baseSpawner.getSpin()) * 10.0F;
            renderState.scale = 0.53125F;
        }
    }
}