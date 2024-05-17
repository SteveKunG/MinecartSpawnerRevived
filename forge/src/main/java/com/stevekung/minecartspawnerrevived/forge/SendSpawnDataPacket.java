package com.stevekung.minecartspawnerrevived.forge;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import com.stevekung.minecartspawnerrevived.client.ClientPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
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

    @Override
    public void write(FriendlyByteBuf buffer)
    {
        buffer.writeInt(this.entityId);
        buffer.writeNbt(this.compoundTag);
    }

    @Override
    public ResourceLocation id()
    {
        return MinecartSpawnerRevived.SEND_SPAWNDATA;
    }

    public void handle(CustomPayloadEvent.Context context)
    {
        context.enqueueWork(() -> ClientPacket.setSpawnerDisplay(this.entityId, this.compoundTag));
    }
}