package org.examplea.annihilationblade.blade;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.Unbreakable;
import org.examplea.annihilationblade.Annihilationblade;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class AnnihilationBladeFactory {
    private static final ResourceLocation BLADE_ID =
            ResourceLocation.fromNamespaceAndPath(Annihilationblade.MODID, "annihilation_blade");

    private AnnihilationBladeFactory() {
    }

    /** 从 SlashBlade 动态注册表创建数据驱动的湮灭之刃。 */
    public static ItemStack getAnnihilationBladeStack(HolderLookup.Provider registryAccess) {
        if (registryAccess != null) {
            try {
                Class<?> definitionClass = Class.forName(
                        "mods.flammpfeil.slashblade.registry.slashblade.SlashBladeDefinition");
                Field registryKeyField = definitionClass.getDeclaredField("REGISTRY_KEY");
                registryKeyField.setAccessible(true);

                @SuppressWarnings("unchecked")
                ResourceKey<Registry<Object>> registryKey =
                        (ResourceKey<Registry<Object>>) registryKeyField.get(null);
                ResourceKey<Object> bladeKey = ResourceKey.create(registryKey, BLADE_ID);

                var lookup = registryAccess.lookup(registryKey);
                if (lookup.isPresent()) {
                    var holder = lookup.get().get(bladeKey);
                    if (holder.isPresent()) {
                        Method getBlade = definitionClass.getMethod("getBlade", HolderLookup.Provider.class);
                        ItemStack result = (ItemStack) getBlade.invoke(holder.get().value(), registryAccess);
                        if (result != null && !result.isEmpty()) {
                            applyGodStats(result);
                            return result;
                        }
                    }
                }
            } catch (Throwable exception) {
                Annihilationblade.LOGGER.error("[AnnihilationBlade] 无法从 SlashBlade 注册表创建湮灭之刃", exception);
            }
        }

        return createGodBladeIcon();
    }

    /** 创建不依赖动态注册表的创造栏图标。 */
    public static ItemStack createGodBladeIcon() {
        var slashbladeItem = BuiltInRegistries.ITEM.get(
                ResourceLocation.fromNamespaceAndPath("slashblade", "slashblade"));
        if (slashbladeItem == null) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = new ItemStack(slashbladeItem);
        applyGodStats(stack);
        stack.set(DataComponents.CUSTOM_NAME,
                Component.translatable("item.annihilationblade.annihilation_blade"));
        return stack;
    }

    /** 写入湮灭之刃的运行时标识和 SlashBlade 属性。 */
    public static void applyGodStats(ItemStack stack) {
        if (stack.isEmpty()) return;

        stack.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
        stack.set(DataComponents.DAMAGE, 0);

        BladePropertyBuilder.of(stack)
                .setAnnihilationBlade(true)
                .setKillCount(10000)
                .setProudSoul(100000)
                .setSummonedSwordColor(0xFFAA00FF)
                .setSlashArts("annihilationblade:spatial_fracture")
                .setModel("annihilationblade:model/blade.obj")
                .setTexture("annihilationblade:model/blade.png")
                .hideFlags(2)
                .build();

        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.putString("ShareId", BLADE_ID.toString());
            tag.putString("id", BLADE_ID.toString());
            tag.putString("translationKey", "item.annihilationblade.annihilation_blade");
        });
    }
}
