package com.stevekung.msr.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.stevekung.msr.MinecartSpawnerRevived;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.gameevent.GameEvent;

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

    /**
     * <p>Fix for <a href="https://bugs.mojang.com/browse/MC-110427">MC-110427</a></p>
     *
     * <p>Fix Spawn Eggs cannot be used on Spawner Minecart to change entity to spawn.</p>
     */
    @Override
    public InteractionResult interact(Player player, InteractionHand hand)
    {
        var thisEntity = MinecartSpawner.class.cast(this);
        var itemStack = player.getItemInHand(hand);

        if (itemStack.getItem() instanceof SpawnEggItem spawnEgg)
        {
            var entityType = spawnEgg.getType(itemStack.getTag());
            thisEntity.getSpawner().setEntityId(entityType, this.level(), this.level().getRandom(), this.blockPosition());
            this.level().gameEvent(player, GameEvent.ENTITY_INTERACT, this.blockPosition());
            itemStack.shrink(1);

            if (this.level() instanceof ServerLevel)
            {
                for (var serverPlayer : PlayerLookup.tracking(this))
                {
                    MinecartSpawnerRevived.sendSpawnDataPacket(serverPlayer, this.getId(), thisEntity.getSpawner().getOrCreateNextSpawnData(this.level(), this.level().getRandom(), this.blockPosition()));
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        return super.interact(player, hand);
    }
}