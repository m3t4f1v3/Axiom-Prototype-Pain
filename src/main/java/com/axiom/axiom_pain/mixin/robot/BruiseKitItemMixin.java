package com.axiom.axiom_pain.mixin.robot;

import com.axiom.axiom_pain.AxiomPainConfig;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.item.bandages.BruiseKitItem;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BruiseKitItem.class)
public class BruiseKitItemMixin {
    @WrapMethod(method = "useBandageAction", remap = false)
    public void useBandageAction(float scalableAmount, Player target, Limb limb, Operation<Void> original) {
        if (!AxiomPainConfig.INSTANCE.isRobot(target)) original.call(scalableAmount, target, limb);
    }
}
