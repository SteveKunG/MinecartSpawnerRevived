package com.stevekung.msr;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RequestSpawnDataPacket(int entityId) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<RequestSpawnDataPacket> TYPE = new CustomPacketPayload.Type<>(MinecartSpawnerRevived.REQUEST_SPAWNDATA);
    public static final StreamCodec<FriendlyByteBuf, RequestSpawnDataPacket> CODEC = CustomPacketPayload.codec(RequestSpawnDataPacket::write, RequestSpawnDataPacket::new);

    private RequestSpawnDataPacket(FriendlyByteBuf buf)
    {
        this(buf.readInt());
    }

    private void write(FriendlyByteBuf buf)
    {
        buf.writeInt(this.entityId);
    }

    @Override
    public CustomPacketPayload.Type<RequestSpawnDataPacket> type()
    {
        return TYPE;
    }
}