package me.wurgo.practiceseedmod.mixin.gui;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin {
    @Shadow protected abstract void createLevel();

    @Inject(method = "tick", at = @At("TAIL"))
    private void autoCreate(CallbackInfo ci) {
        if (PracticeSeedMod.running) {
            this.createLevel();
        }
    }
}
