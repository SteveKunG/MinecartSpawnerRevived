package com.stevekung.msr.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.stevekung.msr.client.renderer.SpawnerClientTicker;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;

@Mixin(BaseSpawner.class)
public abstract class MixinBaseSpawner implements SpawnerClientTicker
{
    @Shadow
    Entity displayEntity;

    @Shadow
    int spawnDelay;

    @Shadow
    double spin;

    @Shadow
    double oSpin;

    @Shadow
    abstract boolean isNearPlayer(Level level, BlockPos pos);

    /**
     * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-66894">MC-66894</a></p>
     *
     * <p>Fix Spawner Minecart particles position.</p>
     */
    @Override
    public void msr$clientTick(Level level, MinecartSpawner spawner)
    {
        if (!this.isNearPlayer(level, spawner.blockPosition()))
        {
            this.oSpin = this.spin;
        }
        else if (this.displayEntity != null)
        {
            var randomSource = level.getRandom();
            var x = spawner.getX() - 0.5d + randomSource.nextDouble();
            var y = spawner.getY() + randomSource.nextDouble();
            var z = spawner.getZ() - 0.5d + randomSource.nextDouble();
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.0, 0.0);

            if (this.spawnDelay > 0)
            {
                --this.spawnDelay;
            }

            this.oSpin = this.spin;
            this.spin = (this.spin + 1000.0F / (this.spawnDelay + 200.0F)) % 360.0;
        }
    }
}