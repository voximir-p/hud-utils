package org.voximir.hud_utils.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.voximir.hud_utils.HudUtils;
import org.voximir.hud_utils.config.SettingsGuiFactory;
import org.voximir.hud_utils.hud.HudDisplay;
import org.voximir.hud_utils.utilities.TranslationKey;

public final class InputHandler {
    private static final TranslationKey KEY_TRANSLATION_PREFIX = TranslationKey.of("key", HudUtils.MOD_ID);

    public static KeyMapping TOGGLE_KEY;
    public static KeyMapping SETTINGS_KEY;

    private static boolean wasToggleKeyDown = false;

    private InputHandler() {
    }

    public static void registerKeys() {
        TOGGLE_KEY = registerKey("toggle", GLFW.GLFW_KEY_H, KeyCategories.HUD_UTILS_CATEGORY);
        SETTINGS_KEY = registerKey("settings", GLFW.GLFW_KEY_F12, KeyCategories.HUD_UTILS_CATEGORY);
    }

    public static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(InputHandler::handleToggleKey);
        ClientTickEvents.END_CLIENT_TICK.register(InputHandler::handleSettingsKey);
    }

    private static void handleToggleKey(Minecraft client) {
        boolean isToggleKeyDown = TOGGLE_KEY.isDown();

        if (client.player == null || isToggleKeyDown == wasToggleKeyDown) {
            return;
        }

        wasToggleKeyDown = isToggleKeyDown;

        if (isToggleKeyDown) {
            HudDisplay.handleToggleKey();
        }
    }

    private static void handleSettingsKey(Minecraft client) {
        while (SETTINGS_KEY.consumeClick()) {
            client.setScreenAndShow(SettingsGuiFactory.createSettingsGui(client.gui.screen()));
        }
    }

    private static KeyMapping registerKey(String id, int code, KeyMapping.Category category) {
        return KeyMappingHelper.registerKeyMapping(
                new KeyMapping(KEY_TRANSLATION_PREFIX.dot(id).asString(), InputConstants.Type.KEYSYM, code, category)
        );
    }
}