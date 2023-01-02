package me.wurgo.practiceseedmod.gui.tabs;

import me.wurgo.practiceseedmod.core.WorldConstants;
import me.wurgo.practiceseedmod.gui.LinkUUIDScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;

public class MiscConfigTab extends ConfigTab {
    public MiscConfigTab() {
        super("Misc");
    }

    @Override
    public void addButtons() {
        this.addButton(
                new ButtonWidget(
                        this.width / 2 - this.btnWidth / 2,
                        this.height / 4,
                        this.btnWidth,
                        this.btnHeight,
                        new LiteralText("Always Eye Drops: " + this.getToggleText(this.wrapper.getBoolValue("eyeDrops", true))),
                        b -> b.setMessage(new LiteralText("Always Eye Drops: " + this.getToggleText(this.wrapper.inverseBoolValue("eyeDrops", true)))),
                        (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Toggles if an Eye of Ender is guaranteed to drop."), i, j)
                )
        );

        this.addButton(
                new ButtonWidget(
                        this.width / 2 - this.btnWidth / 2,
                        this.height / 4 + this.spacingY * 2,
                        this.btnWidth,
                        this.btnHeight,
                        new LiteralText("Copy UUID"),
                        b -> this.client.openScreen(new LinkUUIDScreen(this)),
                        (b, matrices, i, j) -> this.renderTooltip(
                                matrices,
                                new LiteralText("Opens the Link UUID screen."),
                                i,
                                j
                        )
                )
        );

        this.addButton(
                new ButtonWidget(
                        this.width / 2 - this.btnWidth / 2,
                        this.height / 4 + this.spacingY * 3,
                        this.btnWidth,
                        this.btnHeight,
                        new LiteralText("Reset World Constants"),
                        b -> {
                            WorldConstants.reset();
                            b.active = false;
                        },
                        (b, matrices, i, j) -> this.renderTooltip(
                                matrices,
                                new LiteralText("Resets the World Constants."),
                                i,
                                j
                        )
                )
        );
    }

    @Override
    public int getButtonAmount() {
        return super.getButtonAmount() + 1;
    }
}
