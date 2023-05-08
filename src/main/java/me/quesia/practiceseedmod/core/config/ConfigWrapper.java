package me.quesia.practiceseedmod.core.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConfigWrapper {
    private final ConfigWriter writer;
    private final JsonObject config;
    private boolean hasChanged;

    public ConfigWrapper() {
        this.writer = ConfigWriter.INSTANCE;
        this.config = this.writer.get();
        this.hasChanged = false;
    }

    public void putBoolValue(String key, boolean value) {
        this.config.addProperty(key, value);
        this.hasChanged = true;
    }

    public boolean inverseBoolValue(String key, boolean def) {
        boolean value = this.getBoolValue(key, def);
        this.putBoolValue(key, !value);
        return !value;
    }

    public boolean getBoolValue(String key, boolean def) {
        JsonElement element = this.config.get(key);
        if (element != null) {
            return element.getAsBoolean();
        }
        return def;
    }

    public void putIntValue(String key, int value) {
        this.config.addProperty(key, value);
        this.hasChanged = true;
    }

    public int getIntValue(String key, int def, int limit) {
        JsonElement element = this.config.get(key);
        if (element != null) {
            int value = element.getAsInt();
            if (value > limit) {
                value = def;
            }
            return value;
        }
        return def;
    }

    public void save() {
        if (this.hasChanged) {
            this.writer.write(this.config);
        }
    }
}
