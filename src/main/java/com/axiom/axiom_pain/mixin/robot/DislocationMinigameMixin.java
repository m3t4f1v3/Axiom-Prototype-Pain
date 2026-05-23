package com.axiom.axiom_pain.mixin.robot;

import com.axiom.axiom_pain.AxiomPainConfig;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.adinvas.casualties_cubed.client.gui.minigames.BoneObject;
import net.adinvas.casualties_cubed.client.gui.minigames.DislocationMinigameScreen;
import net.adinvas.casualties_cubed.client.gui.minigames.HandObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DislocationMinigameScreen.class)
public abstract class DislocationMinigameMixin extends Screen  {

    @Final
    @Shadow
    private Screen parent;

    @Shadow
    private BoneObject boneObject;

    @Shadow
    private HandObject handObject;

    @Shadow
    @Final
    private Player target;

    protected DislocationMinigameMixin(Component p_96550_) {
        super(p_96550_);
    }

    @WrapMethod(method = "render")
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, Operation<Void> original) {
        if (!AxiomPainConfig.INSTANCE.isRobot(target)) {
            original.call(guiGraphics, mouseX, mouseY, partialTicks);
            return;
        }

        this.parent.render(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.fill(0, 0, this.width, this.height, -2013265920);
        Minecraft mc = Minecraft.getInstance();
        guiGraphics.drawCenteredString(mc.font, Component.translatable("prototype_pain.gui.dislocation_instruction1"), this.width / 2, 10, 16777215);
        guiGraphics.drawCenteredString(mc.font, (int)this.boneObject.getFakeDislocation() + "%", this.width / 2, this.height / 6 + 175, 13369344);
        guiGraphics.drawCenteredString(mc.font, Component.translatable("prototype_pain.gui.minigame_exit"), this.width / 2, this.height / 6 + 190, 16777215);
        guiGraphics.blit(new ResourceLocation("axiom_pain", "textures/gui/bone2.png"), this.width / 2 - 160, this.height / 2 - 40, 0.0F, 0.0F, 160, 80, 160, 80);
        guiGraphics.setColor(0.5F, 0.5F, 0.5F, 0.1F);
        guiGraphics.blit(new ResourceLocation("axiom_pain", "textures/gui/bone.png"), this.width / 2, this.height / 2 - 40, 0.0F, 0.0F, 160, 80, 160, 80);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.boneObject.render(guiGraphics);
        this.handObject.render(guiGraphics, partialTicks);
    }

}
