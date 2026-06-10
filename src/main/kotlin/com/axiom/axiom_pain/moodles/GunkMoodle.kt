package com.axiom.axiom_pain.moodles

import com.axiom.axiom_pain.AxiomPainConfig
import net.zaharenko424.casualties_cubed.PlayerHealthProvider
import net.zaharenko424.casualties_cubed.client.moodles.AbstractMoodleVisual
import net.zaharenko424.casualties_cubed.client.moodles.MoodleStatus
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraftforge.common.util.NonNullFunction

class GunkMoodle : AbstractMoodleVisual() {

    override fun calculateStatus(player: Player): MoodleStatus {
        val dirtyness = player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA)
            .map(NonNullFunction { obj: PlayerHealthData? -> obj!!.dirtiness }).orElse(0f) as Float

        if (!AxiomPainConfig.isRobot(player)) return MoodleStatus.NONE

        return when {
            dirtyness > 80 -> MoodleStatus.CRITICAL
            dirtyness > 60 -> MoodleStatus.HEAVY
            dirtyness > 40 -> MoodleStatus.NORMAL
            dirtyness > 20 -> MoodleStatus.LIGHT
            else -> MoodleStatus.NONE
        }
    }

    override fun renderIcon(ms: GuiGraphics, partialTicks: Float, x: Int, y: Int) {
        var tex = ResourceLocation.fromNamespaceAndPath("axiom_pain", "textures/gui/icons/gunkmoodle.png")

        ms.blit(tex, x, y, 0.0f, 0.0f, 16, 16, 16, 16)
    }

    override fun getTooltip(player: Player): MutableList<Component> {
        val componentList: MutableList<Component> = ArrayList()
        when (this.getMoodleStatus()) {
            MoodleStatus.LIGHT -> {
                componentList.add(Component.literal("Slightly Gunked").withStyle(ChatFormatting.GRAY))
                componentList.add(
                    Component.literal("Gunky")

                ) }
            MoodleStatus.NORMAL -> {
                componentList.add(Component.literal("Gunked").withStyle(ChatFormatting.GRAY))
                componentList.add(
                    Component.literal("Gunky")

                ) }
            MoodleStatus.HEAVY -> {
                componentList.add(Component.literal("Very Gunked").withStyle(ChatFormatting.RED))
                componentList.add(
                    Component.literal("Gunky")
                        .withStyle(ChatFormatting.GOLD)
                ) }
            MoodleStatus.CRITICAL -> {
                componentList.add(Component.literal("Extremely Gunked").withStyle(ChatFormatting.RED))
                componentList.add(
                    Component.literal("Gunky")

                ) }
            MoodleStatus.NONE -> {}
        }


        return componentList
    }
}
