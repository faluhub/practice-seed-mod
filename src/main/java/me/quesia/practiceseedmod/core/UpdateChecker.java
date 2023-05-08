package me.quesia.practiceseedmod.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.quesia.practiceseedmod.PracticeSeedMod;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class UpdateChecker {
    public static String LATEST_VERSION = null;
    public static String LATEST_DOWNLOAD_URL = null;
    public static String LATEST_DOWNLOAD_NAME = null;

    public static void check() {
        try {
            String modVersion = PracticeSeedMod.MOD_CONTAINER.getMetadata().getVersion().getFriendlyString();

            URL u = new URL("https://api.github.com/repos/QuesiaSR/practice-seed-mod/releases");
            HttpURLConnection c = (HttpURLConnection) u.openConnection();

            c.setConnectTimeout(10000);
            c.setReadTimeout(10000);

            InputStreamReader r = new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8);
            JsonElement jsonElement = new JsonParser().parse(r);
            if (jsonElement.getAsJsonArray().size() != 0) {
                for (JsonElement element : jsonElement.getAsJsonArray()) {
                    JsonObject versionData = element.getAsJsonObject();
                    if (!versionData.get("prerelease").getAsBoolean()) {
                        for (JsonElement asset : versionData.get("assets").getAsJsonArray()) {
                            JsonObject assetData = asset.getAsJsonObject();
                            String versionName = assetData.get("name").getAsString();
                            if (!versionName.endsWith(".jar")) continue;
                            versionName = versionName.substring(0, versionName.length() - 4);

                            String targetVersionName = versionName.split("-")[1].split("\\+")[0];
                            String targetMCVersionName = versionName.split("-")[1].split("\\+")[1];
                            String currentVersionName = modVersion.split("\\+")[0];
                            String currentMCVersionName = modVersion.split("\\+")[1];
                            if (Objects.equals(targetMCVersionName, currentMCVersionName) && VersionPredicate.parse(">" + currentVersionName).test(SemanticVersion.parse(targetVersionName))) {
                                LATEST_DOWNLOAD_URL = assetData.get("browser_download_url").getAsString();
                                LATEST_DOWNLOAD_NAME = versionName + ".jar";
                                LATEST_VERSION = targetVersionName;
                            }
                        }
                    }
                }
            }
            for (File file : Objects.requireNonNull(FabricLoader.getInstance().getGameDir().resolve("mods").toFile().listFiles())) {
                String modId = PracticeSeedMod.MOD_CONTAINER.getMetadata().getId();
                String fileName = file.getName();
                String targetVersionName = fileName.split("-")[1].split("\\+")[0].replace(".jar", "");
                if (fileName.startsWith(modId) && VersionPredicate.parse("<" + modVersion).test(SemanticVersion.parse(targetVersionName))) {
                    FileUtils.deleteQuietly(file);
                    break;
                }
            }

            PracticeSeedMod.log("Done checking for new releases.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadUrl(String name, String url) {
        File destinationFile = FabricLoader.getInstance().getGameDir().resolve("mods").resolve(name).toFile();
        try { FileUtils.copyURLToFile(new URL(url), destinationFile); }
        catch (Exception e) { e.printStackTrace(); }
    }
}
