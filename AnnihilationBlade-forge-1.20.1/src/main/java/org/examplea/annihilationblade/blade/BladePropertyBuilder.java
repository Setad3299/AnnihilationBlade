package org.examplea.annihilationblade.blade;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * 神刀属性注入工具。
 * 通过拔刀剑 Capability 接口写入所有状态，不直接操作 raw NBT key，
 * 确保属性被拔刀剑正确读取并序列化。
 */
public class BladePropertyBuilder {
    private final ItemStack bladeStack;

    public BladePropertyBuilder(ItemStack bladeStack) {
        this.bladeStack = bladeStack;
    }

    public static BladePropertyBuilder of(ItemStack bladeStack) {
        return new BladePropertyBuilder(bladeStack);
    }

    /**
     * 设置我们 mod 自定义的 IsAnnihilationBlade 标记。
     * 这是我们自己的识别标签，不属于拔刀剑的 Capability，仍直接写 NBT。
     */
    public BladePropertyBuilder setAnnihilationBlade(boolean value) {
        bladeStack.getOrCreateTag().putBoolean("IsAnnihilationBlade", value);
        return this;
    }

    public BladePropertyBuilder setKillCount(int count) {
        bladeStack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(s -> s.setKillCount(count));
        return this;
    }

    public BladePropertyBuilder setProudSoul(int soul) {
        bladeStack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(s -> s.setProudSoulCount(soul));
        return this;
    }

    public BladePropertyBuilder setModel(String modelName) {
        bladeStack.getCapability(ItemSlashBlade.BLADESTATE)
                .ifPresent(s -> s.setModel(new ResourceLocation(modelName)));
        return this;
    }

    public BladePropertyBuilder setTexture(String textureName) {
        bladeStack.getCapability(ItemSlashBlade.BLADESTATE)
                .ifPresent(s -> s.setTexture(new ResourceLocation(textureName)));
        return this;
    }

    public BladePropertyBuilder setSlashArts(String slashArts) {
        bladeStack.getCapability(ItemSlashBlade.BLADESTATE)
                .ifPresent(s -> s.setSlashArtsKey(new ResourceLocation(slashArts)));
        return this;
    }

    public BladePropertyBuilder setDefaultBewitched(boolean bewitched) {
        bladeStack.getCapability(ItemSlashBlade.BLADESTATE)
                .ifPresent(s -> s.setDefaultBewitched(bewitched));
        return this;
    }

    public BladePropertyBuilder setSealed(boolean sealed) {
        bladeStack.getCapability(ItemSlashBlade.BLADESTATE)
                .ifPresent(s -> s.setSealed(sealed));
        return this;
    }

    /**
     * 设置刀的显示名称（翻译键）。
     * SlashBlade 的 getTranslationKey 格式为 Util.makeDescriptionId("item", rl)，
     * 即 "item.namespace.name"，直接传入已转换好的 key。
     */
    public BladePropertyBuilder setTranslationKey(String translationKey) {
        bladeStack.getCapability(ItemSlashBlade.BLADESTATE)
                .ifPresent(s -> s.setTranslationKey(translationKey));
        return this;
    }

    /**
     * 设置召唤剑颜色。
     * ISlashBladeState 提供 setColorCode(int) default 方法，内部转换为 Color。
     */
    public BladePropertyBuilder setSummonedSwordColor(int argbColor) {
        bladeStack.getCapability(ItemSlashBlade.BLADESTATE)
                .ifPresent(s -> s.setColorCode(argbColor));
        return this;
    }

    public BladePropertyBuilder addEnchantment(Enchantment enchantment, int level) {
        bladeStack.enchant(enchantment, level);
        return this;
    }

    public BladePropertyBuilder hideFlags(int flags) {
        bladeStack.getOrCreateTag().putInt("HideFlags", flags);
        return this;
    }

    public ItemStack build() {
        return bladeStack;
    }
}
