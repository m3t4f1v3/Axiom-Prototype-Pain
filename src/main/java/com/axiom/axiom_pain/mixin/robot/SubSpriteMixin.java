package com.axiom.axiom_pain.mixin.robot;

//This is the worst thing i have ever written

import com.axiom.axiom_pain.AxiomPainConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.zaharenko424.casualties_cubed.client.gui.StatusSprites;
import net.zaharenko424.casualties_cubed.client.gui.widget.SubSprite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SubSprite.class)
public class SubSpriteMixin {


    @Final
    @Shadow
    private ResourceLocation txt;



    @WrapOperation(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V")
    )
    public void swapIcon(GuiGraphics instance, ResourceLocation p_283272_, int p_283605_, int p_281879_, float p_282809_, float p_282942_, int p_281922_, int p_282385_, int p_282596_, int p_281699_, Operation<Void> original) {

        Screen currentScreen = Minecraft.getInstance().screen;
        Player target = null;
        if (currentScreen instanceof HealthScreenAccessor screenAccessor) target = screenAccessor.getPlayer();
        else if (currentScreen instanceof BandageScreenAccessor screenAccessor ) target = screenAccessor.getPlayer();
        else if (currentScreen instanceof CPRScreenAccessor screenAccessor ) target = screenAccessor.getPlayer();
        else if (currentScreen instanceof DislocationScreenAccessor screenAccessor ) target = screenAccessor.getPlayer();
        else if (currentScreen instanceof ShrapnelScreenAccessor screenAccessor ) target = screenAccessor.getPlayer();
        else if (currentScreen instanceof InjectScreenAccessor screenAccessor ) target = screenAccessor.getPlayer();
        else if (currentScreen instanceof AmputationScreenAccessor screenAccessor ) target = screenAccessor.getPlayer();
        else {
            original.call(instance, p_283272_, p_283605_, p_281879_, p_282809_, p_282942_, p_281922_, p_282385_, p_282596_, p_281699_);
            return;
        }

        if (!AxiomPainConfig.INSTANCE.isRobot(target)) {
            original.call(instance, p_283272_, p_283605_, p_281879_, p_282809_, p_282942_, p_281922_, p_282385_, p_282596_, p_281699_);
            return;
        }

        if (this.txt.equals(StatusSprites.BLEED.tex)) original.call(instance, ResourceLocation.fromNamespaceAndPath("axiom_pain",  "textures/gui/icons/oil.png"), p_283605_, p_281879_, p_282809_, p_282942_, p_281922_, p_282385_, p_282596_, p_281699_);
        else if (this.txt.equals(StatusSprites.DISLOCATION.tex)) original.call(instance, ResourceLocation.fromNamespaceAndPath("axiom_pain",  "textures/gui/icons/dislocation.png"), p_283605_, p_281879_, p_282809_, p_282942_, p_281922_, p_282385_, p_282596_, p_281699_);
        else if (this.txt.equals(StatusSprites.FRACTURE.tex)) original.call(instance, ResourceLocation.fromNamespaceAndPath("axiom_pain",  "textures/gui/icons/fracture.png"), p_283605_, p_281879_, p_282809_, p_282942_, p_281922_, p_282385_, p_282596_, p_281699_);
        else original.call(instance, p_283272_, p_283605_, p_281879_, p_282809_, p_282942_, p_281922_, p_282385_, p_282596_, p_281699_);
    }
}
