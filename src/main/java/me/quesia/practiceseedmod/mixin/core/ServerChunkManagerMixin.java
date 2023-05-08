package me.quesia.practiceseedmod.mixin.core;

import me.quesia.practiceseedmod.PracticeSeedMod;
import net.minecraft.server.world.ServerChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerChunkManager.class)
public class ServerChunkManagerMixin {
    @Redirect(method = "close", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;save(Z)V"))
    private void closeRedirect(ServerChunkManager serverChunkManager, boolean flush) {
        if (!PracticeSeedMod.RUNNING) {
            serverChunkManager.save(flush);
        }
    }
}
