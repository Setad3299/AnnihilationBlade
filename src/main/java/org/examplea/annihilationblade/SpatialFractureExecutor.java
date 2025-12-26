package org.examplea.annihilationblade;

import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Executes the Spatial Fracture slash arts by sweeping a More-Avaritia-inspired ray and wiping every valid target.
 */
public final class SpatialFractureExecutor {
    private static final double MAX_DISTANCE = 128.0D;
    private static final double STEP = 3.5D;
    private static final double SAMPLE_RADIUS = 3.0D;
    private static final double BACKUP_RADIUS = 32.0D;
    private static final int MAX_TARGETS = 256;

    private SpatialFractureExecutor() {
    }

    @SuppressWarnings("resource")
    public static void unleash(LivingEntity entity) {
        if (!(entity instanceof Player player)) return;
        if (entity.level().isClientSide) return;

        ServerLevel level = (ServerLevel) entity.level();
        Set<LivingEntity> targets = gatherTargets(level, player);
        if (targets.isEmpty()) return;

        targets.forEach(target -> {
            spawnSlash(level, player, target);
            TerminusLogic.execute(target, player);
        });

        playDetonation(level, player, targets.size());
    }

    private static Set<LivingEntity> gatherTargets(ServerLevel level, Player player) {
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();
        Set<LivingEntity> targets = new LinkedHashSet<>();

        for (double distance = 2.0D; distance <= MAX_DISTANCE && targets.size() < MAX_TARGETS; distance += STEP) {
            Vec3 sampleCenter = eye.add(look.scale(distance));
            AABB sample = new AABB(sampleCenter, sampleCenter).inflate(SAMPLE_RADIUS);
            for (LivingEntity candidate : level.getEntitiesOfClass(LivingEntity.class, sample, entity -> canTarget(player, entity))) {
                targets.add(candidate);
                if (targets.size() >= MAX_TARGETS) break;
            }
        }

        if (targets.isEmpty()) {
            AABB fallback = player.getBoundingBox().inflate(BACKUP_RADIUS);
            targets.addAll(level.getEntitiesOfClass(LivingEntity.class, fallback, entity -> canTarget(player, entity)));
        }

        return targets;
    }

    private static boolean canTarget(Player player, LivingEntity candidate) {
        if (candidate == player) return false;
        if (!candidate.isAlive()) return false;
        if (candidate.isAlliedTo(player)) return false;
        if (candidate instanceof Player other) {
            if (other.isCreative() || other.isSpectator()) return false;
            return !(other.getMainHandItem().getItem() instanceof ItemAnnihilationBlade);
        }
        return true;
    }

    private static void spawnSlash(ServerLevel level, Player player, LivingEntity target) {
        level.sendParticles(ParticleTypes.END_ROD, target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ(), 10, 0.6, 0.6, 0.6, 0.0D);
        level.sendParticles(ParticleTypes.PORTAL, target.getX(), target.getY() + 1.0D, target.getZ(), 20, 1.0D, 1.0D, 1.0D, 0.3D);
        AttackManager.doSlash(player, player.getRandom().nextInt(360), Vec3.ZERO, true, true, 9999.0F);
    }

    private static void playDetonation(ServerLevel level, Player player, int count) {
        float volume = Math.min(4.0F, 1.0F + count / 20.0F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, volume, 0.4F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.END_PORTAL_SPAWN, SoundSource.PLAYERS, volume, 0.8F);
        level.sendParticles(ParticleTypes.DRAGON_BREATH, player.getX(), player.getY() + 1.0D, player.getZ(), 80, 2.0D, 1.0D, 2.0D, 0.2D);
    }
}
