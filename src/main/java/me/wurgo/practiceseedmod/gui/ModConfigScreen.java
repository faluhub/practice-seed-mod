package me.wurgo.practiceseedmod.gui;

import me.wurgo.practiceseedmod.BarterSeedPresets;
import me.wurgo.practiceseedmod.config.ConfigWrapper;
import me.wurgo.practiceseedmod.config.ConfigWriter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModConfigScreen extends Screen {
    private final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
    private ButtonWidget buttonWidget;

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
                        }
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
                            b.setMessage(new LiteralText("Guarantee Drops: " + (guaranteeDrops ? "No" : "Yes")));
                        },
                        (b, matrices, i, j) -> this.renderTooltip(matrices, this.client.textRenderer.wrapLines(new LiteralText("Guarantees mob dropslike Blaze Rods and Ender Pearls."), 150), i, j)
                )
        );
        boolean guaranteeDrops = wrapper.getBoolValue("guaranteeDrops", true);
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
                            this.atomicBoolean.set(!setBarterSeed);
                            b.setMessage(new LiteralText("Set Barter Seed: " + (setBarterSeed ? "No" : "Yes")));
                        },
                        (b, matrices, i, j) -> this.renderTooltip(matrices, this.client.textRenderer.wrapLines(new LiteralText("Picks from 5 good barter seeds."), 150), i, j)
                )
        );
        boolean setBarterSeed = wrapper.getBoolValue("setBarterSeed", true);
        this.atomicBoolean.set(setBarterSeed);
        bw2.setMessage(new LiteralText("Set Barter Seed: " + (setBarterSeed ? "Yes" : "No")));
        ButtonWidget bw3 = this.addButton(
                new ButtonWidget(
                        this.width / 2 - buttonWidth / 2,
                        this.height / 4 + spacingY * 5,
                        buttonWidth,
                        20,
                        new LiteralText(""),
                        b -> {
                            int limit = BarterSeedPresets.values().length - 1;
                            int barterSeedPresetIndex = wrapper.getIntValue("barterSeedPresetIndex", 0, limit);
                            int value = barterSeedPresetIndex + 1;
                            if (value > limit) { value = 0; }
                            wrapper.putIntValue("barterSeedPresetIndex", value);
                            BarterSeedPresets preset = List.of(BarterSeedPresets.values()).get(value);
                            b.setMessage(new LiteralText("Barter Seed Tier: " + preset.cosmeticName));
                        },
                        (b, matrices, i, j) -> this.renderTooltip(matrices, this.client.textRenderer.wrapLines(new LiteralText("The preset for the 5 barter seeds picked between."), 150), i, j)
                )
        );
        bw3.active = this.atomicBoolean.get();
        int limit = BarterSeedPresets.values().length - 1;
        int barterSeedPresetIndex = wrapper.getIntValue("barterSeedPresetIndex", 0, limit);
        BarterSeedPresets preset = List.of(BarterSeedPresets.values()).get(barterSeedPresetIndex);
        bw3.setMessage(new LiteralText("Barter Seed Tier: " + preset.cosmeticName));
        this.buttonWidget = bw3;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 13, 16777215);

        this.buttonWidget.active = this.atomicBoolean.get();

        super.render(matrices, mouseX, mouseY, delta);
    }
}
