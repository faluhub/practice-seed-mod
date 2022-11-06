package me.wurgo.practiceseedmod;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.Nullable;

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
    public static Random barteringRandom;
    public static Long[] barteringSeeds = new Long[] {
            Long.parseLong("7064568383303603059"),
            Long.parseLong("7310735715797688977"),
            Long.parseLong("9068995232731620575"),
            Long.parseLong("-5756924456702763701"),
            Long.parseLong("-2488798226288537351")
    };

    public static void log(Object msg) {
        LOGGER.log(Level.INFO, msg);
    }

    public static boolean playNextSeed() {
        if (!queue.isEmpty()) {
            playNextSeed(queue.get(0));
            return true;
        }
        return false;
    }

    public static void playNextSeed(long l) {
        WorldConstants.reset();
        barteringRandom = new Random(barteringSeeds[new Random().nextInt(barteringSeeds.length - 1)]);

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

        MinecraftClient client = MinecraftClient.getInstance();

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

        IO.Options options = IO.Options.builder()
                .setTransports(new String[] {WebSocket.NAME})
                .build();

        SOCKET = IO.socket(URI.create("http://localhost:3001"), options);
        SOCKET.connect();
        SOCKET.on("play-seed", args1 -> {
            JsonParser parser = new JsonParser();
            JsonArray args = parser.parse(Arrays.toString(args1)).getAsJsonArray().get(0).getAsJsonArray();

            if (args.get(0).getAsString().equals(uuid.toString())) {
                try {
                    long l = Long.parseLong(args.get(1).getAsString());
                    queue.add(l);

                    if (!running && !MinecraftClient.getInstance().isInSingleplayer()) { playNextSeed(); }
                } catch (NumberFormatException ignored) {
                    log("Invalid seed!");
                }
            }
        });
    }
}
