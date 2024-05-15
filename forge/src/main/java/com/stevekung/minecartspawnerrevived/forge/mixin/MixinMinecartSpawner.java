package com.stevekung.minecartspawnerrevived.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import com.stevekung.minecartspawnerrevived.forge.RequestSpawnDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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

            if (this.level() instanceof ServerLevel serverLevel)
            {
                for (var serverPlayer : serverLevel.getPlayers(serverPlayer -> true))//TODO Only get nearest player
                {
                    RequestSpawnDataPacket.sendSpawnDataPacket(serverPlayer, this.getId(), thisEntity.getSpawner().getOrCreateNextSpawnData(this.level(), this.level().getRandom(), this.blockPosition()));
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        return super.interact(player, hand);
    }
}