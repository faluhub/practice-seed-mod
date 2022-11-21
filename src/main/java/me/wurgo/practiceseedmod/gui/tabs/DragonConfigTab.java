package me.wurgo.practiceseedmod.gui.tabs;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import me.wurgo.practiceseedmod.core.config.ConfigPresets;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;

import java.util.List;

public class DragonConfigTab extends ConfigTab {
    public DragonConfigTab() {
        super("Dragon");
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
                                int index = wrapper.getIntValue("dragonPerchTimeIndex", 0, limit);
                                int value = index + 1;
                                if (value > limit) { value = 0; }
                                wrapper.putIntValue("dragonPerchTimeIndex", value);
                                ConfigPresets.DragonPerchTimes preset = List.of(ConfigPresets.DragonPerchTimes.values()).get(value);
                                b.setMessage(new LiteralText("Dragon Perch Time Preset: " + preset.getCosmeticName()));
                            },
                            (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Cycles through the perch tiers."), i, j)
                    )
            );
            int limit = ConfigPresets.DragonPerchTimes.values().length - 1;
            int value = wrapper.getIntValue("dragonPerchTimeIndex", 0, limit);
            ConfigPresets.DragonPerchTimes preset = List.of(ConfigPresets.DragonPerchTimes.values()).get(value);
            bw.setMessage(new LiteralText("Dragon Perch Time Preset: " + preset.getCosmeticName()));
        }

        this.addButton(
                new ButtonWidget(
                        this.width / 2 - this.btnWidth / 2,
                        this.height / 4 + spacingY,
                        this.btnWidth,
                        this.btnHeight,
                        new LiteralText("Lower Node Height: " + this.getToggleText(this.wrapper.getBoolValue("lowerNodeHeight", true))),
                        b -> b.setMessage(new LiteralText("Lower Node Height: " + this.getToggleText(this.wrapper.inverseBoolValue("lowerNodeHeight", true)))),
                        (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Toggles setting the node height to " + PracticeSeedMod.nodeHeight + "."), i, j)
                )
        );
    }
}
