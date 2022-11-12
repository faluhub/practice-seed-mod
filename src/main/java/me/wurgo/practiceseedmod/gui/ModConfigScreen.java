package me.wurgo.practiceseedmod.gui;

import me.wurgo.practiceseedmod.config.ConfigPresets;
import me.wurgo.practiceseedmod.config.ConfigWrapper;
import me.wurgo.practiceseedmod.config.ConfigWriter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModConfigScreen extends Screen {
    private final AtomicBoolean enableBarterSeed = new AtomicBoolean(true);
    private final AtomicBoolean enableDrops = new AtomicBoolean(true);
    private ButtonWidget barterSeedButton;
    private ButtonWidget[] dropDependentButtons;

    public ModConfigScreen() {
        super(new LiteralText("Configure Mod:"));
    }

    @Override
    protected void init() {
        super.init();
        if (this.client == null) { return; }

        ConfigWrapper wrapper = new ConfigWrapper(ConfigWriter.INSTANCE);

        int buttonWidth = 200;
        int spacingY = 24;

        this.addButton(
                new ButtonWidget(
                        this.width / 2 - buttonWidth / 2,
                        this.height / 4,
                        buttonWidth,
                        20,
                        new LiteralText("Copy UUID"),
                        b -> {
                            if (this.client != null) {
                                this.client.openScreen(new LinkUUIDScreen(this));
                            }
                        },
                        (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Generates a new UUID to link with Discord."), i, j)
                )
        );
        ButtonWidget bw = this.addButton(
                new ButtonWidget(
                        this.width / 2 - buttonWidth / 2,
                        this.height / 4 + spacingY * 2,
                        buttonWidth,
                        20,
                        new LiteralText(""),
                        b -> {
                            boolean firstTryFlint = wrapper.getBoolValue("firstTryFlint", true);
                            wrapper.putBoolValue("firstTryFlint", !firstTryFlint);
                            b.setMessage(new LiteralText("Guarantee FTF: " + (firstTryFlint ? "No" : "Yes")));
                        },
                        (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Guarantees First Try Flint."), i, j)
                )
        );
        boolean firstTryFlint = wrapper.getBoolValue("firstTryFlint", true);
        bw.setMessage(new LiteralText("Guarantee FTF: " + (firstTryFlint ? "Yes" : "No")));
        ButtonWidget bw1 = this.addButton(
                new ButtonWidget(
                        this.width / 2 - buttonWidth / 2,
                        this.height / 4 + spacingY * 3,
                        buttonWidth,
                        20,
                        new LiteralText(""),
                        b -> {
                            boolean guaranteeDrops = wrapper.getBoolValue("guaranteeDrops", true);
                            wrapper.putBoolValue("guaranteeDrops", !guaranteeDrops);
                            this.enableDrops.set(!guaranteeDrops);
                            b.setMessage(new LiteralText("Guarantee Drops: " + (guaranteeDrops ? "No" : "Yes")));
                        },
                        (b, matrices, i, j) -> this.renderTooltip(matrices, this.client.textRenderer.wrapLines(new LiteralText("Guarantees mob drops like Blaze Rods and Ender Pearls."), 150), i, j)
                )
        );
        boolean guaranteeDrops = wrapper.getBoolValue("guaranteeDrops", true);
        this.enableDrops.set(guaranteeDrops);
        bw1.setMessage(new LiteralText("Guarantee Drops: " + (guaranteeDrops ? "Yes" : "No")));
        ButtonWidget bw2 = this.addButton(
                new ButtonWidget(
                        this.width / 2 - buttonWidth / 2,
                        this.height / 4 + spacingY * 4,
                        buttonWidth,
                        20,
                        new LiteralText(""),
                        b -> {
                            boolean setBarterSeed = wrapper.getBoolValue("setBarterSeed", true);
                            wrapper.putBoolValue("setBarterSeed", !setBarterSeed);
                            this.enableBarterSeed.set(!setBarterSeed);
                            b.setMessage(new LiteralText("Set Barter Seed: " + (setBarterSeed ? "No" : "Yes")));
                        },
                        (b, matrices, i, j) -> this.renderTooltip(matrices, this.client.textRenderer.wrapLines(new LiteralText("Picks from 5 good barter seeds."), 150), i, j)
                )
        );
        boolean setBarterSeed = wrapper.getBoolValue("setBarterSeed", true);
        this.enableBarterSeed.set(setBarterSeed);
        bw2.setMessage(new LiteralText("Set Barter Seed: " + (setBarterSeed ? "Yes" : "No")));
        ButtonWidget bw3 = this.addButton(
                new ButtonWidget(
                        this.width / 2 - buttonWidth / 2,
                        this.height / 4 + spacingY * 5,
                        buttonWidth,
                        20,
                        new LiteralText(""),
                        b -> {
                            int limit = ConfigPresets.BarterSeedPresets.values().length - 1;
                            int barterSeedPresetIndex = wrapper.getIntValue("barterSeedPresetIndex", 0, limit);
                            int value = barterSeedPresetIndex + 1;
                            if (value > limit) { value = 0; }
                            wrapper.putIntValue("barterSeedPresetIndex", value);
                            ConfigPresets.BarterSeedPresets preset = List.of(ConfigPresets.BarterSeedPresets.values()).get(value);
                            b.setMessage(new LiteralText("Barter Seed Tier: " + preset.cosmeticName));
                        },
                        (b, matrices, i, j) -> this.renderTooltip(matrices, this.client.textRenderer.wrapLines(new LiteralText("The preset for the 5 barter seeds picked between."), 150), i, j)
                )
        );
        {
            bw3.active = this.enableBarterSeed.get();
            int limit = ConfigPresets.BarterSeedPresets.values().length - 1;
            int barterSeedPresetIndex = wrapper.getIntValue("barterSeedPresetIndex", 0, limit);
            ConfigPresets.BarterSeedPresets preset = List.of(ConfigPresets.BarterSeedPresets.values()).get(barterSeedPresetIndex);
            bw3.setMessage(new LiteralText("Barter Seed Tier: " + preset.cosmeticName));
            this.barterSeedButton = bw3;
        }
        ButtonWidget bw4 = this.addButton(
                new ButtonWidget(
                        this.width / 2 - buttonWidth / 2,
                        this.height / 4 + spacingY * 6,
                        buttonWidth / 2,
                        20,
                        new LiteralText(""),
                        b -> {
                            if (this.client == null) { return; }

                            int limit = 16;
                            int currentKills = wrapper.getIntValue("blazeDropKills", 6, limit);
                            int currentValue = wrapper.getIntValue("blazeDropRods", 6, limit);
                            int value = !Screen.hasShiftDown() ? currentValue + 1 : currentValue - 1;
                            if (value < 0) { value = currentKills; }
                            else if (value > limit || value > currentKills) { value = 0; }
                            wrapper.putIntValue("blazeDropRods", value);
                            b.setMessage(new LiteralText("Rods: " + value));
                        },
                        (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Shift + Click to decrease."), i, j)
                )
        );
        ButtonWidget bw5 = this.addButton(
                new ButtonWidget(
                        this.width / 2,
                        this.height / 4 + spacingY * 6,
                        buttonWidth / 2,
                        20,
                        new LiteralText(""),
                        b -> {
                            if (this.client == null) { return; }

                            int limit = 16;
                            int currentRods = wrapper.getIntValue("blazeDropRods", 6, limit);
                            int currentValue = wrapper.getIntValue("blazeDropKills", 6, limit);
                            int value = !Screen.hasShiftDown() ? currentValue + 1 : currentValue - 1;
                            if (value < -1) { value = currentRods; }
                            else if (value > limit) { value = -1; }
                            else if (value < currentRods && value != -1) {
                                if (currentValue < value) { value = currentRods; }
                                else { value = -1; }
                            }
                            wrapper.putIntValue("blazeDropKills", value);
                            b.setMessage(value == -1 ? new LiteralText("Kills: Random") : new LiteralText("Kills: " + value));
                        },
                        (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Shift + Click to decrease."), i, j)
                )
        );
        {
            this.dropDependentButtons = new ButtonWidget[] { bw4, bw5 };
            bw4.active = bw5.active = this.enableDrops.get();
            int rods = wrapper.getIntValue("blazeDropRods", 6, 16);
            int kills = wrapper.getIntValue("blazeDropKills", 6, 16);
            bw4.setMessage(new LiteralText("Rods: " + rods));
            bw5.setMessage(kills == -1 ? new LiteralText("Kills: Random") : new LiteralText("Kills: " + kills));
        }

        this.addButton(
                new ButtonWidget(
                        this.width / 2 - buttonWidth / 2,
                        this.height / 4 + spacingY * 8,
                        buttonWidth,
                        20,
                        ScreenTexts.DONE,
                        b -> {
                            if (this.client != null) {
                                this.client.openScreen(null);
                            }
                        }
                )
        );
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 13, 16777215);

        this.barterSeedButton.active = this.enableBarterSeed.get();
        for (ButtonWidget widget : this.dropDependentButtons) {
            widget.active = this.enableDrops.get();
        }

        super.render(matrices, mouseX, mouseY, delta);
    }
}
