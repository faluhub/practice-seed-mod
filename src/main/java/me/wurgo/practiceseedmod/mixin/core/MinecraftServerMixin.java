package me.wurgo.practiceseedmod.mixin.core;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Shadow @Final protected LevelStorage.Session session;

    @ModifyConstant(method = "shutdown", constant = @Constant(intValue = 0, ordinal = 0))
    private int disableWorldSaving(int constant) {
        return PracticeSeedMod.running ? 1 : constant;
    }

    @Redirect(
            method = "shutdown",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Iterator;hasNext()Z",
                    ordinal = 1
            )
    )
    private boolean closeWorldsRedirect(Iterator<ServerWorld> iterator) {
        if (!PracticeSeedMod.running) {
            return iterator.hasNext();
        }

        new Thread(() -> {
            synchronized (PracticeSeedMod.saveLock) {
                PracticeSeedMod.saving.set(true);

                while (iterator.hasNext()) {
                    ServerWorld world = iterator.next();
                    if (world != null) {
                        try { world.close(); }
                        catch (ConcurrentModificationException | IOException ignored) {}
                    }
                }
                try { this.session.deleteSessionLock(); }
                catch (IllegalStateException | IOException ignored) {}

                PracticeSeedMod.saving.set(false);
            }
        }).start();

        return false;
    }

    @Redirect(method = "shutdown", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;saveAllPlayerData()V"))
    private void disablePlayerSaving(PlayerManager playerManager) {
        if (!PracticeSeedMod.running) {
            playerManager.saveAllPlayerData();
        }
    }
}
