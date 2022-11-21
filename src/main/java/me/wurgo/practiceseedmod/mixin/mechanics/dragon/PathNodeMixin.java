package me.wurgo.practiceseedmod.mixin.mechanics.dragon;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import me.wurgo.practiceseedmod.core.config.ConfigWrapper;
import me.wurgo.practiceseedmod.core.config.ConfigWriter;
import net.minecraft.entity.ai.pathing.PathNode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PathNode.class)
public class PathNodeMixin {
    @Mutable
    @Shadow @Final public int y;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void editPathY(int x, int y, int z, CallbackInfo ci) {
        if (new ConfigWrapper(ConfigWriter.INSTANCE).getBoolValue("lowerNodeHeight", true)) {
            this.y = PracticeSeedMod.nodeHeight;
        }
    }
}
