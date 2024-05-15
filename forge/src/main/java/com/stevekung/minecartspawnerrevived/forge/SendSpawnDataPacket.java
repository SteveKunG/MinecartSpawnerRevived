package com.stevekung.minecartspawnerrevived.forge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class SendSpawnDataPacket
{
    private final int entityId;
    private final CompoundTag compoundTag;

    public SendSpawnDataPacket(int entityId, CompoundTag compoundTag)
    {
        this.entityId = entityId;
        this.compoundTag = compoundTag;
    }

    public SendSpawnDataPacket(FriendlyByteBuf buf)
    {
        this.entityId = buf.readInt();
        this.compoundTag = buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeInt(this.entityId);
        buf.writeNbt(this.compoundTag);
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public CompoundTag getCompoundTag()
    {
        return this.compoundTag;
    }
}