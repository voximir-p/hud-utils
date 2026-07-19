package org.voximir.hud_utils.input;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.voximir.hud_utils.HudUtils;

public final class KeyCategories {
    public static KeyMapping.Category HUD_UTILS_CATEGORY;

    private KeyCategories() {
    }

    public static void registerCategories() {
        HUD_UTILS_CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(HudUtils.MOD_ID, HudUtils.MOD_ID));
    }
}