package me.wurgo.practiceseedmod.gui;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.UUID;

public class LinkUUIDScreen extends Screen {
    private final Screen parent;

    public LinkUUIDScreen(Screen parent) {
        super(new LiteralText("Link UUID:"));

        PracticeSeedMod.uuid = UUID.randomUUID();
        this.parent = parent;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        int buttonWidth = 100;

        this.addButton(
                new ButtonWidget(
                        this.width / 2 - buttonWidth / 2,
                        this.height / 2,
                        buttonWidth,
                        20,
                        new LiteralText("Copy UUID"),
                        b -> {
                            if (this.client != null) {
                                if (b.getMessage().getString().equals("Copy UUID")) {
                                    this.client.keyboard.setClipboard(PracticeSeedMod.uuid.toString());
                                    b.setMessage(ScreenTexts.DONE);
                                } else {
                                    this.client.openScreen(null);
                                }
                            }
                        }
                )
        );

        super.init();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 13, 16777215);

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (this.client != null) {
            this.client.openScreen(this.parent);
        }
    }
}
