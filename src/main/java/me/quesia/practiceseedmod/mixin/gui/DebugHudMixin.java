package me.quesia.practiceseedmod.mixin.gui;

import me.quesia.practiceseedmod.PracticeSeedMod;
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
        if (PracticeSeedMod.RUNNING) {
            List<String> list = cir.getReturnValue();
            list.add("");
            if (PracticeSeedMod.IS_RACE) {
                list.add("Race hosted by " + PracticeSeedMod.RACE_HOST);
            } else {
                list.add("Practicing on seed '" + PracticeSeedMod.CURRENT_SEED.seed + "'");
            }
            cir.setReturnValue(list);
        }
    }
}
