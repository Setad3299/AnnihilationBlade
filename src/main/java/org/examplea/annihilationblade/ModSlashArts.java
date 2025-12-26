package org.examplea.annihilationblade;

import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModSlashArts {
    public static final DeferredRegister<SlashArts> ARTS = DeferredRegister.create(new ResourceLocation("slashblade", "slash_arts"), Annihilationblade.MODID);

    public static final RegistryObject<SlashArts> SPATIAL_FRACTURE = ARTS.register("spatial_fracture", () ->
            new SlashArts(
                    // 链接到我们在 ModComboStates 里定义的动作
                    (entity) -> ModComboStates.SPATIAL_FRACTURE_STATE.getId()
            )
    );

    public static void register(IEventBus eventBus) {
        ARTS.register(eventBus);
    }
}