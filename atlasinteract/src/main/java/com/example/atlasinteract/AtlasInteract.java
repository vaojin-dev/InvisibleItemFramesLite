package com.vaojin.atlasinteract;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AtlasInteract implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("atlasinteract");

    @Override
    public void onInitialize() {
        LOGGER.info("AtlasInteract mod loaded!");
    }
}