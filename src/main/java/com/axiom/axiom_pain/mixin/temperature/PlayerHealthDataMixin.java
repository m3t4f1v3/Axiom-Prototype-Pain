package com.axiom.axiom_pain.mixin.temperature;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import homeostatic.common.capabilities.ITemperature;
import homeostatic.common.capabilities.TemperatureCapability;
import homeostatic.util.TempHelper;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.adinvas.casualties_cubed.limbs.PlayerHealthData;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerHealthData.class)
public class PlayerHealthDataMixin {

    @Shadow
    private float temperature;

    @Shadow
    private double totalPain;

    @Inject(method = "updateTemperature", at = @At("RETURN"), remap = false)
    void replaceTemperature(Player player, CallbackInfo ci) {

        TemperatureCapability.getCapability(player).ifPresent((h)-> {
            float coreTemp = h.getCoreTemperature();
            this.temperature = (float) TempHelper.convertMcTemp(coreTemp, false);
        } );

    }

    @WrapOperation(
            method = "handleFireDamage",
            at = @At(value = "INVOKE", target = "Lnet/adinvas/casualties_cubed/limbs/PlayerHealthData;applyBleedDamage(Lnet/adinvas/casualties_cubed/limbs/Limb;FLnet/minecraft/world/entity/player/Player;)V")
            , remap = false)
    void applyBleedDamageFire(PlayerHealthData instance, Limb limb, float damage, Player player, Operation<Void> original) {
        return;
    }
}
