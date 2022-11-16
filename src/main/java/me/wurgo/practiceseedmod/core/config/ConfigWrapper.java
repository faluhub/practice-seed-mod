package me.wurgo.practiceseedmod.core.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConfigWrapper {
    private final ConfigWriter writer;

    public ConfigWrapper(ConfigWriter writer) {
        this.writer = writer;
    }

    public void putBoolValue(String key, boolean value) {
        JsonObject config = this.writer.get();
        config.addProperty(key, value);
        this.writer.write(config);
    }

    public boolean getBoolValue(String key, boolean def) {
        JsonObject config = this.writer.get();
        JsonElement element = config.get(key);
        if (element != null) {
            return element.getAsBoolean();
        }
        return def;
    }

    public void putIntValue(String key, int value) {
        JsonObject config = this.writer.get();
        config.addProperty(key, value);
        this.writer.write(config);
    }

    public int getIntValue(String key, int def, int limit) {
        JsonObject config = this.writer.get();
        JsonElement element = config.get(key);
        if (element != null) {
            int value = element.getAsInt();
            if (value > limit) {
                value = def;
            }
            return value;
        }
        return def;
    }
}
