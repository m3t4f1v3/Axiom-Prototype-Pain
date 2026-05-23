package com.axiom.axiom_pain.mixin.robot;

import com.axiom.axiom_pain.AxiomPain;
import com.axiom.axiom_pain.AxiomPainConfig;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.adinvas.casualties_cubed.limbs.LimbStatistics;
import net.adinvas.casualties_cubed.limbs.PlayerHealthData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
    private float drug_addition;
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
            this.drug_addition = 0f;
        }
    }

    @WrapMethod(method = "recalculateConsciousness", remap = false)
    void recalculateConsciousness(Operation<Void> original) {
        if (isRobot) this.totalPain = 0.0;
        original.call();
    }

    @WrapOperation(
            method = "applyPenalties",
            at = @At(value = "INVOKE", target = "Lnet/adinvas/casualties_cubed/limbs/PlayerHealthData;applyAttributeModifier(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/Attribute;Ljava/lang/String;DLnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;)V"), remap = false)
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

    @WrapMethod(method = "getNORMAL_LIMB_HEAL_RATE", remap = false)
    public float getNORMAL_LIMB_HEAL_RATE(Operation<Float> original) {
        if (isRobot) return 0f;
        return original.call();
    }

    @WrapMethod(method = "getBOOSTED_LIMB_HEAL_RATE", remap = false)
    public float getBOOSTED_LIMB_HEAL_RATE(Operation<Float> original) {
        if (isRobot) return 0f;
        return original.call();
    }

    @WrapMethod(method = "getINFECTION_CHANCE", remap = false)
    public float getINFECTION_CHANCE(Operation<Float> original) {
        if (isRobot) return 0f;
        return original.call();
    }

    @WrapMethod(method = "setLimbDisinfected", remap = false)
    public void setLimbDesinfected(Limb limb, float desinfection, Operation<Void> original) {
        if (desinfection > 0) dirtiness = 0f;
        if (!isRobot) original.call(limb, desinfection);
    }
}
