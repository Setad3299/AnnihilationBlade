package org.examplea.annihilationblade.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import javax.annotation.Nonnull;
import java.util.List;
import org.examplea.annihilationblade.blade.AnnihilationBladeFactory;

public class ItemAnnihilationCore extends Item {
    public ItemAnnihilationCore() {
        super(new Properties().stacksTo(1));
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            ItemStack godSword = AnnihilationBladeFactory.getAnnihilationBladeStack(level.registryAccess());
            if (!godSword.isEmpty()) {
                if (player.getInventory().add(godSword)) {
                    itemStack.shrink(1);
                    return InteractionResultHolder.success(itemStack);
                }
            }
        }
        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull Item.TooltipContext context, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("item.annihilationblade.annihilation_core.tip"));
    }
}
