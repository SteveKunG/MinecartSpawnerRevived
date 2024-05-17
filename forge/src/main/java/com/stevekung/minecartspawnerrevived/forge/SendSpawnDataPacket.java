package com.stevekung.minecartspawnerrevived.forge;

import java.util.function.Supplier;

import com.stevekung.minecartspawnerrevived.client.ClientPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

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

    public void handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> ClientPacket.setSpawnerDisplay(this.entityId, this.compoundTag));
    }
}