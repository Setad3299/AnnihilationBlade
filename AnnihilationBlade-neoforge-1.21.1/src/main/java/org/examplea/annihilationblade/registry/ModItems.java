package org.examplea.annihilationblade.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.examplea.annihilationblade.Annihilationblade;
import org.examplea.annihilationblade.item.ItemAnnihilationCore;
import org.examplea.annihilationblade.item.ItemAnnihilationFragment;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Annihilationblade.MODID);

    public static final DeferredHolder<Item, Item> ANNIHILATION_FRAGMENT = ITEMS.register("annihilation_fragment",
            () -> new ItemAnnihilationFragment());

    public static final DeferredHolder<Item, Item> ANNIHILATION_CORE = ITEMS.register("annihilation_core",
            () -> new ItemAnnihilationCore());
}
