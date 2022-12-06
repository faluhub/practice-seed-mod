package me.wurgo.practiceseedmod.gui.tabs;

import me.wurgo.practiceseedmod.core.config.ConfigPresets;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;

import java.util.List;

public class EntitiesConfigTab extends ConfigTab {
    private final int blazeLimit = 16;
    private final int blazeDef = 6;

    public EntitiesConfigTab() {
        super("Entities");
    }

    @Override
    public void addButtons() {
        {
            ButtonWidget bw = this.addButton(
                    new ButtonWidget(
                            this.width / 2 - this.btnWidth / 2,
                            this.height / 4,
                            this.btnWidth,
                            this.btnHeight,
                            new LiteralText(""),
                            b -> {
                                int limit = ConfigPresets.DragonPerchTimes.values().length - 1;
                                int index = this.wrapper.getIntValue("dragonPerchTimeIndex", 0, limit);
                                int value = index + 1;
                                if (value > limit) { value = 0; }
                                this.wrapper.putIntValue("dragonPerchTimeIndex", value);
                                ConfigPresets.DragonPerchTimes preset = List.of(ConfigPresets.DragonPerchTimes.values()).get(value);
                                b.setMessage(new LiteralText("Dragon Perch Time Preset: " + preset.getCosmeticName()));
                            },
                            (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Cycles through the perch tiers."), i, j)
                    )
            );
            int limit = ConfigPresets.DragonPerchTimes.values().length - 1;
            int value = this.wrapper.getIntValue("dragonPerchTimeIndex", 0, limit);
            ConfigPresets.DragonPerchTimes preset = List.of(ConfigPresets.DragonPerchTimes.values()).get(value);
            bw.setMessage(new LiteralText("Dragon Perch Time Preset: " + preset.getCosmeticName()));
        }

        this.addButton(
                new ButtonWidget(
                        this.width / 2 - this.btnWidth / 2,
                        this.height / 4 + this.spacingY,
                        this.btnWidth,
                        this.btnHeight,
                        new LiteralText("No Early Flyaway: " + this.getToggleText(this.wrapper.getBoolValue("noEarlyFlyaway", true))),
                        b -> b.setMessage(new LiteralText("No Early Flyaway: " + this.getToggleText(this.wrapper.inverseBoolValue("noEarlyFlyaway", true)))),
                        (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Toggles if the dragon can fly away early."), i, j)
                )
        );

        this.addButton(
                new ButtonWidget(
                        this.width / 2 - this.btnWidth / 2,
                        this.height / 4 + this.spacingY * 2,
                        this.btnWidth,
                        this.btnHeight,
                        new LiteralText("Guarantee Drops: " + this.getToggleText(this.wrapper.getBoolValue("guaranteeEntityDrops", true))),
                        b -> b.setMessage(new LiteralText("Guarantee Drops: " + this.getToggleText(this.wrapper.inverseBoolValue("guaranteeEntityDrops", true)))),
                        (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Toggles the drop from entities guarantee."), i, j)
                )
        );

        {
            ButtonWidget bw = this.addButton(
                    new ButtonWidget(
                            this.width / 2 - this.btnWidth / 2,
                            this.height / 4 + this.spacingY * 3,
                            this.btnWidth,
                            this.btnHeight,
                            new LiteralText(""),
                            b -> {
                                int limit = ConfigPresets.BarterSeedPresets.values().length - 1;
                                int index = wrapper.getIntValue("barterSeedPresetIndex", 0, limit);
                                int value = index + 1;
                                if (value > limit) { value = 0; }
                                this.wrapper.putIntValue("barterSeedPresetIndex", value);
                                ConfigPresets.BarterSeedPresets preset = List.of(ConfigPresets.BarterSeedPresets.values()).get(value);
                                b.setMessage(new LiteralText("Barter Seed Preset: " + preset.getCosmeticName()));
                            },
                            (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Cycles through the barter tiers."), i, j)
                    )
            );
            int limit = ConfigPresets.BarterSeedPresets.values().length - 1;
            int value = this.wrapper.getIntValue("barterSeedPresetIndex", 0, limit);
            ConfigPresets.BarterSeedPresets preset = List.of(ConfigPresets.BarterSeedPresets.values()).get(value);
            bw.setMessage(new LiteralText("Barter Seed Preset: " + preset.getCosmeticName()));
        }

        {
            this.addButton(
                    new ButtonWidget(
                            this.width / 2 - this.btnWidth / 2,
                            this.height / 4 + this.spacingY * 4,
                            this.btnWidth / 2,
                            this.btnHeight,
                            new LiteralText("Rods: " + this.wrapper.getIntValue("blazeDropRods", this.blazeDef, this.blazeLimit)),
                            b -> {
                                int rods = this.wrapper.getIntValue("blazeDropRods", this.blazeDef, this.blazeLimit);
                                int kills = this.wrapper.getIntValue("blazeDropKills", this.blazeDef, this.blazeLimit);
                                int value = this.compareAB(rods, kills, this.blazeLimit);
                                this.wrapper.putIntValue("blazeDropRods", value);
                                b.setMessage(new LiteralText("Rods: " + value));
                            },
                            (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Shift + Click to decrease."), i, j)
                    )
            );

            {
                ButtonWidget bw3 = this.addButton(
                        new ButtonWidget(
                                this.width / 2,
                                this.height / 4 + this.spacingY * 4,
                                this.btnWidth / 2,
                                this.btnHeight,
                                new LiteralText(""),
                                b -> {
                                    int kills = this.wrapper.getIntValue("blazeDropKills", this.blazeDef, this.blazeLimit);
                                    int rods = this.wrapper.getIntValue("blazeDropRods", this.blazeDef, this.blazeLimit);
                                    int value = this.compareBA(kills, rods, this.blazeLimit);
                                    this.wrapper.putIntValue("blazeDropKills", value);
                                    b.setMessage(
                                            value == -1
                                                    ? new LiteralText("Kills: Random")
                                                    : new LiteralText("Kills: " + value)
                                    );
                                },
                                (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Shift + Click to decrease."), i, j)
                        )
                );
                int kills = this.wrapper.getIntValue("blazeDropKills", 6, 16);
                bw3.setMessage(
                        kills == -1
                                ? new LiteralText("Kills: Random")
                                : new LiteralText("Kills: " + kills)
                );
            }
        }
    }

    @Override
    public int getButtonAmount() {
        return super.getButtonAmount() - 1;
    }
}
