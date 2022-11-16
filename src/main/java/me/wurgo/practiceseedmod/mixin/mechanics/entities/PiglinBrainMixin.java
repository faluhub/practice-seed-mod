package me.wurgo.practiceseedmod.mixin.mechanics.entities;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import me.wurgo.practiceseedmod.core.config.ConfigWrapper;
import me.wurgo.practiceseedmod.core.config.ConfigWriter;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {
    @Inject(method = "getBarteredItem", at = @At("RETURN"), cancellable = true)
    private static void barterSeed(PiglinEntity piglin, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (piglin.world.getServer() != null && new ConfigWrapper(ConfigWriter.INSTANCE).getBoolValue("setBarterSeed", true)) {
            ServerWorld world = (ServerWorld) piglin.world;
            LootTable lootTable = world.getServer().getLootManager().getTable(LootTables.PIGLIN_BARTERING_GAMEPLAY);
            cir.setReturnValue(lootTable.generateLoot(new LootContext.Builder(world).parameter(LootContextParameters.THIS_ENTITY, piglin).random(PracticeSeedMod.barteringRandom).build(LootContextTypes.BARTER)));
        }
    }
}
