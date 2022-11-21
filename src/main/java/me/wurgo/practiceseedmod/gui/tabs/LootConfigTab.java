package me.wurgo.practiceseedmod.gui.tabs;

import me.wurgo.practiceseedmod.core.config.ConfigPresets;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;

import java.util.List;

public class LootConfigTab extends ConfigTab {
    private final int blazeLimit = 16;
    private final int blazeDef = 6;

    public LootConfigTab() {
        super("Loot");
    }

    private int compareRodsKills(int rods, int kills) {
        int value = !Screen.hasShiftDown() ? rods + 1 : rods - 1;
        if (value < 0) { value = kills; }
        else if (value > this.blazeLimit || value > kills) { value = 0; }
        return value;
    }

    private int compareKillsRods(int kills, int rods) {
        int value = !Screen.hasShiftDown() ? kills + 1 : kills - 1;
        if (value < -1) { value = rods; }
        else if (value > this.blazeLimit) { value = -1; }
        else if (value < rods && value != -1) {
            // this makes no fucking sense but it works
            if (kills < value) { value = rods; }
            else { value = -1; }
        }
        return value;
    }

    @Override
    public void addButtons() {
        this.addButton(
                new ButtonWidget(
                        this.width / 2 - this.btnWidth / 2,
                        this.height / 4,
                        this.btnWidth,
                        this.btnHeight,
                        new LiteralText("First Try Flint: " + this.getToggleText(this.wrapper.getBoolValue("firstTryFlint", true))),
                        b -> b.setMessage(new LiteralText("First Try Flint: " + this.getToggleText(this.wrapper.inverseBoolValue("firstTryFlint", true)))),
                        (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Toggles first try flint guarantee."), i, j)
                )
        );

        this.addButton(
                new ButtonWidget(
                        this.width / 2 - this.btnWidth / 2,
                        this.height / 4 + spacingY,
                        this.btnWidth,
                        this.btnHeight,
                        new LiteralText("Guarantee Pearls: " + this.getToggleText(this.wrapper.getBoolValue("guaranteePearls", true))),
                        b -> b.setMessage(new LiteralText("Guarantee Pearls: " + this.getToggleText(this.wrapper.inverseBoolValue("guaranteePearls", true)))),
                        (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Toggles the pearl drop from enderman guarantee."), i, j)
                )
        );

        {
            ButtonWidget bw = this.addButton(
                    new ButtonWidget(
                            this.width / 2 - this.btnWidth / 2,
                            this.height / 4 + spacingY * 2,
                            this.btnWidth,
                            this.btnHeight,
                            new LiteralText(""),
                            b -> {
                                int limit = ConfigPresets.BarterSeedPresets.values().length - 1;
                                int index = wrapper.getIntValue("barterSeedPresetIndex", 0, limit);
                                int value = index + 1;
                                if (value > limit) { value = 0; }
                                wrapper.putIntValue("barterSeedPresetIndex", value);
                                ConfigPresets.BarterSeedPresets preset = List.of(ConfigPresets.BarterSeedPresets.values()).get(value);
                                b.setMessage(new LiteralText("Barter Seed Preset: " + preset.getCosmeticName()));
                            },
                            (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Cycles through the barter tiers."), i, j)
                    )
            );
            int limit = ConfigPresets.BarterSeedPresets.values().length - 1;
            int value = wrapper.getIntValue("barterSeedPresetIndex", 0, limit);
            ConfigPresets.BarterSeedPresets preset = List.of(ConfigPresets.BarterSeedPresets.values()).get(value);
            bw.setMessage(new LiteralText("Barter Seed Preset: " + preset.getCosmeticName()));
        }

        this.addButton(
                new ButtonWidget(
                        this.width / 2 - this.btnWidth / 2,
                        this.height / 4 + spacingY * 3,
                        this.btnWidth / 2,
                        this.btnHeight,
                        new LiteralText("Rods: " + wrapper.getIntValue("blazeDropRods", this.blazeDef, this.blazeLimit)),
                        b -> {
                            int rods = this.wrapper.getIntValue("blazeDropRods", this.blazeDef, this.blazeLimit);
                            int kills = this.wrapper.getIntValue("blazeDropKills", this.blazeDef, this.blazeLimit);
                            int value = this.compareRodsKills(rods, kills);
                            wrapper.putIntValue("blazeDropRods", value);
                            b.setMessage(new LiteralText("Rods: " + value));
                        },
                        (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Shift + Click to decrease."), i, j)
                )
        );

        {
            ButtonWidget bw2 = this.addButton(
                    new ButtonWidget(
                            this.width / 2,
                            this.height / 4 + spacingY * 3,
                            this.btnWidth / 2,
                            this.btnHeight,
                            new LiteralText(""),
                            b -> {
                                int kills = this.wrapper.getIntValue("blazeDropKills", this.blazeDef, this.blazeLimit);
                                int rods = this.wrapper.getIntValue("blazeDropRods", this.blazeDef, this.blazeLimit);
                                int value = this.compareKillsRods(kills, rods);
                                wrapper.putIntValue("blazeDropKills", value);
                                b.setMessage(
                                        value == -1
                                                ? new LiteralText("Kills: Random")
                                                : new LiteralText("Kills: " + value)
                                );
                            },
                            (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Shift + Click to decrease."), i, j)
                    )
            );
            int kills = wrapper.getIntValue("blazeDropKills", 6, 16);
            bw2.setMessage(
                    kills == -1
                            ? new LiteralText("Kills: Random")
                            : new LiteralText("Kills: " + kills)
            );
        }
    }

    @Override
    public int getButtonAmount() {
        return super.getButtonAmount() - 1;
    }
}
