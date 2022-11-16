package me.wurgo.practiceseedmod.mixin.mechanics.blocks;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import me.wurgo.practiceseedmod.core.WorldConstants;
import me.wurgo.practiceseedmod.core.config.ConfigWrapper;
import me.wurgo.practiceseedmod.core.config.ConfigWriter;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.GravelBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(GravelBlock.class)
public abstract class GravelBlockMixin extends FallingBlock {
    public GravelBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        if (PracticeSeedMod.running && !WorldConstants.hasDroppedFlint && new ConfigWrapper(ConfigWriter.INSTANCE).getBoolValue("firstTryFlint", true)) {
            WorldConstants.hasDroppedFlint = true;
            return List.of(new ItemStack(Items.FLINT));
        }

        return super.getDroppedStacks(state, builder);
    }
}
