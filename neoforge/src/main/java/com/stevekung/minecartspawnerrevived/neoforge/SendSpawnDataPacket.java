package com.stevekung.minecartspawnerrevived.neoforge;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import com.stevekung.minecartspawnerrevived.client.ClientPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SendSpawnDataPacket(int entityId, CompoundTag compoundTag) implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<SendSpawnDataPacket> TYPE = new CustomPacketPayload.Type<>(MinecartSpawnerRevived.SEND_SPAWNDATA);
    public static final StreamCodec<FriendlyByteBuf, SendSpawnDataPacket> CODEC = CustomPacketPayload.codec(SendSpawnDataPacket::write, SendSpawnDataPacket::new);

    public SendSpawnDataPacket(FriendlyByteBuf buffer)
    {
        this(buffer.readInt(), buffer.readNbt());
    }

    public void write(FriendlyByteBuf buffer)
    {
        buffer.writeInt(this.entityId);
        buffer.writeNbt(this.compoundTag);
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }

    public void handle(IPayloadContext context)
    {
        context.enqueueWork(() -> ClientPacket.setSpawnerDisplay(this.entityId, this.compoundTag));
    }
}