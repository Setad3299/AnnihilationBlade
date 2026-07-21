package org.examplea.annihilationblade.registry;

import mods.flammpfeil.slashblade.init.DefaultResources;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.examplea.annihilationblade.Annihilationblade;
import org.examplea.annihilationblade.combat.SpatialFractureExecutor;

public class ModComboStates {
    public static final DeferredRegister<ComboState> REGISTRY = DeferredRegister.create(ComboState.REGISTRY_KEY, Annihilationblade.MODID);

    public static final DeferredHolder<ComboState, ComboState> SPATIAL_FRACTURE_STATE = REGISTRY.register("spatial_fracture_state", () ->
            ComboState.Builder.newInstance()
                    .priority(100)
                    .startAndEnd(400, 460) // 动作帧
                    .motionLoc(DefaultResources.ExMotionLocation)
                    .next(entity -> ResourceLocation.fromNamespaceAndPath("slashblade", "none"))
                    .nextOfTimeout(entity -> ResourceLocation.fromNamespaceAndPath("slashblade", "none"))
                    .addTickAction(ComboState.TimeLineTickAction.getBuilder()
                            // 第 5 帧播放起手音效
                            .put(5, (entity) -> {
                                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                        SoundEvents.END_PORTAL_SPAWN, SoundSource.PLAYERS, 1.0F, 0.5F);
                            })
                            // 第 15 帧执行空间破碎
                            .put(15, (player) -> {
                                if (player.level().isClientSide) return;
                                if (!(player instanceof net.minecraft.world.entity.player.Player mcPlayer)) return;
                                SpatialFractureExecutor.unleash(mcPlayer);
                            })
                            .build()
                    )
                    .build()
    );

    public static void register(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
