package org.examplea.annihilationblade;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Annihilationblade.MODID);

    // 注册物品 ID: "annihilation_blade"
    public static final RegistryObject<Item> ANNIHILATION_BLADE = ITEMS.register("annihilation_blade",
            ItemAnnihilationBlade::new);
    
    // 注册物品 ID: "annihilation_fragment"
    public static final RegistryObject<Item> ANNIHILATION_FRAGMENT = ITEMS.register("annihilation_fragment",
            ItemAnnihilationFragment::new);
    
    // 注册物品 ID: "annihilation_core"
    public static final RegistryObject<Item> ANNIHILATION_CORE = ITEMS.register("annihilation_core",
            ItemAnnihilationCore::new);
}