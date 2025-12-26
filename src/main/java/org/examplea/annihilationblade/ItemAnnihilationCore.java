package org.examplea.annihilationblade;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemAnnihilationCore extends Item {
    public ItemAnnihilationCore() {
        super(new Properties().stacksTo(1));
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // 获取并强化妖刀
            ItemStack godSword = getGodBladeFromManager();
            if (!godSword.isEmpty()) {
                // 给玩家物品
                if (player.getInventory().add(godSword)) {
                    // 消耗核心物品
                    itemStack.shrink(1);
                    return InteractionResultHolder.success(itemStack);
                }
            }
        }

        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("item.annihilationblade.annihilation_core.tip"));
    }

    // 从 BladeModelManager 获取妖刀
    private static ItemStack getGodBladeFromManager() {
        if (Minecraft.getInstance().getConnection() != null) {
            var registry = BladeModelManager.getClientSlashBladeRegistry();
            for (var entry : registry.entrySet()) {
                // 找到属于我们 MOD 的刀
                if (entry.getKey() != null && entry.getKey().location().getNamespace().equals(Annihilationblade.MODID)) {
                    ItemStack stack = entry.getValue().getBlade().copy(); // 复制一份，别改坏了原始缓存
                    Annihilationblade.applyGodStats(stack); // 强化
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }
}