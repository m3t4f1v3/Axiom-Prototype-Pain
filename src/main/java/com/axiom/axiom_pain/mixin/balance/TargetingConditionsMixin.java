package com.axiom.axiom_pain.mixin.balance;

import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TargetingConditions.class)
public class TargetingConditionsMixin {

    @Inject(method = "test", at = @At("HEAD"), cancellable = true)
    private void handleUnconsciousPlayers(LivingEntity attacker, LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (target instanceof Player player) {
            // Access your capability
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(cap -> {
                if (cap.getConsciousness() <= 10) {
                    // Tell the AI this player is not a valid target
                    cir.setReturnValue(false);
                }
            });
        }
    }
}