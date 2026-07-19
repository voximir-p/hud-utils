package org.voximir.hud_utils.config;

import com.mojang.serialization.Codec;
import dev.isxander.yacl3.config.v3.ConfigEntry;
import dev.isxander.yacl3.config.v3.JsonFileCodecConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.voximir.hud_utils.HudUtils;

public class HudUtilsSettings extends JsonFileCodecConfig<HudUtilsSettings> {
    private static final HudUtilsSettings INSTANCE = new HudUtilsSettings();

    private static final int SCHEMA_VERSION = 1;
    private final ConfigEntry<Integer> schemaVersion = register("schema_version", SCHEMA_VERSION, Codec.INT);

    static {
        if (!INSTANCE.loadFromFile()) {
            INSTANCE.saveToFile();
        } else {
            // noinspection StatementWithEmptyBody
            if (INSTANCE.schemaVersion.get() > 1) {
                // Handle migration; not used yet
            }
        }
    }

    final General general = new General();

    class General {
        private final ConfigEntry<Boolean> showHud = register(
                "show_hud",
                true,
                Codec.BOOL
        );

        final Displays displays = new Displays();

        class Displays {
            private final ConfigEntry<Boolean> displayCoordinates = register(
                    "display_coordinates",
                    true,
                    Codec.BOOL
            );

            private final ConfigEntry<Boolean> displayDays = register(
                    "display_days",
                    true,
                    Codec.BOOL
            );

            private final ConfigEntry<Boolean> displayTime = register(
                    "display_time",
                    true,
                    Codec.BOOL
            );

            private final ConfigEntry<Boolean> use24Hour = register(
                    "use_24_hour",
                    false,
                    Codec.BOOL
            );
        }
    }

    public HudUtilsSettings() {
        super(FabricLoader.getInstance().getConfigDir().resolve(HudUtils.MOD_ID + ".json"));
    }

    public static HudUtilsSettings getInstance() {
        return INSTANCE;
    }

    public static ConfigEntry<Boolean> getShowHud() {
        return INSTANCE.general.showHud;
    }

    public static ConfigEntry<Boolean> getDisplayCoordinates() {
        return INSTANCE.general.displays.displayCoordinates;
    }

    public static ConfigEntry<Boolean> getDisplayDays() {
        return INSTANCE.general.displays.displayDays;
    }

    public static ConfigEntry<Boolean> getDisplayTime() {
        return INSTANCE.general.displays.displayTime;
    }

    public static ConfigEntry<Boolean> getUse24Hour() {
        return INSTANCE.general.displays.use24Hour;
    }
}