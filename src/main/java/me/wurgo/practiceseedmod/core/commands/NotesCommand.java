package me.wurgo.practiceseedmod.core.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.wurgo.practiceseedmod.PracticeSeedMod;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class NotesCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("notes").executes(commandContext -> {
            if (PracticeSeedMod.seedNotes != null) {
                commandContext.getSource().sendFeedback(new LiteralText(PracticeSeedMod.seedNotes), true);
            } else {
                commandContext.getSource().sendFeedback(new LiteralText("There is no description available for this seed!"), false);
            }
            return 1;
        }));
    }
}
