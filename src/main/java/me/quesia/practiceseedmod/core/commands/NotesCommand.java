package me.quesia.practiceseedmod.core.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.quesia.practiceseedmod.PracticeSeedMod;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class NotesCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("notes").executes(commandContext -> {
            if (PracticeSeedMod.CURRENT_SEED.notes() != null) {
                commandContext.getSource().sendFeedback(new LiteralText(PracticeSeedMod.CURRENT_SEED.notes()), true);
            } else {
                commandContext.getSource().sendFeedback(new LiteralText("There is no description available for this seed!"), false);
            }
            return 1;
        }));
    }
}
