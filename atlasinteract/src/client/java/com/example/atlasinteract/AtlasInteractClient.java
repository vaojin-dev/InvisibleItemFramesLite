package com.vaojin.atlasinteract;

import com.vaojin.atlasinteract.network.EmptyClickPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class AtlasInteractClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AtlasInteract.LOGGER.info("[AtlasInteract] Client Initialized.");
        
        PayloadTypeRegistry.serverboundPlay().register(EmptyClickPayload.TYPE, EmptyClickPayload.CODEC);
    }
}