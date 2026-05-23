package com.axiom.axiom_pain.init

import com.axiom.axiom_pain.AxiomPainConfig
import net.adinvas.casualties_cubed.PlayerHealthProvider
import net.adinvas.casualties_cubed.fluid_system.MedicalEffect
import net.adinvas.casualties_cubed.limbs.Limb
import net.adinvas.casualties_cubed.limbs.PlayerHealthData
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraftforge.common.util.NonNullConsumer

object AxiomMedicalEffects {

    val BLOOD: MedicalEffect = object : MedicalEffect {
        override fun applyInjected(player: ServerPlayer, ml: Float, limb: Limb?) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA)
                .ifPresent(NonNullConsumer { h: PlayerHealthData? ->
                    if (!AxiomPainConfig.isRobot(player)) h?.bloodVolume = h.bloodVolume + ml * 0.001f
                    else {
                        player.addEffect(MobEffectInstance(MobEffects.CONFUSION, (100*ml).toInt(), 1))
                        player.addEffect(MobEffectInstance(MobEffects.POISON, (100*ml).toInt(), 2))
                    }
                })
        }
    }

    val OIL: MedicalEffect = object : MedicalEffect {
        override fun applyInjected(player: ServerPlayer, ml: Float, limb: Limb?) {
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA)
                .ifPresent(NonNullConsumer { h: PlayerHealthData? ->
                    if (AxiomPainConfig.isRobot(player)) h?.bloodVolume = h.bloodVolume + ml * 0.001f
                    else {
                        player.addEffect(MobEffectInstance(MobEffects.CONFUSION, (100*ml).toInt(), 1))
                        player.addEffect(MobEffectInstance(MobEffects.POISON, (100*ml).toInt(), 2))
                    }
                })
        }

        override fun applyIngested(player: ServerPlayer, ml: Float) {
            if (!AxiomPainConfig.isRobot(player)) {
                player.addEffect(MobEffectInstance(MobEffects.CONFUSION, 100, 1))
                player.addEffect(MobEffectInstance(MobEffects.POISON, 100, 2))
            }
        }
    }
}