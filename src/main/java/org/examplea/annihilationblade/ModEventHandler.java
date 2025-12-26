package org.examplea.annihilationblade;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Annihilationblade.MODID)
public class ModEventHandler {

    private static boolean isGodBlade(ItemStack stack) {
        if (stack.getDescriptionId().equals("item.annihilationblade.annihilation_blade")) return true;
        return stack.hasTag() && stack.getTag().getBoolean("IsAnnihilationBlade");
    }

    private static boolean hasBladeInInventory(Player player) {
        if (isGodBlade(player.getMainHandItem())) return true;
        if (isGodBlade(player.getOffhandItem())) return true;
        for (ItemStack stack : player.getInventory().items) {
            if (isGodBlade(stack)) return true;
        }
        return false;
    }

    private static String getKey(Player player) {
        return player.getStringUUID();
    }

    // === 1. 简介与显示优化 ===
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!isGodBlade(stack)) return;

        List<Component> tooltip = event.getToolTip();

        // A. 清理原版信息
        Iterator<Component> it = tooltip.iterator();
        while (it.hasNext()) {
            String text = it.next().getString();
            if (text.contains("攻击伤害") || text.contains("Attack Damage") ||
                    text.contains("在主手") || text.contains("When in main hand") ||
                    text.contains("攻击速度") || text.contains("Attack Speed") ||
                    text.contains("范围") || text.contains("Range")) {
                it.remove();
            }
        }

        // B. 生成彩虹渐变字: "+∞ 攻击伤害"
        MutableComponent rainbowText = Component.literal(" ");
        String rawText = Component.translatable("item.annihilationblade.infinite_damage").getString();
        long time = System.currentTimeMillis();

        for (int i = 0; i < rawText.length(); i++) {
            float hue = ((time % 3000L) / 3000.0f + (i * 0.08f)) % 1.0f;
            int rgb = Color.HSBtoRGB(hue, 1.0f, 1.0f);
            rainbowText.append(Component.literal(String.valueOf(rawText.charAt(i)))
                    .withStyle(style -> style.withColor(rgb)));
        }

        // C. 插入信息 (在底部)
        tooltip.add(Component.literal(" "));
        tooltip.add(rainbowText);

        // 插入到顶部 (简介)
        if (tooltip.size() >= 1) {
            // 倒序插入到索引1的位置，保证在名字下方
            tooltip.add(1, Component.literal(" "));
            tooltip.add(1, Component.translatable("item.annihilationblade.desc.line4").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
            tooltip.add(1, Component.translatable("item.annihilationblade.desc.line3").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
            tooltip.add(1, Component.translatable("item.annihilationblade.desc.line2").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
            tooltip.add(1, Component.translatable("item.annihilationblade.desc.line1").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
            tooltip.add(1, Component.literal(" "));
            tooltip.add(1, Component.translatable("item.annihilationblade.passive"));
        }
    }

    // === 2. 秒杀逻辑 ===
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onHurt(LivingHurtEvent event) {
        Entity source = event.getSource().getEntity();
        Entity directSource = event.getSource().getDirectEntity();

        if (source instanceof Player player) {
            boolean shouldKill = false;
            if (isGodBlade(player.getMainHandItem())) shouldKill = true;
            if (directSource != null && directSource.getType().toString().contains("slashblade")) {
                if (hasBladeInInventory(player)) shouldKill = true;
            }

            if (shouldKill && !TerminusLogic.isMarkedForDeath(event.getEntity())) {
                event.setAmount(10000.0f);
                TerminusLogic.markForDeath(event.getEntity());

                if (player.distanceTo(event.getEntity()) < 6.0f) {
                    event.getEntity().level().playSound(null, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                            SoundEvents.TRIDENT_THUNDER, SoundSource.PLAYERS, 0.5f, 2.0f);
                }
            }
        }
    }

    // === 3. 保护与增益 ===
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player && hasBladeInInventory(player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player && hasBladeInInventory(player)) {
            event.setCanceled(true);
            player.setHealth(player.getMaxHealth());
        }
    }

    private static final Set<String> playersWithFlight = new HashSet<>();
    
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        String key = getKey(player);

        if (hasBladeInInventory(player)) {
            if (player.getHealth() < player.getMaxHealth()) player.setHealth(player.getMaxHealth());
            player.getFoodData().setFoodLevel(20);
            player.getFoodData().setSaturation(20.0f);
            player.removeAllEffects();

            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 220, 1, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 220, 4, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 220, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 220, 2, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 220, 2, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 220, 4, false, false));

            if (!player.isCreative() && !player.isSpectator()) {
                if (!player.getAbilities().mayfly) {
                    player.getAbilities().mayfly = true;
                    playersWithFlight.add(key);
                    player.onUpdateAbilities();
                }
            }
            if (player.getY() < -64) {
                player.teleportTo(player.getX(), 320, player.getZ());
                player.setDeltaMovement(0, 0, 0);
                if (!player.getAbilities().flying) {
                    player.getAbilities().flying = true;
                    player.onUpdateAbilities();
                }
            }
        } else {
            if (!player.isCreative() && !player.isSpectator() && player.getAbilities().mayfly && playersWithFlight.contains(key)) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                playersWithFlight.remove(key);
                player.onUpdateAbilities();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onItemToss(ItemTossEvent event) {
        if (isGodBlade(event.getEntity().getItem())) {
            if (!event.getPlayer().isCreative()) {
                event.setCanceled(true);
                event.getPlayer().getInventory().add(event.getEntity().getItem().copy());
            }
        }
    }
}