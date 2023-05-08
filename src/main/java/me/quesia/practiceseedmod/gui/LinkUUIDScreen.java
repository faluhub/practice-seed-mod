package me.quesia.practiceseedmod.gui;

import me.quesia.practiceseedmod.PracticeSeedMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.UUID;

public class LinkUUIDScreen extends Screen {
    private final Screen parent;
    private boolean hasCopied = false;
    private boolean hasTyped = false;
    private ButtonWidget copyButton;
    private TextFieldWidget passwordField;

    public LinkUUIDScreen(Screen parent) {
        super(new LiteralText("Link UUID:"));

        PracticeSeedMod.UUID = UUID.randomUUID();
        this.parent = parent;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        int buttonWidth = 150;

        this.copyButton = this.addButton(
                new ButtonWidget(
                        this.width / 2 - buttonWidth / 2,
                        this.height / 2 + 25,
                        buttonWidth,
                        20,
                        new LiteralText("Copy UUID"),
                        b -> {
                            if (this.client != null) {
                                if (!this.hasCopied && !this.hasTyped) {
                                    this.client.keyboard.setClipboard(PracticeSeedMod.UUID.toString());
                                    this.hasCopied = true;
                                } else {
                                    this.onClose();
                                }
                            }
                        }
                )
        );

        this.passwordField = new TextFieldWidget(
                this.textRenderer,
                this.width / 2 - buttonWidth / 2,
                this.height / 2,
                buttonWidth,
                20,
                new LiteralText("")
        );
        this.passwordField.setMaxLength(50);
        this.addChild(this.passwordField);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 13, 16777215);

        this.drawCenteredString(
                matrices,
                this.textRenderer,
                "Race Password (Optional)",
                this.width / 2,
                this.height / 2 - 15,
                16777215
        );

        this.copyButton.setMessage(this.hasCopied || this.hasTyped ? ScreenTexts.DONE : new LiteralText("Copy UUID"));
        this.hasTyped = this.passwordField.getText().length() > 0;
        this.passwordField.setEditable(!this.hasCopied);
        if (this.passwordField.isFocused() && this.hasCopied) {
            this.passwordField.setSelected(false);
        }

        this.passwordField.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (this.hasTyped) {
            PracticeSeedMod.IS_RACE = true;
            PracticeSeedMod.RACE_PASSWORD = this.passwordField.getText();
            PracticeSeedMod.UUID = null;
            PracticeSeedMod.log("Linked Multiplayer: " + PracticeSeedMod.RACE_PASSWORD);
        } else {
            PracticeSeedMod.IS_RACE = false;
            PracticeSeedMod.RACE_PASSWORD = null;
            PracticeSeedMod.log("Linked UUID: " + PracticeSeedMod.UUID);
        }

        if (this.client != null) {
            this.client.openScreen(this.parent);
        }
    }
}
