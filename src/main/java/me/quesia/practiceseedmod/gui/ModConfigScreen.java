package me.quesia.practiceseedmod.gui;

import me.quesia.practiceseedmod.gui.tabs.ConfigTab;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class ModConfigScreen extends Screen {
    public ModConfigScreen() {
        super(new LiteralText("Mod Config"));
    }

    @Override
    protected void init() {
        if (this.client == null) { return; }

        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacingY = 24;

        int i = 1;
        for (ConfigTab.Tabs tab : ConfigTab.Tabs.values()) {
            this.addButton(
                    new ButtonWidget(
                            this.width / 2 - buttonWidth / 2,
                            this.height / 4 + spacingY * i,
                            buttonWidth,
                            buttonHeight,
                            new LiteralText(tab.getName()),
                            b -> {
                                try {
                                    this.client.openScreen(tab.getScreen().getDeclaredConstructor().newInstance());
                                } catch (ReflectiveOperationException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    )
            );
            i++;
        }

        this.addButton(
                new ButtonWidget(
                        this.width / 2 - buttonWidth / 2,
                        this.height / 4 + spacingY * (this.buttons.size() + 2   ),
                        buttonWidth,
                        buttonHeight,
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

        super.render(matrices, mouseX, mouseY, delta);
    }
}
