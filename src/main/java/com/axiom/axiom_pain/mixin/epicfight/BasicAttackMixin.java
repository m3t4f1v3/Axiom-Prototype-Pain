package com.axiom.axiom_pain.mixin.epicfight;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.InteractionHand;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import yesman.epicfight.skill.BasicAttack;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

@Mixin(value = BasicAttack.class, remap = false)
public class BasicAttackMixin {

    @Inject(method = "isExecutableState", at = @At("RETURN"), cancellable = true)
    private void blockTwoHandWhenAmputated(PlayerPatch<?> executor, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        CapabilityItem mainHand = executor.getHoldingItemCapability(InteractionHand.MAIN_HAND);

        if (mainHand.getStyle(executor) == CapabilityItem.Styles.TWO_HAND) {
            executor.getOriginal().getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
                if (data.isAmputated(Limb.LEFT_ARM) || data.isAmputated(Limb.RIGHT_ARM)) {
                    cir.setReturnValue(false);
                }
            });
        }
    }
}
