package org.examplea.annihilationblade;

import com.mojang.logging.LogUtils;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import mods.flammpfeil.slashblade.init.SBItems;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Map;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod(Annihilationblade.MODID)
public class Annihilationblade {
    public static final String MODID = "annihilationblade";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // 注册自定义标签页
    public static final RegistryObject<CreativeModeTab> SLASHBLADE_TAB = CREATIVE_MODE_TABS.register("slashblade_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item.annihilationblade.tab_title"))
                    .icon(() -> {
                        ItemStack stack = new ItemStack(SBItems.slashblade);
                        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(s -> {
                            s.setModel(new ResourceLocation(MODID, "model/blade.obj"));
                            s.setTexture(new ResourceLocation(MODID, "model/blade.png"));
                        });
                        return stack;
                    })
                    .displayItems((parameters, output) -> {
                        // === 修复：自定义标签页 ===
                        // 从管理器获取有模型的刀 -> 强化 -> 放入
                        ItemStack godSword = getGodBladeFromManager();
                        if (!godSword.isEmpty()) {
                            output.accept(godSword);
                        }

                        // 添加湮灭碎片和核心到创造标签页
                        output.accept(new ItemStack(ModItems.ANNIHILATION_FRAGMENT.get()));
                        output.accept(new ItemStack(ModItems.ANNIHILATION_CORE.get()));
                    })
                    .build());

    public Annihilationblade() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        ModSlashArts.register(modEventBus);
        ModComboStates.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        MinecraftForge.EVENT_BUS.register(this);
    }

    // === 修复：其他标签页 (战斗/拔刀剑) ===
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // 用户要求删除手动添加的部分，依靠 SlashBlade 自动扫描
        // 我们只负责在自定义标签页添加
    }

    // === 核心方法：获取并强化妖刀 ===
    // 这个方法会去 BladeModelManager 里找那把由 JSON 加载成功的刀
    // 这样就能保证有模型，然后再给它注入数据
    private static ItemStack getGodBladeFromManager() {
        if (Minecraft.getInstance().getConnection() != null) {
            var registry = BladeModelManager.getClientSlashBladeRegistry();
            for (var entry : registry.entrySet()) {
                // 找到属于我们 MOD 的刀
                if (entry.getKey() != null && entry.getKey().location().getNamespace().equals(MODID)) {
                    ItemStack stack = entry.getValue().getBlade().copy(); // 复制一份，别改坏了原始缓存
                    applyGodStats(stack); // 强化
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    // 属性注入
    public static void applyGodStats(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("IsAnnihilationBlade", true);
        stack.getOrCreateTag().putInt("KillCount", 10000);
        stack.getOrCreateTag().putInt("ProudSoul", 100000);
        stack.getOrCreateTag().putInt("RepairCost", 1);

        // 这里的路径其实主要靠 JSON 加载，但 NBT 补一下也没坏处
        stack.getOrCreateTag().putString("ModelName", "annihilationblade:model/blade");
        stack.getOrCreateTag().putString("TextureName", "annihilationblade:model/blade");
        stack.getOrCreateTag().putString("SlashArts", "annihilationblade:spatial_fracture");
        stack.getOrCreateTag().putInt("SummonedSwordColor", 0xFFAA00FF);

        stack.enchant(Enchantments.SHARPNESS, 10);
        stack.enchant(Enchantments.FIRE_ASPECT, 10);
        stack.enchant(Enchantments.SMITE, 10);
        stack.enchant(Enchantments.BANE_OF_ARTHROPODS, 10);
        stack.enchant(Enchantments.MOB_LOOTING, 10);

        // 隐藏原版属性 (攻击力/速度)
        stack.getOrCreateTag().putInt("HideFlags", 2);
    }
}