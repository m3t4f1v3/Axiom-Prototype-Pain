package com.axiom.axiom_pain.mixin.robot;

import com.axiom.axiom_pain.AxiomPainConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.adinvas.casualties_cubed.client.gui.minigames.BoneObject;
import net.adinvas.casualties_cubed.client.gui.minigames.GrabObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BoneObject.class)
public abstract class BoneObjectMixin extends GrabObject {

    public BoneObjectMixin(int x, int y, int hitX, int hitY, int hitWidth, int hitHeight, ResourceLocation tex, int texWidth, int texHeight, float scale) {
        super(x, y, hitX, hitY, hitWidth, hitHeight, tex, texWidth, texHeight, scale);
    }

    @WrapOperation(
            method = "<init>",
            at = @At(value = "NEW", target = "(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;")
    )
    private static ResourceLocation fuck(String p_135811_, String p_135812_, Operation<ResourceLocation> original) {

        Screen currentScreen = Minecraft.getInstance().screen;
        if (currentScreen instanceof DislocationScreenAccessor screenAccessor && AxiomPainConfig.INSTANCE.isRobot(screenAccessor.getPlayer()))
            return ResourceLocation.fromNamespaceAndPath("axiom_pain", "textures/gui/bone.png");
        return original.call(p_135811_, p_135812_);


    }
}
