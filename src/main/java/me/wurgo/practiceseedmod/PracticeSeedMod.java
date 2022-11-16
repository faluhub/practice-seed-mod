package me.wurgo.practiceseedmod;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;
import me.wurgo.practiceseedmod.core.WorldConstants;
import me.wurgo.practiceseedmod.core.config.ConfigPresets;
import me.wurgo.practiceseedmod.core.config.ConfigWrapper;
import me.wurgo.practiceseedmod.core.config.ConfigWriter;
import me.wurgo.practiceseedmod.core.RandomSeedGenerator;
import me.wurgo.practiceseedmod.core.UpdateChecker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import org.apache.logging.log4j.*;

import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PracticeSeedMod implements ClientModInitializer {
    public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer("practiceseedmod").get();
    public static final String LOGGER_NAME = MOD_CONTAINER.getMetadata().getName();
    public static Logger LOGGER = LogManager.getLogger(LOGGER_NAME);
    public static Socket SOCKET;
    public static UUID uuid;
    public static boolean running = false;
    public static final AtomicBoolean saving = new AtomicBoolean(false);
    public static List<Long> queue = new ArrayList<>();
    public static final Object saveLock = new Object();
    public static Long currentSeed;
    public static String seedNotes;
    public static Random barteringRandom;
    public static Random blazeDropRandom;

    public static void log(Object msg) {
        LOGGER.log(Level.INFO, msg);
    }

    public static boolean playNextSeed() {
        if (!queue.isEmpty() && running) {
            playNextSeed(queue.get(0));
            return true;
        }
        return false;
    }

    public static void playNextSeed(long l) {
        MinecraftClient client = MinecraftClient.getInstance();

        WorldConstants.reset();

        ConfigWrapper wrapper = new ConfigWrapper(ConfigWriter.INSTANCE);

        client.execute(() -> {
            if (wrapper.getBoolValue("setBarterSeed", true)) {
                client.method_29970(new SaveLevelScreen(new LiteralText("Initialising barter seed")));
                int limit = ConfigPresets.BarterSeedPresets.values().length - 1;
                int barterSeedPresetIndex = wrapper.getIntValue("barterSeedPresetIndex", 0, limit);
                ConfigPresets.BarterSeedPresets preset = List.of(ConfigPresets.BarterSeedPresets.values()).get(barterSeedPresetIndex);
                barteringRandom = new Random(preset.seeds.get(new Random().nextInt(preset.seeds.size() - 1)));
            }
            if (wrapper.getBoolValue("guaranteeDrops", true)) {
                client.method_29970(new SaveLevelScreen(new LiteralText("Generating blaze drop seed")));
                int rods = wrapper.getIntValue("blazeDropRods", 6, 16);
                int kills = wrapper.getIntValue("blazeDropKills", 6, 16);
                if (kills == -1) {
                    kills = rods + new Random().nextInt(6);
                }
                blazeDropRandom = new Random(new RandomSeedGenerator().getBlazeDropSeed(rods, kills));
            }
        });

        LevelInfo levelInfo = new LevelInfo(
                "Practice Seed",
                GameMode.SURVIVAL,
                false,
                Difficulty.EASY,
                true,
                new GameRules(),
                DataPackSettings.SAFE_MODE
        );
        RegistryTracker.Modifiable registryTracker = RegistryTracker.create();
        GeneratorOptions generatorOptions = GeneratorType.DEFAULT.method_29077(
                registryTracker,
                l,
                true,
                false
        );

        client.execute(() -> {
            if (client.world != null) {
                client.world.disconnect();
                if (client.isInSingleplayer()) {
                    client.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
                } else {
                    client.disconnect();
                }
            }

            client.openScreen(
                    new CreateWorldScreen(
                            null,
                            levelInfo,
                            generatorOptions,
                            null,
                            registryTracker
                    )
            );
        });
        running = true;
        queue.remove(l);
        currentSeed = l;

        log("Playing practice seed: " + l);
    }

    @Override
    public void onInitializeClient() {
        WorldConstants.reset();
        UpdateChecker.check();

        IO.Options options = IO.Options.builder()
                .setTransports(new String[] {WebSocket.NAME})
                .build();

        SOCKET = IO.socket(URI.create("https://salty-wave-05504.herokuapp.com/"), options);
        SOCKET.connect();
        SOCKET.on("play-seed", args1 -> {
            JsonParser parser = new JsonParser();
            JsonArray args = parser.parse(Arrays.toString(args1)).getAsJsonArray().get(0).getAsJsonArray();

            if (args.get(0).getAsString().equals(uuid.toString())) {
                try {
                    long l = Long.parseLong(args.get(1).getAsString());
                    queue.add(l);

                    JsonElement notesElement = args.get(2);
                    if (notesElement != null && !notesElement.isJsonNull()) {
                        seedNotes = notesElement.getAsString();
                    }

                    if (!running && !MinecraftClient.getInstance().isInSingleplayer()) {
                        running = true;
                        playNextSeed();
                    }
                } catch (NumberFormatException ignored) {
                    log("Invalid seed!");
                }
            }
        });
    }
}
