package me.quesia.practiceseedmod.mixin.gui;

import me.quesia.practiceseedmod.PracticeSeedMod;
import me.quesia.practiceseedmod.gui.tabs.QueueConfigTab;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    private ButtonWidget nextSeedButton;

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    private void quit() {
        if (this.client != null) {
            boolean bl = this.client.isInSingleplayer();
            boolean bl2 = this.client.isConnectedToRealms();
            if (this.client.world != null) {
                this.client.world.disconnect();
            }
            if (bl) {
                this.client.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
            } else {
                this.client.disconnect();
            }
            if (bl) {
                this.client.openScreen(new TitleScreen());
            } else if (bl2) {
                RealmsBridge realmsBridge = new RealmsBridge();
                realmsBridge.switchToRealms(new TitleScreen());
            } else {
                this.client.openScreen(new MultiplayerScreen(new TitleScreen()));
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends AbstractButtonWidget> T addButton(T button) {
        if (PracticeSeedMod.RUNNING && this.client != null) {
            if (button.getMessage().equals(new TranslatableText("menu.shareToLan"))) {
                return (T) super.addButton(
                        new ButtonWidget(
                                this.width / 2 + 4,
                                this.height / 4 + 96 - 16,
                                98,
                                20,
                                new LiteralText("View Queue"),
                                b -> this.client.openScreen(new QueueConfigTab(this))
                        )
                );
            }
        }
        return super.addButton(button);
    }

    @Redirect(
            method = "initWidgets",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/GameMenuScreen;addButton(Lnet/minecraft/client/gui/widget/AbstractButtonWidget;)Lnet/minecraft/client/gui/widget/AbstractButtonWidget;",
                    ordinal = 7
            )
    )
    private AbstractButtonWidget addCustomButtons(GameMenuScreen instance, AbstractButtonWidget abstractButtonWidget) {
        if (this.client == null) { return null; }
        int i = -16;

        if (PracticeSeedMod.RUNNING) {
            ButtonWidget bw = this.addButton(
                    new ButtonWidget(
                            this.width / 2 - 102,
                            this.height / 4 + 120 + i,
                            68 - 4,
                            20,
                            new TranslatableText("Save & Quit"),
                            b -> {
                                b.active = false;
                                PracticeSeedMod.RUNNING = false;
                                this.quit();
                            }
                    )
            );
            this.nextSeedButton = this.addButton(
                    new ButtonWidget(
                            this.width / 2 - 102 + 68,
                            this.height / 4 + 120 + i,
                            68,
                            20,
                            new LiteralText("Next Seed"),
                            b -> {
                                b.active = false;
                                PracticeSeedMod.playNextSeed();
                            }
                    )
            );
            this.nextSeedButton.active = !PracticeSeedMod.QUEUE.isEmpty();
            ButtonWidget buttonWidget24 = this.addButton(
                    new ButtonWidget(
                            this.width / 2 - 102 + 68 * 2 + 4,
                            this.height / 4 + 120 + i,
                            68 - 4,
                            20,
                            new LiteralText("Retry Seed"),
                            b -> {
                                if (PracticeSeedMod.CURRENT_SEED != null) {
                                    b.active = false;
                                    PracticeSeedMod.playNextSeed(PracticeSeedMod.CURRENT_SEED);
                                }
                            }
                    )
            );
            buttonWidget24.active = PracticeSeedMod.CURRENT_SEED != null;
            return bw;
        } else {
            ButtonWidget buttonWidget22 = this.addButton(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 120 + i, 204, 20, new TranslatableText("menu.returnToMenu"), buttonWidget -> {
                buttonWidget.active = false;
                this.quit();
            }));
            if (!this.client.isInSingleplayer()) {
                buttonWidget22.setMessage(new TranslatableText("menu.disconnect"));
            }
            return buttonWidget22;
        }
    }

    @Redirect(
            method = "initWidgets",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/ButtonWidget;setMessage(Lnet/minecraft/text/Text;)V"
            )
    )
    private void cancelLabelChange(ButtonWidget instance, Text text) {}

    @Inject(method = "render", at = @At("TAIL"))
    private void updateActiveButtons(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (this.nextSeedButton != null) {
            this.nextSeedButton.active = !PracticeSeedMod.QUEUE.isEmpty();
        }
    }
}
