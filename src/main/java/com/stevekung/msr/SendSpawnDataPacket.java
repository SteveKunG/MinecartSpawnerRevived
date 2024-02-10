package com.stevekung.msr;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SendSpawnDataPacket(int entityId, CompoundTag spawnDataTag) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<SendSpawnDataPacket> TYPE = new CustomPacketPayload.Type<>(MinecartSpawnerRevived.SEND_SPAWNDATA);
    public static final StreamCodec<FriendlyByteBuf, SendSpawnDataPacket> CODEC = CustomPacketPayload.codec(SendSpawnDataPacket::write, SendSpawnDataPacket::new);

    private SendSpawnDataPacket(FriendlyByteBuf buf)
    {
        this(buf.readInt(), buf.readNbt());
    }

    private void write(FriendlyByteBuf buf)
    {
        buf.writeInt(this.entityId);
        buf.writeNbt(this.spawnDataTag);
    }

    @Override
    public CustomPacketPayload.Type<SendSpawnDataPacket> type()
    {
        return TYPE;
    }
}