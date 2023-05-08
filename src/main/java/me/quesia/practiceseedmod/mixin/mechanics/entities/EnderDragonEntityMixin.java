package me.quesia.practiceseedmod.mixin.mechanics.entities;

import me.quesia.practiceseedmod.PracticeSeedMod;
import me.quesia.practiceseedmod.core.WorldConstants;
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
        if (WorldConstants.END_ENTER_TIMESTAMP == null) {
            WorldConstants.END_ENTER_TIMESTAMP = Instant.now(Clock.systemUTC());
        }
    }

    @Override
    public void tick() {
        if (WorldConstants.END_ENTER_TIMESTAMP != null && !WorldConstants.HAS_PERCHED) {
            long seconds = Duration.between(WorldConstants.END_ENTER_TIMESTAMP, Instant.now(Clock.systemUTC())).getSeconds();
            if (seconds > PracticeSeedMod.MAXIMUM_PERCH_SECONDS) {
                PracticeSeedMod.log("Forcing dragon to perch after " + seconds + " seconds.");
                this.getPhaseManager().setPhase(PhaseType.LANDING_APPROACH);
                WorldConstants.HAS_PERCHED = true;
            }
        }

        super.tick();
    }
}
