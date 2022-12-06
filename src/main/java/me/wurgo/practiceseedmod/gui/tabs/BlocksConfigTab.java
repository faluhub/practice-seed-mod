package me.wurgo.practiceseedmod.gui.tabs;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;

public class BlocksConfigTab extends ConfigTab {
    private final int gravelDef = 1;
    private final int gravelLimit = 10;

    public BlocksConfigTab() {
        super("Blocks");
    }

    @Override
    public void addButtons() {
        {
            this.addButton(
                    new ButtonWidget(
                            this.width / 2 - this.btnWidth / 2,
                            this.height / 4,
                            this.btnWidth / 2,
                            this.btnHeight,
                            new LiteralText("Flint: " + this.wrapper.getIntValue("gravelDropFlint", this.gravelDef, this.gravelLimit)),
                            b -> {
                                int flint = this.wrapper.getIntValue("gravelDropFlint", this.gravelDef, this.gravelLimit);
                                int gravel = this.wrapper.getIntValue("gravelDropGravel", this.gravelDef, this.gravelLimit);
                                int value = this.compareAB(flint, gravel, this.gravelLimit);
                                this.wrapper.putIntValue("gravelDropFlint", value);
                                b.setMessage(new LiteralText("Flint: " + value));
                            },
                            (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Shift + Click to decrease."), i, j)
                    )
            );

            {
                ButtonWidget bw2 = this.addButton(
                        new ButtonWidget(
                                this.width / 2,
                                this.height / 4,
                                this.btnWidth / 2,
                                this.btnHeight,
                                new LiteralText(""),
                                b -> {
                                    int gravel = this.wrapper.getIntValue("gravelDropGravel", this.gravelDef, this.gravelLimit);
                                    int flint = this.wrapper.getIntValue("gravelDropFlint", this.gravelDef, this.gravelLimit);
                                    int value = this.compareBA(gravel, flint, this.gravelLimit);
                                    this.wrapper.putIntValue("gravelDropGravel", value);
                                    b.setMessage(new LiteralText("Gravel: " + value));
                                },
                                (b, matrices, i, j) -> this.renderTooltip(matrices, new LiteralText("Shift + Click to decrease."), i, j)
                        )
                );
                int gravel = this.wrapper.getIntValue("gravelDropGravel", this.gravelDef, this.gravelLimit);
                bw2.setMessage(
                        gravel == -1
                                ? new LiteralText("Gravel: Random")
                                : new LiteralText("Gravel: " + gravel)
                );
            }
        }
    }

    @Override
    public int getButtonAmount() {
        return super.getButtonAmount() - 1;
    }
}
