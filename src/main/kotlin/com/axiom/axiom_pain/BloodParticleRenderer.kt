package com.axiom.axiom_pain // Ensure this matches your other files!

import com.axiom.axiom_pain.init.AxiomParticleTypes
import net.adinvas.casualties_cubed.PlayerHealthProvider
import net.mcreator.bloodbits.init.BloodbitsModParticleTypes
import net.minecraft.client.Minecraft
import net.minecraftforge.event.TickEvent
import kotlin.random.Random

object BloodParticleRenderer {
    fun tick(event: TickEvent.ClientTickEvent) {
        // Only run on the start of the tick to avoid double-processing
        //if (event.phase != TickEvent.Phase.START) return

        val level = Minecraft.getInstance().level ?: return
        if (Minecraft.getInstance().isPaused) return
        val players = level.players() ?: return

        for (player in players) {
            val dataCapability = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA)
            if (!dataCapability.isPresent) continue

            val data = dataCapability.orElseThrow { AssertionError() }
            val bleed = data.combinedBleed

            if (bleed <= 0f) continue

            // --- SPURT LOGIC ---

            // 1. Frequency: How often do we spurt?
            // Higher bleed = higher chance per tick.
            // Example: at 0.01 bleed, chance is 0.1 (10% chance per tick)
            val spurtChance = (bleed * 100.0).coerceAtMost(0.5)

            if (Random.nextDouble() < spurtChance) {

                // 2. Quantity: How many particles in this specific spurt?
                // Higher bleed = more particles.
                val amount = (bleed * 25000).toInt().coerceIn(3, 50)

                // 3. Intensity: How fast do they spray?
                val velocityMult = (bleed * 7500.0).coerceAtMost(1.5)

                val type = if (AxiomPainConfig.isRobot(player)) {
                    AxiomParticleTypes.OILSPASH
                } else {
                    BloodbitsModParticleTypes.BLOODSPLASH
                }

                for (i in 1..amount) {
                    level.addParticle(
                        type.get(),
                        player.x + Random.nextDouble(-0.1, 0.1),
                        player.y + 1.0 + Random.nextDouble(-0.2, 0.2),
                        player.z + Random.nextDouble(-0.1, 0.1),
                        // Movement math: adds player momentum + random spray + intensity
                        (Random.nextDouble(-0.1, 0.1) * velocityMult) + player.deltaMovement.x,
                        (Random.nextDouble(0.1, 0.3) * velocityMult) + player.deltaMovement.y,
                        (Random.nextDouble(-0.1, 0.1) * velocityMult) + player.deltaMovement.z
                    )
                }
            }
        }
    }
}