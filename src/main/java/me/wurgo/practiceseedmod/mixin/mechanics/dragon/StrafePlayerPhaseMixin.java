package me.wurgo.practiceseedmod.mixin.mechanics.dragon;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import me.wurgo.practiceseedmod.core.config.ConfigWrapper;
import me.wurgo.practiceseedmod.core.config.ConfigWriter;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.boss.dragon.phase.StrafePlayerPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StrafePlayerPhase.class)
public class StrafePlayerPhaseMixin {
    @Redirect(
            method = "method_6862",
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/entity/ai/pathing/PathNode"
            )
    )
    private PathNode customPathNodeHeight2(int x, int y, int z) {
        if (new ConfigWrapper(ConfigWriter.INSTANCE).getBoolValue("lowerNodeHeight", true)) {
            return new PathNode(x, PracticeSeedMod.nodeHeight, z);
        }
        return new PathNode(x, y, z);
    }
}
