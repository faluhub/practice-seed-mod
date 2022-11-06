package me.wurgo.practiceseedmod.mixin.core.gui;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
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

        if (PracticeSeedMod.running) {
            ButtonWidget bw = this.addButton(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 120 + i, 68 - 4, 20, new TranslatableText("Save & Quit"), buttonWidget -> {
                buttonWidget.active = false;
                PracticeSeedMod.running = false;
                this.quit();
            }));
            ButtonWidget buttonWidget23 = this.addButton(
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
            buttonWidget23.active = PracticeSeedMod.queue.size() > 0;
            ButtonWidget buttonWidget24 = this.addButton(
                    new ButtonWidget(
                            this.width / 2 - 102 + 68 * 2 + 4,
                            this.height / 4 + 120 + i,
                            68 - 4,
                            20,
                            new LiteralText("Retry Seed"),
                            b -> {
                                if (PracticeSeedMod.currentSeed != null) {
                                    b.active = false;
                                    PracticeSeedMod.playNextSeed(PracticeSeedMod.currentSeed);
                                }
                            }
                    )
            );
            buttonWidget24.active = PracticeSeedMod.currentSeed != null;
            return bw;
        } else {
            ButtonWidget buttonWidget22 = this.addButton(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 120 + -16, 204, 20, new TranslatableText("menu.returnToMenu"), buttonWidget -> {
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
}
