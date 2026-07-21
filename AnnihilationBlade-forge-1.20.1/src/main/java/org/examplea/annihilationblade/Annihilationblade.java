package org.examplea.annihilationblade;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.examplea.annihilationblade.registry.ModComboStates;
import org.examplea.annihilationblade.registry.ModCreativeTabs;
import org.examplea.annihilationblade.registry.ModItems;
import org.examplea.annihilationblade.registry.ModSlashArts;

@Mod(Annihilationblade.MODID)
public class Annihilationblade {
    public static final String MODID = "annihilationblade";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Annihilationblade() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        ModSlashArts.register(modEventBus);
        ModComboStates.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
    }

}
