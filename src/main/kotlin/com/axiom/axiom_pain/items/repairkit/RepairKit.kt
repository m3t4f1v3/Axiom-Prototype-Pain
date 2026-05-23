package com.axiom.axiom_pain.items.repairkit

import com.axiom.axiom_pain.AxiomPainConfig
import net.adinvas.casualties_cubed.PlayerHealthProvider
import net.adinvas.casualties_cubed.item.api.IAllowInMedicBags
import net.adinvas.casualties_cubed.item.api.IBandage
import net.adinvas.casualties_cubed.item.api.IMedicalMinigameUsable
import net.adinvas.casualties_cubed.item.api.INbtDrivenDurability
import net.adinvas.casualties_cubed.limbs.Limb
import net.adinvas.casualties_cubed.limbs.PlayerHealthData
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.util.NonNullConsumer
import net.minecraftforge.fml.DistExecutor
import java.util.function.Supplier
import kotlin.math.max

class RepairKit(p_41383_: Properties) : Item(p_41383_), IMedicalMinigameUsable, IAllowInMedicBags,
    INbtDrivenDurability, IBandage {

    override fun openMinigameScreen(target: Player, stack: ItemStack, p2: Limb?, hand: InteractionHand) {
        DistExecutor.unsafeRunWhenOn(
            Dist.CLIENT, { Runnable { openRepairKitMinigame(target, stack, p2!!, hand) } })
    }

    override fun openMinigameBagScreen(
        target: Player,
        bagStack: ItemStack,
        p2: ItemStack,
        slot: Int,
        p4: Limb?,
        hand: InteractionHand
    ) {
        DistExecutor.unsafeRunWhenOn(
            Dist.CLIENT,
            Supplier { Runnable { openRepairKitMinigame(target, p2, bagStack, slot, p4!!, hand) } })
    }

    override fun useBandageAction(scalableAmount: Float, target: Player, limb: Limb?) {
        if (!AxiomPainConfig.isRobot(target)) return

        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA)
            .ifPresent(NonNullConsumer { h: PlayerHealthData? ->
                val painRed = max(0.0f, 1.0f - 0.01f * scalableAmount)
                h!!.setLimbPain(limb, h.getLimbPain(limb) * painRed)
                h.setLimbSkinHealth(limb, h.getLimbSkinHealth(limb) + 0.24f * scalableAmount)
                h.setLimbMuscleHealth(limb, h.getLimbMuscleHealth(limb) + 1.2f * scalableAmount)
                val fractRed = max(0.0f, 1.0f - 0.024f * scalableAmount)
                h.setLimbDislocation(limb, h.getLimbDislocated(limb) * fractRed)
            })
    }

    companion object {
        fun openRepairKitMinigame(target: Player, stack: ItemStack, limb: Limb, hand: InteractionHand) {
            Minecraft.getInstance()
                .setScreen(RepairKitMinigameScreen(Minecraft.getInstance().screen!!, target, stack, limb, hand))
        }

        fun openRepairKitMinigame(target: Player, stack: ItemStack, bagStack: ItemStack, slot: Int, limb: Limb, hand: InteractionHand) {
            Minecraft.getInstance().setScreen(RepairKitMinigameScreen(Minecraft.getInstance().screen!!, target, stack, slot, limb, hand))
        }
    }

    override fun getName(pStack: ItemStack): Component {
        return appendDurability(pStack, Component.empty().append(super.getName(pStack)))
    }

    override fun onCraftedBy(pStack: ItemStack, pLevel: Level, pPlayer: Player) {
        this.getNbtDurability(pStack)
        super.onCraftedBy(pStack, pLevel, pPlayer)
    }
}