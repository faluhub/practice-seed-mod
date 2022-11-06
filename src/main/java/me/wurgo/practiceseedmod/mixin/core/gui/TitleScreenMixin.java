package me.wurgo.practiceseedmod.mixin.core.gui;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void playNextSeed(CallbackInfo ci) {
        PracticeSeedMod.currentSeed = null;
        if (PracticeSeedMod.playNextSeed()) {
            ci.cancel();
        } else {
            PracticeSeedMod.running = false;
        }
    }
}
