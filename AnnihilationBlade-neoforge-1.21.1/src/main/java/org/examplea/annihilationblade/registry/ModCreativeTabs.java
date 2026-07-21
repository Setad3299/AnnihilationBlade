package org.examplea.annihilationblade.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.examplea.annihilationblade.Annihilationblade;
import org.examplea.annihilationblade.blade.AnnihilationBladeFactory;

public final class ModCreativeTabs {
    private static final ResourceLocation SLASHBLADE_TAB_ID =
            ResourceLocation.fromNamespaceAndPath("slashblade", "slashblade");

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Annihilationblade.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ANNIHILATION_TAB = TABS.register(
            "slashblade_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item.annihilationblade.tab_title"))
                    .icon(ModCreativeTabs::createIcon)
                    .displayItems((parameters, output) -> {
                        ItemStack blade = AnnihilationBladeFactory.getAnnihilationBladeStack(parameters.holders());
                        if (!blade.isEmpty()) output.accept(blade);
                        output.accept(new ItemStack(ModItems.ANNIHILATION_FRAGMENT.get()));
                        output.accept(new ItemStack(ModItems.ANNIHILATION_CORE.get()));
                    })
                    .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
        eventBus.addListener(ModCreativeTabs::addToSlashBladeTab);
    }

    private static void addToSlashBladeTab(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().location().equals(SLASHBLADE_TAB_ID)) return;

        ItemStack blade = AnnihilationBladeFactory.getAnnihilationBladeStack(event.getParameters().holders());
        if (blade.isEmpty()) return;

        var slashbladeItem = BuiltInRegistries.ITEM.get(
                ResourceLocation.fromNamespaceAndPath("slashblade", "slashblade"));
        try {
            if (slashbladeItem != null) {
                event.insertAfter(new ItemStack(slashbladeItem), blade,
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            } else {
                event.accept(blade);
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    private static ItemStack createIcon() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            try {
                var minecraft = net.minecraft.client.Minecraft.getInstance();
                if (minecraft != null && minecraft.level != null) {
                    ItemStack blade = AnnihilationBladeFactory.getAnnihilationBladeStack(
                            minecraft.level.registryAccess());
                    if (!blade.isEmpty()) return blade;
                }
            } catch (Throwable ignored) {
            }
        }
        return AnnihilationBladeFactory.createGodBladeIcon();
    }
}
