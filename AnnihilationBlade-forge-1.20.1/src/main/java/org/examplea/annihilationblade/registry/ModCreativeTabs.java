package org.examplea.annihilationblade.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.examplea.annihilationblade.Annihilationblade;
import org.examplea.annihilationblade.blade.AnnihilationBladeFactory;

public final class ModCreativeTabs {
    private static final ResourceKey<CreativeModeTab> SLASHBLADE_TAB_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB,
                    new ResourceLocation("slashblade", "slashblade"));

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Annihilationblade.MODID);

    public static final RegistryObject<CreativeModeTab> ANNIHILATION_TAB = TABS.register(
            "slashblade_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item.annihilationblade.tab_title"))
                    .icon(AnnihilationBladeFactory::createGodBlade)
                    .displayItems((parameters, output) -> {
                        ItemStack blade = AnnihilationBladeFactory.createGodBlade();
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
        if (!SLASHBLADE_TAB_KEY.equals(event.getTabKey())) return;

        ItemStack blade = AnnihilationBladeFactory.createGodBlade();
        if (!blade.isEmpty()) event.accept(blade);
    }
}
