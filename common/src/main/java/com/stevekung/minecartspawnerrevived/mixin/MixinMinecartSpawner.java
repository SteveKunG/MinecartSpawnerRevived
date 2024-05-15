package com.stevekung.minecartspawnerrevived.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ExperienceOrb;
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
     * <p>Add experience drop when Spawner Minecart is destroyed. Same as regular spawner block.</p>
     */
    @Override
    public void destroy(DamageSource damageSource)
    {
        super.destroy(damageSource);

        if (!this.level().isClientSide())
        {
            var i = 20 + this.random.nextInt(20) + this.random.nextInt(20);
            ExperienceOrb.award((ServerLevel)this.level(), this.position(), i);
        }
    }
}