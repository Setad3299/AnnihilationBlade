package org.examplea.annihilationblade.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.examplea.annihilationblade.Annihilationblade;
import org.examplea.annihilationblade.item.ItemAnnihilationCore;
import org.examplea.annihilationblade.item.ItemAnnihilationFragment;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Annihilationblade.MODID);

    public static final RegistryObject<Item> ANNIHILATION_FRAGMENT = ITEMS.register("annihilation_fragment",
            ItemAnnihilationFragment::new);

    public static final RegistryObject<Item> ANNIHILATION_CORE = ITEMS.register("annihilation_core",
            ItemAnnihilationCore::new);
}
