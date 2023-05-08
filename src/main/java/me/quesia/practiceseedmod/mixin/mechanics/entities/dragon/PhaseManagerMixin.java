package me.quesia.practiceseedmod.mixin.mechanics.entities.dragon;

import me.quesia.practiceseedmod.PracticeSeedMod;
import me.quesia.practiceseedmod.core.WorldConstants;
import me.quesia.practiceseedmod.core.config.ConfigPresets;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Mixin(PhaseManager.class)
public abstract class PhaseManagerMixin {
    @Shadow public abstract void setPhase(PhaseType<?> type);
    private boolean isIlluminaPreset = false;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initialisePreset(EnderDragonEntity dragon, CallbackInfo ci) {
        if (PracticeSeedMod.MAXIMUM_PERCH_SECONDS == ConfigPresets.DragonPerchTimes.ILLUMINA.getSeconds()) {
            this.isIlluminaPreset = true;
        }
    }

    @Inject(method = "setPhase", at = @At("HEAD"), cancellable = true)
    private void illuminaEasterEgg(PhaseType<?> type, CallbackInfo ci) {
        if (type.equals(PhaseType.LANDING_APPROACH)) {
            if (this.isIlluminaPreset) {
                if (Duration.between(Instant.now(Clock.systemUTC()), WorldConstants.END_ENTER_TIMESTAMP).getSeconds() < PracticeSeedMod.MAXIMUM_PERCH_SECONDS) {
                    this.setPhase(PhaseType.HOLDING_PATTERN);
                    ci.cancel();
                }
            }
        }
    }
}
