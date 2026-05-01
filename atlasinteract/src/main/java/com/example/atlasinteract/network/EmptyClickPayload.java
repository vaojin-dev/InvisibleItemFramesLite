package com.vaojin.atlasinteract.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record EmptyClickPayload() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<EmptyClickPayload> TYPE = 
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("atlas", "empty_click"));
            
    public static final StreamCodec<FriendlyByteBuf, EmptyClickPayload> CODEC = 
            StreamCodec.unit(new EmptyClickPayload());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}