package com.axiom.axiom_pain.mixin.robot;

import com.axiom.axiom_pain.AxiomPainConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(PlayerHealthData.class)
public abstract class PlayerHealthDataMixin {

    @Shadow
    private Map<Limb, LimbStatistics> limbStats;

    @Shadow
    private double totalPain;
    @Shadow
    private float Opioids;
    @Shadow
    private float adrenaline;
    @Shadow
    private float drugAddition;
    @Shadow
    private float dirtiness;


    @Unique
    private boolean isRobot = false;

    @Inject(method = "applyPenalties", at = @At("HEAD"), remap = false)
    void applyPenalties(ServerPlayer player, CallbackInfo ci) {
        if (isRobot)  {
            this.totalPain = 0f;
            this.Opioids = 0f;
            this.adrenaline = 0f;
            this.drugAddition = 0f;
        }
    }

    @WrapMethod(method = "recalculateConsciousness", remap = false)
    void recalculateConsciousness(Operation<Void> original) {
        if (isRobot) this.totalPain = 0.0;
        original.call();
    }

    @WrapOperation(
            method = "applyPenalties",
            at = @At(value = "INVOKE", target = "Lnet/zaharenko424/casualties_cubed/limbs/PlayerHealthData;applyAttributeModifier(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/Attribute;Ljava/lang/String;DLnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;)V"), remap = false)
    private void applyGunk(LivingEntity player, Attribute attribute, String name, double amount, AttributeModifier.Operation operation, Operation<Void> original) {

        double gunkReduction = 0.0;
        if (!isRobot) gunkReduction = 0.0;
        else if (dirtiness > 80) gunkReduction = 0.05;
        else if (dirtiness > 60) gunkReduction = 0.025;
        else if (dirtiness > 40) gunkReduction = 0.0125;
        else if (dirtiness > 20) gunkReduction = 0.0075;

        original.call(player, attribute, name, amount-gunkReduction, operation);
    }

    @WrapMethod(method = "tickUpdate", remap = false)
    void tickUpdate(ServerPlayer player, Operation<Void> original) {
        original.call(player);
        isRobot = AxiomPainConfig.INSTANCE.isRobot(player);
    }

    @ModifyArg(
            method = "UpdateLimb",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/zaharenko424/casualties_cubed/limbs/LimbStatistics;addSkinHealth(F)V"
            ),
            index = 0,
            remap = false
    )
    private float modifySkinHealAmount(float amount) {
        return isRobot ? 0.0F : amount;
    }

    @ModifyArg(
            method = "UpdateLimb",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/zaharenko424/casualties_cubed/limbs/LimbStatistics;addMuscleHealth(F)V"
            ),
            index = 0,
            remap = false
    )
    private float modifyMuscleHealAmount(float amount) {
        return isRobot ? 0.0F : amount;
    }

    @WrapOperation(
            method = "UpdateLimb",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/zaharenko424/casualties_cubed/limbs/LimbStatistics;addInfection(F)V"
            ),
            remap = false
    )
    private void applyInfection(LimbStatistics limbStats, float infection, Operation<Void> original) {
        if (!isRobot) original.call(limbStats, infection);
    }

    @WrapOperation(
            method = "calculateInfectionAndSpread",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/zaharenko424/casualties_cubed/limbs/LimbStatistics;getDisinfectionTimer()F"
            ),
            remap = false
    )
    private float wrapDisinfectionTimer(
            LimbStatistics stats,
            Operation<Float> original) {
        if (isRobot) {
            dirtiness = 0.0F;
            return 6.7F;
        }
        return original.call(stats);
    }
}
