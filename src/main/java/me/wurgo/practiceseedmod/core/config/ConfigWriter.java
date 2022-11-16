package me.wurgo.practiceseedmod.core.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.wurgo.practiceseedmod.PracticeSeedMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class ConfigWriter {
    public static ConfigWriter INSTANCE = new ConfigWriter("config");

    private final File file;

    public ConfigWriter(String fileName) {
        this.file = this.create(fileName);
    }

    private File create(String fileName) {
        File folder = new File(FabricLoader.getInstance().getConfigDir() + "/practiceseedmod");
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                PracticeSeedMod.log("Created config folder.");
            }
        }

        File file = new File(FabricLoader.getInstance().getConfigDir() + "/practiceseedmod/" + fileName + ".json");

        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    PracticeSeedMod.log("Created file '" + fileName + "'.");
                }
            } catch (IOException ignored) {}
        }

        return file;
    }

    public void write(JsonObject config) {
        this.create(this.file.getName().split("\\.")[0]);
        try {
            FileWriter writer = new FileWriter(this.file);

            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(config));
            writer.flush();
            writer.close();

            PracticeSeedMod.log("Saved file '" + this.file.getName() + "'. (Put)");
        } catch (IOException ignored) {}
    }

    public JsonObject get() {
        this.create(this.file.getName().split("\\.")[0]);
        try {
            FileReader reader = new FileReader(this.file);
            JsonParser parser = new JsonParser();

            Object obj = parser.parse(reader);

            return Objects.equals(JsonNull.INSTANCE, obj) ? new JsonObject() : (JsonObject) obj;
        } catch (IOException ignored) {}

        return null;
    }
}
