package me.wurgo.practiceseedmod.mixin.mechanics.entities;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import me.wurgo.practiceseedmod.core.WorldConstants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin extends MobEntity {
    @Shadow public abstract PhaseManager getPhaseManager();

    protected EnderDragonEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initialiseTimestamp(EntityType<? extends MobEntity> entityType, World world, CallbackInfo ci) {
        if (WorldConstants.endEnterTimestamp == null) {
            WorldConstants.endEnterTimestamp = Instant.now(Clock.systemUTC());
        }
    }

    @Override
    public void tick() {
        if (WorldConstants.endEnterTimestamp != null && !WorldConstants.hasPerched) {
            long seconds = Duration.between(WorldConstants.endEnterTimestamp, Instant.now(Clock.systemUTC())).getSeconds();
            if (seconds > PracticeSeedMod.maximumPerchSeconds) {
                PracticeSeedMod.log("Forcing dragon to perch after " + seconds + " seconds.");
                this.getPhaseManager().setPhase(PhaseType.LANDING_APPROACH);
                WorldConstants.hasPerched = true;
            }
        }

        super.tick();
    }
}
