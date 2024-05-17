package com.stevekung.minecartspawnerrevived.neoforge;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import com.stevekung.minecartspawnerrevived.client.ClientPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SendSpawnDataPacket(int entityId, CompoundTag compoundTag) implements CustomPacketPayload
{
    public SendSpawnDataPacket(FriendlyByteBuf buffer)
    {
        this(buffer.readInt(), buffer.readNbt());
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

    public void handle(PlayPayloadContext context)
    {
        context.workHandler().execute(() -> ClientPacket.setSpawnerDisplay(this.entityId, this.compoundTag));
    }
}