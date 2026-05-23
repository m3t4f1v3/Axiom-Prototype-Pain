package com.axiom.axiom_pain.mixin.robot;

import com.axiom.axiom_pain.init.ItemRegistry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.adinvas.casualties_cubed.client.gui.minigames.BandageObject;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BandageObject.class)
public class BandageObjectMixin {

    @WrapMethod(method = "calculateScale", remap = false)
    public void calculateScale(ItemStack stack, Operation<Void> original) {
        if (stack.is(ItemRegistry.INSTANCE.getREPAIR_KIT().get())) return;
        original.call(stack);
    }
}
