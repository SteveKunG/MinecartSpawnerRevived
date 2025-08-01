package com.stevekung.minecartspawnerrevived.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.minecartspawnerrevived.client.SpawnerMinecartRenderState;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.SpawnerRenderer;
import net.minecraft.client.renderer.entity.AbstractMinecartRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
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
    public void submit(SpawnerMinecartRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector)
    {
        super.submit(renderState, poseStack, submitNodeCollector);
        poseStack.pushPose();
        poseStack.translate(-0.5F, 0.0F, -0.5F);
        var entity = renderState.displayEntity;

        if (entity != null)
        {
            SpawnerRenderer.renderEntityInSpawner(renderState.ageInTicks, poseStack, null, entity, this.entityRenderer, renderState.oSpin, renderState.spin);
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
        renderState.displayEntity = baseSpawner.getOrCreateDisplayEntity(spawner.level(), spawner.blockPosition());
        renderState.oSpin = baseSpawner.getoSpin();
        renderState.spin = baseSpawner.getSpin();
    }
}