package me.wurgo.practiceseedmod.mixin.mechanics.entities;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import me.wurgo.practiceseedmod.core.config.ConfigWrapper;
import me.wurgo.practiceseedmod.core.config.ConfigWriter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends HostileEntity {
    protected EndermanEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void dropLoot(DamageSource source, boolean causedByPlayer) {
        if (PracticeSeedMod.running && new ConfigWrapper(ConfigWriter.INSTANCE).getBoolValue("guaranteeDrops", true)) {
            this.dropStack(new ItemStack(Items.ENDER_PEARL));
            return;
        }
        super.dropLoot(source, causedByPlayer);
    }
}
