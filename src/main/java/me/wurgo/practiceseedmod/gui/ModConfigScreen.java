package me.wurgo.practiceseedmod.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class ModConfigScreen extends Screen {
    public ModConfigScreen() {
        super(new LiteralText("Configure Mod:"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 13, 16777215);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
