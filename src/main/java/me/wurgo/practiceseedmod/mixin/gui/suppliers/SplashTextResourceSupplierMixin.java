package me.wurgo.practiceseedmod.mixin.gui.suppliers;

import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SplashTextResourceSupplier.class)
public class SplashTextResourceSupplierMixin {
    @Inject(method = "get", at = @At("RETURN"), cancellable = true)
    private void customSplash(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("Practicing!");
    }
}
