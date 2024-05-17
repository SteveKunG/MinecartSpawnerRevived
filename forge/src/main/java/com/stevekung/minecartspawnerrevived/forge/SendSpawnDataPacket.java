package com.stevekung.minecartspawnerrevived.forge;

import com.stevekung.minecartspawnerrevived.client.ClientPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class SendSpawnDataPacket implements CustomPacketPayload
{
    private final int entityId;
    private final CompoundTag compoundTag;

    public SendSpawnDataPacket(int entityId, CompoundTag compoundTag)
    {
        this.entityId = entityId;
        this.compoundTag = compoundTag;
    }

    public SendSpawnDataPacket(FriendlyByteBuf buffer)
    {
        this.entityId = buffer.readInt();
        this.compoundTag = buffer.readNbt();
    }

    public void write(FriendlyByteBuf buffer)
    {
        buffer.writeInt(this.entityId);
        buffer.writeNbt(this.compoundTag);
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return com.stevekung.minecartspawnerrevived.SendSpawnDataPacket.TYPE;
    }

    public void handle(CustomPayloadEvent.Context context)
    {
        context.enqueueWork(() -> ClientPacket.setSpawnerDisplay(this.entityId, this.compoundTag));
    }
}