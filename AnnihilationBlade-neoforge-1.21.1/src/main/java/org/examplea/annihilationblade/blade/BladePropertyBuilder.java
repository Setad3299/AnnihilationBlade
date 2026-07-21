package org.examplea.annihilationblade.blade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.component.DataComponents;

public class BladePropertyBuilder {
    private final ItemStack bladeStack;
    private final CompoundTag tag;

    public BladePropertyBuilder(ItemStack bladeStack) {
        this.bladeStack = bladeStack;
        CustomData customData = bladeStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        this.tag = customData.copyTag();
    }

    public static BladePropertyBuilder of(ItemStack bladeStack) {
        return new BladePropertyBuilder(bladeStack);
    }

    public BladePropertyBuilder setAnnihilationBlade(boolean value) {
        tag.putBoolean("IsAnnihilationBlade", value);
        return this;
    }

    public BladePropertyBuilder setKillCount(int count) {
        tag.putInt("KillCount", count);
        return this;
    }

    public BladePropertyBuilder setProudSoul(int soul) {
        tag.putInt("ProudSoul", soul);
        return this;
    }

    public BladePropertyBuilder setModel(String modelName) {
        tag.putString("ModelName", modelName);
        return this;
    }

    public BladePropertyBuilder setTexture(String textureName) {
        tag.putString("TextureName", textureName);
        return this;
    }

    public BladePropertyBuilder setSlashArts(String slashArts) {
        tag.putString("SlashArts", slashArts);
        return this;
    }

    public BladePropertyBuilder setSummonedSwordColor(int color) {
        tag.putInt("SummonedSwordColor", color);
        return this;
    }

    public BladePropertyBuilder hideFlags(int flags) {
        tag.putInt("HideFlags", flags);
        return this;
    }

    public ItemStack build() {
        if (!tag.isEmpty()) {
            bladeStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
        return bladeStack;
    }
}
