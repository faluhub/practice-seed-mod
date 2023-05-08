package me.quesia.practiceseedmod.mixin.mechanics.entities.dragon;

import me.quesia.practiceseedmod.core.config.ConfigWrapper;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(EnderDragonFight.class)
public class EnderDragonFightMixin {
    @Redirect(method = "createDragon", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;refreshPositionAndAngles(DDDFF)V"))
    private void chooseAngle(EnderDragonEntity instance, double x, double y, double z, float yaw, float pitch) {
        if (new ConfigWrapper().getBoolValue("noEarlyFlyaway", true)) {
            instance.refreshPositionAndAngles(x, y, z, (Objects.requireNonNull(instance.world.getServer()).getOverworld().getSeed() ^ 579723852935L) % 360, pitch);
        }
    }
}
