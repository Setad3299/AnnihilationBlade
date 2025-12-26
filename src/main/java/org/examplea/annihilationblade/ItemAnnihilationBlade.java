package org.examplea.annihilationblade;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Tiers;

public class ItemAnnihilationBlade extends ItemSlashBlade {
    public ItemAnnihilationBlade() {
        super(Tiers.NETHERITE, 100, -2.4F, new Item.Properties().fireResistant().stacksTo(1));
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        Annihilationblade.applyGodStats(stack);
        return stack;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!stack.hasTag() || !stack.getTag().getBoolean("IsAnnihilationBlade")) {
            Annihilationblade.applyGodStats(stack);
        }
    }
}