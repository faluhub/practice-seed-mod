package me.wurgo.practiceseedmod.mixin.mechanics.entities;

import me.wurgo.practiceseedmod.PracticeSeedMod;
import me.wurgo.practiceseedmod.config.ConfigWrapper;
import me.wurgo.practiceseedmod.config.ConfigWriter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "dropLoot", at = @At("HEAD"), cancellable = true)
    private void dropBlazeRod(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        if (PracticeSeedMod.running && new ConfigWrapper(ConfigWriter.INSTANCE).getBoolValue("guaranteeDrops", true)) {
            if (this.getType().equals(EntityType.ENDERMAN)) { this.dropStack(new ItemStack(Items.ENDER_PEARL)); }
            else if (this.getType().equals(EntityType.BLAZE)) { this.dropStack(new ItemStack(Items.BLAZE_ROD)); }
            else { return; }
            ci.cancel();
        }
    }
}
