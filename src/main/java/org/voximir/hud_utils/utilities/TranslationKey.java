package org.voximir.hud_utils.utilities;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.voximir.hud_utils.HudUtils;

public final class TranslationKey {
    private final TranslationKey parent;
    private final String part;
    private String cached;

    private TranslationKey(TranslationKey parent, String part) {
        this.parent = parent;
        this.part = part;
    }

    public static TranslationKey of(String... parts) {
        TranslationKey key = null;
        for (String part : parts) {
            key = new TranslationKey(key, part);
        }
        return key;
    }

    public TranslationKey dot(String part) {
        return new TranslationKey(this, part);
    }

    private String build() {
        StringBuilder sb = new StringBuilder();
        buildInto(sb);
        return sb.toString();
    }

    private void buildInto(StringBuilder sb) {
        if (parent != null) {
            parent.buildInto(sb);
            sb.append('.');
        }
        sb.append(part);
    }

    public String asString() {
        if (cached == null) {
            if (parent != null && parent.cached != null) {
                cached = parent.cached + "." + part;
            } else {
                cached = build();
            }
        }
        return cached;
    }

    public MutableComponent asComponent() {
        return Component.translatable(asString());
    }

    public static TranslationKey getConfigFor(String key) {
        return of("config", HudUtils.MOD_ID, key);
    }

    public static TranslationKey getCategory(String key) {
        return getConfigFor("category").dot(key);
    }
}