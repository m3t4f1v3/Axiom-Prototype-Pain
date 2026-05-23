package com.axiom.axiom_pain.items.repairkit

import com.axiom.axiom_pain.mixin.robot.BandageObjectAccessor
import com.mojang.math.Axis
import net.adinvas.casualties_cubed.client.gui.StatusSprites
import net.adinvas.casualties_cubed.client.gui.minigames.BandageMinigameScreen
import net.adinvas.casualties_cubed.client.gui.minigames.BandageObject
import net.adinvas.casualties_cubed.client.gui.minigames.HandObject
import net.adinvas.casualties_cubed.limbs.Limb
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import java.lang.reflect.Field


class RepairKitMinigameScreen : BandageMinigameScreen {

    private val hand: InteractionHand
    private val slot: Int

    // Define your custom textures here
    private val EMPTY_ICON = ResourceLocation.fromNamespaceAndPath("axiom_pain", "textures/gui/empty.png")
    private val REPAIR_KIT = ResourceLocation.fromNamespaceAndPath("axiom_pain", "textures/gui/repair_kit.png")

    // Primary constructor
    constructor(
        parent: Screen,
        target: Player,
        stack: ItemStack,
        limb: Limb,
        hand: InteractionHand,
    ) : super(parent, target, stack, limb, hand) {
        this.hand = hand
        this.slot = -1
    }

    // Secondary constructor (for bags)
    constructor(
        parent: Screen,
        target: Player,
        stack: ItemStack,
        slot: Int,
        limb: Limb,
        hand: InteractionHand,
    )
            : super(parent, target, stack, slot, limb, hand) {
        this.hand = hand
        this.slot = slot
    }

    override fun init() {
        super.init()

        // Use reflection to replace the private bandageObject with one using our texture
        try {
            val field: Field = BandageMinigameScreen::class.java.getDeclaredField("bandageObject")
            field.isAccessible = true

            val newObject = BandageObject(
                0, 0, 0, 0, 64, 64,
                EMPTY_ICON, // Your custom icon
                64, 64, 1.0f,
                this.width / 2, this.height / 2,
                this.itemStackFromParent, // Helper property defined below
                this.slot,
                this.hand
            )

            field.set(this, newObject)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTicks: Float) {
        // We don't call super.render() because it hardcodes the bandage textures.
        // Instead, we call the grandparent (Screen) and reimplement the logic.

        val parentScreen = this.parentScreenFromParent
        parentScreen?.render(guiGraphics, mouseX, mouseY, partialTicks)

        // Call Screen.render (skipping BandageMinigameScreen.render)
        renderBackground(guiGraphics)

        // Draw the dark overlay
        guiGraphics.fill(0, 0, this.width, this.height, -2013265920)

        val mc = Minecraft.getInstance()

        // Draw YOUR custom center texture
        val pose = guiGraphics.pose()

        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath("prototype_pain", "textures/gui/bandage_center.png"), this.width / 2 - 40, this.height / 2 - 40, 0, 0.0f, 80.0f, 80, 80, 80, 80)

        pose.pushPose()
        pose.translate(bandageObjectFromParent!!.x + 32f, bandageObjectFromParent!!.y + 32f, 0.0)
        guiGraphics.pose().mulPose(Axis.ZP.rotation((bandageObjectFromParent as BandageObjectAccessor).rotation/6f))
        pose.translate(-32.0, -32.0, 0.0)
        guiGraphics.blit(REPAIR_KIT, 0, 0, 0f, 0f, 64, 64, 64, 64)
        pose.popPose()

        // Instructions (reusing parent translations or adding your own)
        guiGraphics.drawCenteredString(mc.font, Component.translatable("prototype_pain.gui.repair_kit_instruction1"), this.width / 2, 10, 16777215)
        guiGraphics.drawCenteredString(mc.font, Component.translatable("prototype_pain.gui.repair_kit_instruction2"), this.width / 2, 20, 16777215)
        guiGraphics.drawCenteredString(mc.font, Component.translatable("prototype_pain.gui.minigame_exit"), this.width / 2, this.height / 6 + 190, 16777215)

        // Render the bandageObject (which we swapped in init)
        this.bandageObjectFromParent?.render(guiGraphics)

        // Bleed logic (copied from parent)
        val bleedRate = getBleedRateFromParent()
        val maxBleed = getMaxBleedFromParent()
        if (bleedRate > 0.0f) {
            val bleedScale = 0.5f + 1.4f * (bleedRate / maxBleed)
            val sizePx = 20.0f * bleedScale
            guiGraphics.blit(StatusSprites.BLEED.tex, (this.width / 2 - sizePx / 2f).toInt(), (this.height / 2 - sizePx / 2f + 10f).toInt(), 0f, 0f, sizePx.toInt(), sizePx.toInt(), sizePx.toInt(), sizePx.toInt())
        }

        // Render the item icon in corner
        guiGraphics.renderItem(this.itemStackFromParent, this.width / 10 - 10, this.height / 10 + 5)
        guiGraphics.drawString(mc.font, this.itemStackFromParent.hoverName, this.width / 10 + 16, this.height / 10 + 5, 16777215)

        this.handObjectFromParent?.render(guiGraphics, partialTicks)
    }

    // --- Reflection Helpers to access private fields from BandageMinigameScreen ---

    private val bandageObjectFromParent: BandageObject?
        get() = getPrivateField<BandageObject>("bandageObject")

    private val handObjectFromParent: HandObject?
        get() = getPrivateField<HandObject>("handObject")

    private val itemStackFromParent: ItemStack
        get() = getPrivateField<ItemStack>("bandageStack") ?: ItemStack.EMPTY

    private val parentScreenFromParent: Screen?
        get() = getPrivateField<Screen>("parent")

    private fun getBleedRateFromParent(): Float = getPrivateField<Float>("bleedRate") ?: 0f
    private fun getMaxBleedFromParent(): Float = getPrivateField<Float>("maxBleed") ?: 1f

    private fun <T> getPrivateField(name: String): T? {
        return try {
            val field = BandageMinigameScreen::class.java.getDeclaredField(name)
            field.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            field.get(this) as T
        } catch (e: Exception) {
            null
        }
    }
}