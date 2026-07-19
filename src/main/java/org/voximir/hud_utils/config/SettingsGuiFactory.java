package org.voximir.hud_utils.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v3.ConfigEntry;
import net.minecraft.client.gui.screens.Screen;
import org.voximir.hud_utils.utilities.TranslationKey;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class SettingsGuiFactory {
    private SettingsGuiFactory() {
    }

    public static Screen createSettingsGui(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(TranslationKey.getConfigFor("title").asComponent())
                .save(HudUtilsSettings.getInstance()::saveToFile)
                .category(Categories.createBehaviorCategory())
                .build()
                .generateScreen(parent);
    }

    private static <T> Option<T> registerOption(
            TranslationKey translationKey,
            ConfigEntry<T> entry,
            Function<Option<T>, ControllerBuilder<T>> controllerFactory,
            OptionFlag[] flags,
            BiFunction<T, TranslationKey, OptionDescription.Builder> descriptionBuilderFactory
    ) {
        var descriptionKey = translationKey.dot("description");
        if (descriptionBuilderFactory == null) {
            descriptionBuilderFactory = (ignoredValue, key) -> OptionDescription.createBuilder()
                    .text(key.asComponent());
        }

        var finalDescriptionBuilderFactory = descriptionBuilderFactory;
        Option.Builder<T> builder = Option.<T>createBuilder()
                .name(translationKey.asComponent())
                .description(value -> finalDescriptionBuilderFactory.apply(value, descriptionKey).build())
                .binding(entry.defaultValue(), entry::get, entry::set)
                .controller(controllerFactory);

        if (flags.length > 0) {
            builder.flag(flags);
        }

        return builder.build();
    }

    private static <T> void bindDependentsAvailability(
            Option<T> sourceOption,
            List<Option<?>> dependentOptions,
            Function<T, Boolean> availabilityPredicate
    ) {
        for (var dependent : dependentOptions) {
            dependent.setAvailable(availabilityPredicate.apply(sourceOption.pendingValue()));
        }

        sourceOption.addEventListener((option, event) -> {
            if (event == OptionEventListener.Event.INITIAL || event == OptionEventListener.Event.STATE_CHANGE) {
                for (var dependent : dependentOptions) {
                    dependent.setAvailable(availabilityPredicate.apply(option.pendingValue()));
                }
            }
        });
    }

    private static class Categories {
        private static ConfigCategory createBehaviorCategory() {
            TranslationKey category = TranslationKey.getCategory("general");

            var showHud = registerOption(
                    category.dot("show_hud"),
                    HudUtilsSettings.getShowHud(),
                    TickBoxControllerBuilder::create,
                    new OptionFlag[0],
                    null
            );

            class Groups {
                OptionGroup createDisplayGroup() {
                    TranslationKey group = category.dot("display");

                    var displayCoordinates = registerOption(
                            group.dot("display_coordinates"),
                            HudUtilsSettings.getDisplayCoordinates(),
                            TickBoxControllerBuilder::create,
                            new OptionFlag[0],
                            null
                    );

                    var displayDays = registerOption(
                            group.dot("display_days"),
                            HudUtilsSettings.getDisplayDays(),
                            TickBoxControllerBuilder::create,
                            new OptionFlag[0],
                            null
                    );

                    var displayTime = registerOption(
                            group.dot("display_time"),
                            HudUtilsSettings.getDisplayTime(),
                            TickBoxControllerBuilder::create,
                            new OptionFlag[0],
                            null
                    );

                    var use24Hour = registerOption(
                            group.dot("use_24_hour"),
                            HudUtilsSettings.getUse24Hour(),
                            TickBoxControllerBuilder::create,
                            new OptionFlag[0],
                            null
                    );

                    bindDependentsAvailability(
                            displayTime,
                            List.of(use24Hour),
                            Boolean::booleanValue
                    );

                    return OptionGroup.createBuilder()
                            .name(group.asComponent())
                            .option(displayCoordinates)
                            .option(displayDays)
                            .option(displayTime)
                            .option(use24Hour)
                            .build();
                }
            }

            Groups groups = new Groups();

            return ConfigCategory.createBuilder()
                    .name(category.asComponent())
                    .option(showHud)
                    .group(groups.createDisplayGroup())
                    .build();
        }
    }
}