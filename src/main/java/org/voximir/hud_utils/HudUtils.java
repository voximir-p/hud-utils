package org.voximir.hud_utils;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.voximir.hud_utils.hud.HudDisplay;
import org.voximir.hud_utils.input.InputHandler;
import org.voximir.hud_utils.input.KeyCategories;

public class HudUtils implements ClientModInitializer {
    public static final String MOD_ID = "hud_utils";
    public static final Logger LOGGER = LoggerFactory.getLogger(HudUtils.class);

    @Override
    public void onInitializeClient() {
        HudDisplay.registerHudDisplay();

        KeyCategories.registerCategories();
        InputHandler.registerKeys();
        InputHandler.registerEvents();

        LOGGER.info("Initialized");
    }
}