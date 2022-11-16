package me.wurgo.practiceseedmod.mixin.core;

import com.mojang.brigadier.CommandDispatcher;
import me.wurgo.practiceseedmod.PracticeSeedMod;
import me.wurgo.practiceseedmod.core.commands.NotesCommand;
import net.minecraft.server.command.AdvancementCommand;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommandManager.class)
public class CommandManagerMixin {
    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/command/AdvancementCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V"
            )
    )
    private void registerDescriptionCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        AdvancementCommand.register(dispatcher);
        if (PracticeSeedMod.running) {
            NotesCommand.register(dispatcher);
        }
    }
}
