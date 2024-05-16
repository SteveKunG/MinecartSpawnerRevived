package com.stevekung.minecartspawnerrevived.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.stevekung.minecartspawnerrevived.client.PlatformClient;
import com.stevekung.minecartspawnerrevived.client.renderer.SpawnerClientTicker;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;

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
        PlatformClient.sendSpawnDataRequestOnLoad(this.getId());
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
        PlatformClient.sendSpawnDataRequestOnLoad(this.getId());
    }

    /**
     * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-66894">MC-66894</a></p>
     *
     * <p>Fix Spawner Minecart particles position.</p>
     */
    @Redirect(method = "method_31554", at = @At(value = "INVOKE", target = "net/minecraft/world/level/BaseSpawner.clientTick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void msr$createClientTicker(BaseSpawner spawner, Level level, BlockPos pos)
    {
        ((SpawnerClientTicker) spawner).msr$clientTick(level, MinecartSpawner.class.cast(this));
    }
}