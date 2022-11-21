package me.wurgo.practiceseedmod.mixin.mechanics.entities;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import me.wurgo.practiceseedmod.core.WorldConstants;
import me.wurgo.practiceseedmod.core.config.ConfigWrapper;
import me.wurgo.practiceseedmod.core.config.ConfigWriter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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

    @Redirect(
            method = "getNearestPathNodeIndex()I",
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/entity/ai/pathing/PathNode"
            )
    )
    private PathNode customPathNodeHeight1(int x, int y, int z) {
        if (new ConfigWrapper(ConfigWriter.INSTANCE).getBoolValue("lowerNodeHeight", true)) {
            return new PathNode(x, PracticeSeedMod.nodeHeight, z);
        }
        return new PathNode(x, y, z);
    }

    @Redirect(
            method = "getNearestPathNodeIndex(DDD)I",
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
