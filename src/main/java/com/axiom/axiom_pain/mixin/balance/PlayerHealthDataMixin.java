package com.axiom.axiom_pain.mixin.balance;

import com.axiom.axiom_pain.AxiomPainConfig;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.adinvas.casualties_cubed.limbs.PlayerHealthData;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerHealthData.class)
public class PlayerHealthDataMixin {

    @WrapMethod(method = "handleAmputation", remap = false)
    boolean handleAmputation(Limb limb, float damage, float base_damage_treshhold, Player player, Operation<Boolean> original) {
        return original.call(limb, damage, 40f, player);
    }
}
