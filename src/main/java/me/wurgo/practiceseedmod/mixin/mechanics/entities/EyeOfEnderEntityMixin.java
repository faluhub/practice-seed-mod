package me.wurgo.practiceseedmod.mixin.mechanics.entities;

import me.wurgo.practiceseedmod.core.config.ConfigWrapper;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EyeOfEnderEntity.class)
public class EyeOfEnderEntityMixin {
    @Shadow private boolean dropsItem;

    @Inject(method = "moveTowards", at = @At("TAIL"))
    private void breakEye(BlockPos pos, CallbackInfo ci) {
        if (new ConfigWrapper().getBoolValue("eyeDrops", true)) {
            this.dropsItem = true;
        }
    }
}
