package com.stevekung.minecartspawnerrevived.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.stevekung.minecartspawnerrevived.client.renderer.SpawnerClientTicker;
import net.minecraft.core.BlockPos;
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
     * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-66894">MC-66894</a></p>
     *
     * <p>Fix Spawner Minecart particles position.</p>
     */
    @Redirect(method = "method_31554", at = @At(value = "INVOKE", target = "net/minecraft/world/level/BaseSpawner.clientTick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void msr$createClientTicker(BaseSpawner spawner, Level level, BlockPos pos)
    {
        ((SpawnerClientTicker)spawner).msr$clientTick(level, MinecartSpawner.class.cast(this));
    }
}