package me.wurgo.practiceseedmod.mixin.mechanics.blocks;

import me.wurgo.practiceseedmod.PracticeSeedMod;
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

    @SuppressWarnings("deprecation")
    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        List<ItemStack> original = super.getDroppedStacks(state, builder);
        int amount = 0;
        if (original.get(0).getItem().equals(Items.FLINT)) {
            amount = original.get(0).getCount();
            if (amount != 0) { amount--; }
        }
        if (PracticeSeedMod.gravelDropRandom.nextInt(2) == 0) { amount++; }
        if (amount > 0) {
            return List.of(new ItemStack(Items.FLINT, amount));
        }
        return original;
    }
}
