package org.examplea.annihilationblade;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class TerminusLogic {

    // 标记为濒死（给普通攻击用，防止死循环）
    public static void markForDeath(LivingEntity target) {
        if (target.getHealth() > 0) {
            target.setHealth(0.1f); // 剩一丝血
            target.invulnerableTime = 0; // 移除无敌帧
        }
    }

    // 检查是否已经标记为死亡
    public static boolean isMarkedForDeath(LivingEntity target) {
        return target.getHealth() <= 0.1f && target.invulnerableTime == 0;
    }

    // 执行处决（给 SA 用，或者补刀用）
    public static void execute(LivingEntity target, Player attacker) {
        if (target.level().isClientSide) return;

        // 1. 破盾
        target.invulnerableTime = 0;

        // 2. 清零血量
        target.setHealth(0);

        // 3. 触发死亡逻辑 (掉落物品等)
        if (target.isAlive()) {
            DamageSource source = target.level().damageSources().playerAttack(attacker);
            target.die(source);
        }

        // 4. 强制删除 (防止锁血怪)
        if (target.isAlive()) {
            target.discard();
        }
    }
}