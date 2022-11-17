package me.wurgo.practiceseedmod.mixin.gui;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    @Inject(method = "getRightText", at = @At("RETURN"), cancellable = true)
    private void addDebugText(CallbackInfoReturnable<List<String>> cir) {
        if (PracticeSeedMod.running) {
            List<String> list = cir.getReturnValue();
            list.add("");
            if (PracticeSeedMod.isRace) {
                list.add("Race hosted by " + PracticeSeedMod.raceHost);
                list.add("Racing on seed '" + PracticeSeedMod.currentSeed + "'");
            } else {
                list.add("Practicing on seed '" + PracticeSeedMod.currentSeed + "'");
            }
            cir.setReturnValue(list);
        }
    }
}
