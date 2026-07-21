package org.examplea.annihilationblade.registry;

import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.resources.ResourceKey; // 完全校准为正确的资源包
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.examplea.annihilationblade.Annihilationblade;

public class ModSlashArts {
    private static final ResourceKey<net.minecraft.core.Registry<SlashArts>> SLASH_ARTS_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("slashblade", "slash_arts"));

    public static final DeferredRegister<SlashArts> ARTS = DeferredRegister.create(SLASH_ARTS_REGISTRY_KEY, Annihilationblade.MODID);

    public static final DeferredHolder<SlashArts, SlashArts> SPATIAL_FRACTURE = ARTS.register("spatial_fracture", () ->
            new SlashArts(
                    (entity) -> ModComboStates.SPATIAL_FRACTURE_STATE.getId()
            )
    );

    public static void register(IEventBus eventBus) {
        ARTS.register(eventBus);
    }
}
