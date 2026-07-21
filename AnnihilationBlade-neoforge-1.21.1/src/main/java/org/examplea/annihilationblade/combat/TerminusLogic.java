package org.examplea.annihilationblade.combat;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class TerminusLogic {

    private TerminusLogic() {
    }

    /** 将目标压到濒死状态，防止伤害事件递归。 */
    public static void markForDeath(LivingEntity target) {
        if (target.getHealth() > 0) {
            target.setHealth(0.1f);
            target.invulnerableTime = 0;
        }
    }

    public static boolean isMarkedForDeath(LivingEntity target) {
        return target.getHealth() <= 0.1f && target.invulnerableTime == 0;
    }

    /** 处决目标，并在常规死亡逻辑失效时移除实体。 */
    public static void execute(LivingEntity target, Player attacker) {
        if (target.level().isClientSide) return;

        target.invulnerableTime = 0;
        target.setHealth(0);
        if (target.isAlive()) {
            DamageSource source = target.level().damageSources().playerAttack(attacker);
            target.die(source);
        }

        if (target.isAlive()) {
            target.discard();
        }
    }
}
