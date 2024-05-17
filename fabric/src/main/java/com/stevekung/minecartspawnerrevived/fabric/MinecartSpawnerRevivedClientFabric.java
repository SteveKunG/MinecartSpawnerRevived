package com.stevekung.minecartspawnerrevived.fabric;

import com.stevekung.minecartspawnerrevived.MinecartSpawnerRevived;
import com.stevekung.minecartspawnerrevived.client.ClientPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class MinecartSpawnerRevivedClientFabric
{
    public static void init()
    {
        ClientPlayNetworking.registerGlobalReceiver(MinecartSpawnerRevived.SEND_SPAWNDATA, MinecartSpawnerRevivedClientFabric::setSpawnerDisplay);
    }

    public static void sendSpawnDataRequest(int entityId)
    {
        var buff = PacketByteBufs.create();
        buff.writeVarInt(entityId);
        ClientPlayNetworking.send(MinecartSpawnerRevived.REQUEST_SPAWNDATA, buff);
    }

    public static void setSpawnerDisplay(Minecraft minecraft, ClientPacketListener listener, FriendlyByteBuf buf, PacketSender responseSender)
    {
        var entityId = buf.readInt();
        var compoundTag = buf.readNbt();
        ClientPacket.setSpawnerDisplay(entityId, compoundTag);
    }
}