package me.quesia.practiceseedmod.mixin.gui;

import me.quesia.practiceseedmod.PracticeSeedMod;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin {
    @Shadow protected abstract void createLevel();
    @Shadow @Final public MoreOptionsDialog moreOptionsDialog;
    @Shadow public boolean hardcore;

    @Inject(method = "tick", at = @At("TAIL"))
    private void autoCreate(CallbackInfo ci) {
        if (PracticeSeedMod.RUNNING) {
            this.createLevel();
        }
    }

    @Inject(method = "createLevel", at = @At("HEAD"))
    private void initLevelData(CallbackInfo ci) {
        PracticeSeedMod.initialiseLevelData(this.moreOptionsDialog.getGeneratorOptions(this.hardcore).getSeed());
    }
}
