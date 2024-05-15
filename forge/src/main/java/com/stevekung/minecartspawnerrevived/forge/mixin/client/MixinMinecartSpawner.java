package com.stevekung.minecartspawnerrevived.forge.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.stevekung.minecartspawnerrevived.forge.MinecartSpawnerRevivedForge;
import com.stevekung.minecartspawnerrevived.forge.RequestSpawnDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartSpawner;

@Mixin(MinecartSpawner.class)
public abstract class MixinMinecartSpawner extends AbstractMinecart
{
    MixinMinecartSpawner()
    {
        super(null, null);
    }

    /**
     * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-65065">MC-65065</a></p>
     *
     * <p>Re-send a request SpawnData packet to the server when modifying spawner minecart data.</p>
     */
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void msr$resendSpawnDataRequestOnLoad(CompoundTag compound, CallbackInfo info)
    {
        if (Minecraft.getInstance().getConnection() != null)
        {
            MinecartSpawnerRevivedForge.sendToServer(new RequestSpawnDataPacket(this.getId()));
        }
    }

    /**
     * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-65065">MC-65065</a></p>
     *
     * <p>When entity recreated from a packet, send a request SpawnData packet to the server.</p>
     */
    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet)
    {
        super.recreateFromPacket(packet);
        MinecartSpawnerRevivedForge.sendToServer(new RequestSpawnDataPacket(this.getId()));
    }
}