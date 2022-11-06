package me.wurgo.practiceseedmod.mixin.mechanics.loot;

import me.wurgo.practiceseedmod.config.ConfigWrapper;
import me.wurgo.practiceseedmod.config.ConfigWriter;
import net.minecraft.loot.UniformLootTableRange;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(UniformLootTableRange.class)
public class UniformLootTableRangeMixin {
    @Shadow @Final private float max;

    @Inject(method = "next", at = @At("RETURN"), cancellable = true)
    private void increaseLuck(Random random, CallbackInfoReturnable<Integer> cir) {
        if (new ConfigWrapper(ConfigWriter.INSTANCE).getBoolValue("guaranteeDrops", true)) {
            cir.setReturnValue((int) this.max);
        }
    }
}
