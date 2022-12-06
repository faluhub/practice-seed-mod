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
    public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer("practiceseedmod").orElse(null);

    public static final String LOGGER_NAME = Objects.requireNonNull(MOD_CONTAINER).getMetadata().getName();
    public static final Logger LOGGER = LogManager.getLogger(LOGGER_NAME);
    public static Socket SOCKET;
    public static final URI SOCKET_URI = URI.create("https://desolate-cove-87183.herokuapp.com");

    public static boolean running = false;
    public static UUID uuid;
    public static final List<Long> queue = new ArrayList<>();
    public static Long currentSeed;
    public static String seedNotes;

    public static boolean isRace = false;
    public static String racePassword;
    public static String raceHost;

    public static final AtomicBoolean saving = new AtomicBoolean(false);
    public static final Object saveLock = new Object();

    public static Random barteringRandom;
    public static Random gravelDropRandom;
    public static Random blazeDropRandom;

    public static int maximumPerchSeconds;

    public static void log(Object msg) {
        LOGGER.log(Level.INFO, msg);
    }

    public static void initialiseLevelData(long l) {
        WorldConstants.reset();

        MinecraftClient client = MinecraftClient.getInstance();
        ConfigWrapper wrapper = new ConfigWrapper();

        client.execute(() -> client.method_29970(new SaveLevelScreen(new LiteralText("Initialising barter seed"))));
        log("Initialising barter seed.");
        int limit = ConfigPresets.BarterSeedPresets.values().length - 1;
        int barterSeedPresetIndex = wrapper.getIntValue("barterSeedPresetIndex", 0, limit);
        ConfigPresets.BarterSeedPresets preset = List.of(ConfigPresets.BarterSeedPresets.values()).get(barterSeedPresetIndex);
        barteringRandom = new Random();
        if (preset.getSeeds() != null) {
            barteringRandom.setSeed(preset.getSeeds().get(new Random().nextInt(preset.getSeeds().size() - 1)));
        }

        client.execute(() -> client.method_29970(new SaveLevelScreen(new LiteralText("Initialising gravel drop seed"))));
        log("Initialising gravel drop seed.");
        int flint = wrapper.getIntValue("gravelDropFlint", 1, 10);
        int gravel = wrapper.getIntValue("gravelDropGravel", 1, 10);
        if (gravel == -1) {
            gravel = flint + new Random(l).nextInt(4);
        }
        gravelDropRandom = new Random(new RandomSeedGenerator().getSeed(flint, gravel));

        client.execute(() -> client.method_29970(new SaveLevelScreen(new LiteralText("Initialising blaze drop seed"))));
        log("Initialising blaze drop seed.");
        int rods = wrapper.getIntValue("blazeDropRods", 6, 16);
        int kills = wrapper.getIntValue("blazeDropKills", 6, 16);
        if (kills == -1) {
            kills = rods + new Random(l).nextInt(6);
        }
        blazeDropRandom = new Random(new RandomSeedGenerator().getSeed(rods, kills));

        client.execute(() -> client.method_29970(new SaveLevelScreen(new LiteralText("Initialising dragon perch"))));
        log("Initialising dragon perch.");
        limit = ConfigPresets.DragonPerchTimes.values().length - 1;
        int index = wrapper.getIntValue("dragonPerchTimeIndex", 0, limit);
        ConfigPresets.DragonPerchTimes dragonPreset = List.of(ConfigPresets.DragonPerchTimes.values()).get(index);
        maximumPerchSeconds = dragonPreset.getSeconds();
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

        LevelInfo levelInfo = new LevelInfo(
                "Practice Seed",
                GameMode.SURVIVAL,
                false,
                Difficulty.EASY,
                !isRace,
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

        SOCKET = IO.socket(SOCKET_URI, options);
        SOCKET.connect();
        SOCKET.on("play-seed", args1 -> {
            JsonParser parser = new JsonParser();
            JsonArray args = parser.parse(Arrays.toString(args1)).getAsJsonArray().get(0).getAsJsonArray();

            if (uuid != null && args.get(0).getAsString().equals(uuid.toString())) {
                try {
                    long l = Long.parseLong(args.get(1).getAsString());
                    if (!(running && isRace)) {
                        queue.add(l);
                    }

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
                } catch(IndexOutOfBoundsException ignored) {
                    log("Invalid request received!");
                }
            }
        });
        SOCKET.on("race-seed", args1 -> {
            JsonParser parser = new JsonParser();
            JsonArray args = parser.parse(Arrays.toString(args1)).getAsJsonArray().get(0).getAsJsonArray();

            if (args.get(0).getAsString().equals(racePassword) && isRace) {
                try {
                    queue.clear();
                    long l = Long.parseLong(args.get(1).getAsString());
                    queue.add(l);

                    raceHost = args.get(2).getAsString();

                    running = true;
                    playNextSeed();
                } catch (NumberFormatException ignored) {
                    log("Invalid seed!");
                } catch (IndexOutOfBoundsException ignored) {
                    log("Invalid request received!");
                }
            }
        });
    }
}
