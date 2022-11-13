package me.wurgo.practiceseedmod.gui;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import me.wurgo.practiceseedmod.updater.UpdateChecker;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class DownloadUpdateScreen extends Screen {

    public static boolean CHECKED = false;
    private enum Status { CHECK, DOWNLOADING, DONE }
    private Status currentStatus = Status.CHECK;

    private ButtonWidget closeButton;
    private ButtonWidget downloadButton;

    public DownloadUpdateScreen() {
        super(Text.method_30163(PracticeSeedMod.MOD_CONTAINER.getMetadata().getName() + " Update Checker"));
        DownloadUpdateScreen.CHECKED = true;
    }

    @Override
    protected void init() {
        super.init();

        this.closeButton = this.addButton(new ButtonWidget(width / 2 + 3, height - 50, 120, 20, Text.method_30163("Close"), (button) -> {
            if (this.client != null) {
                this.client.openScreen(new TitleScreen());
            }
        }));

        this.downloadButton = this.addButton(new ButtonWidget(width / 2 - 3 - 120, height - 50, 120, 20, Text.method_30163("Download"), (button) -> {
            if (currentStatus == Status.CHECK) {
                new Thread(() -> {
                    UpdateChecker.downloadUrl(UpdateChecker.LATEST_DOWNLOAD_NAME, UpdateChecker.LATEST_DOWNLOAD_URL);

                    this.closeButton.visible = true;
                    this.downloadButton.active = true;
                    this.downloadButton.setMessage(Text.method_30163("Close This Instance"));
                    this.downloadButton.setWidth(120);

                    this.currentStatus = Status.DONE;
                }, "PSM UpdateChecker").start();

                this.closeButton.visible = false;
                this.downloadButton.active = false;
                this.downloadButton.setMessage(Text.method_30163("Downloading..."));
                this.downloadButton.setWidth(246);

                this.currentStatus = Status.DOWNLOADING;
            } else if (currentStatus == Status.DONE) {
                if (this.client != null) {
                    this.client.scheduleStop();
                }
            }
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        final int whiteColor = BackgroundHelper.ColorMixer.getArgb(255, 255, 255, 255);

        this.drawCenteredText(matrices, this.textRenderer, this.title, width / 2, 20, whiteColor);

        if (currentStatus == Status.CHECK) {
            String modVersion = PracticeSeedMod.MOD_CONTAINER.getMetadata().getVersion().getFriendlyString();

            this.drawCenteredString(matrices, this.textRenderer, "New Update for " + PracticeSeedMod.MOD_CONTAINER.getMetadata().getName() + " has been found!", width / 2, height / 2 - 20, whiteColor);
            this.drawCenteredString(matrices, this.textRenderer, String.format("Do you want to download it? (Current : %s, Latest : %s)", modVersion.split("\\+")[0], UpdateChecker.LATEST_VERSION), width / 2, height / 2 - 9, whiteColor);
        }
        if (currentStatus == Status.DOWNLOADING) {
            this.drawCenteredString(matrices, this.textRenderer, String.format("Downloading '%s'...", UpdateChecker.LATEST_DOWNLOAD_NAME), width / 2, height / 2 - 20, whiteColor);
        }
        if (currentStatus == Status.DONE) {
            this.drawCenteredString(matrices, this.textRenderer, "Done!", width / 2, height / 2 - 20, whiteColor);
            this.drawCenteredString(matrices, this.textRenderer, "Do you want to close and re-launch this instance?", width / 2, height / 2 - 9, whiteColor);
        }

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
    }
}
