package com.axiom.axiom_pain.mixin.robot;

import com.axiom.axiom_pain.init.ItemRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.adinvas.casualties_cubed.client.gui.minigames.BandageObject;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BandageObject.class)
public class BandageObjectMixin {
    @Shadow
    private ItemStack itemStack;

    @WrapMethod(method = "calculateScale", remap = false)
    public void calculateScale(Operation<Void> original) {
        if (itemStack.is(ItemRegistry.INSTANCE.getREPAIR_KIT().get())) return;
        original.call();
    }
}
