package org.examplea.annihilationblade.mixin;

import mods.flammpfeil.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.examplea.annihilationblade.Annihilationblade;
import org.examplea.annihilationblade.blade.AnnihilationBladeFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 在 SlashBlade 根据 JSON 创建湮灭之刃后补充运行时属性。 */
@Mixin(value = SlashBladeDefinition.class, remap = false)
public class MixinSlashBladeDefinition {

    private static final ResourceLocation ANNIHILATION_BLADE_ID =
            ResourceLocation.fromNamespaceAndPath(Annihilationblade.MODID, "annihilation_blade");

    @Inject(method = "getBlade(Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("RETURN"),
            cancellable = false,
            remap = false)
    private void injectGodStats(HolderLookup.Provider provider,
                                CallbackInfoReturnable<ItemStack> cir) {
        SlashBladeDefinition self = (SlashBladeDefinition) (Object) this;
        ResourceLocation name = self.getName();
        if (ANNIHILATION_BLADE_ID.equals(name)) {
            ItemStack result = cir.getReturnValue();
            if (result != null && !result.isEmpty()) {
                AnnihilationBladeFactory.applyGodStats(result);
            }
        }
    }

    @Inject(method = "getBlade(Lnet/minecraft/world/item/Item;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("RETURN"),
            cancellable = false,
            remap = false)
    private void injectGodStatsItem(Item item, HolderLookup.Provider provider,
                                    CallbackInfoReturnable<ItemStack> cir) {
        SlashBladeDefinition self = (SlashBladeDefinition) (Object) this;
        ResourceLocation name = self.getName();
        if (ANNIHILATION_BLADE_ID.equals(name)) {
            ItemStack result = cir.getReturnValue();
            if (result != null && !result.isEmpty()) {
                AnnihilationBladeFactory.applyGodStats(result);
            }
        }
    }
}
