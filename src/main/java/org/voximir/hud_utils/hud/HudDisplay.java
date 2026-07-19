package org.voximir.hud_utils.hud;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.voximir.hud_utils.config.HudUtilsSettings;

public class HudDisplay {
    private static boolean hudVisible = false;

    public static void registerHudDisplay() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            var player = client.player;

            if (player == null) {
                return;
            }

            if (!HudUtilsSettings.getShowHud().get()) {
                if (hudVisible) {
                    player.sendOverlayMessage(Component.literal(""));
                    hudVisible = false;
                }
                return;
            }

            if (!hudVisible) {
                hudVisible = true;
            }

            player.sendOverlayMessage(Component.literal(buildHudMessage(player)));
        });
    }

    public static void handleToggleKey() {
        var showHudSetting = HudUtilsSettings.getShowHud();
        showHudSetting.set(!showHudSetting.get());
    }

    private static String buildHudMessage(Player player) {
        long worldTime = player.level().getOverworldClockTime();
        boolean showCoordinates = HudUtilsSettings.getDisplayCoordinates().get();
        boolean showDays = HudUtilsSettings.getDisplayDays().get();
        boolean showTime = HudUtilsSettings.getDisplayTime().get();

        var message = new StringBuilder();

        if (showCoordinates) {
            message.append(String.format("§6XYZ§r %d %d %d", (long) player.getX(), (long) player.getY(), (long) player.getZ()));
        }

        if (showDays || showTime) {
            message.append(" §6|§r ");
        }

        if (showDays) {
            message.append(String.format("DAY %d", getCurrentDay(worldTime)));
        }

        if (showDays && showTime) {
            message.append(" §6[§r");
        }

        if (showTime) {
            message.append(formatTime(worldTime, HudUtilsSettings.getUse24Hour().get()));
        }

        if (showDays && showTime) {
            message.append("§6]§r");
        }

        return message.toString();
    }

    private static long getCurrentDay(long worldTime) {
        return worldTime / 24000 + 1;
    }

    private static String formatTime(long time, boolean use24Hour) {
        long ticks = time % 24000;
        long totalMinutes = (long) Math.floor(ticks * 0.06);
        long hour = ((totalMinutes / 60) + 6) % 24;
        long minutes = totalMinutes % 60;

        if (use24Hour) {
            return String.format("%02d:%02d", hour, minutes);
        }

        var amPm = hour >= 12 ? "PM" : "AM";
        long displayHour = hour % 12;
        if (displayHour == 0) {
            displayHour = 12;
        }
        return String.format("%02d:%02d %s", displayHour, minutes, amPm);
    }
}