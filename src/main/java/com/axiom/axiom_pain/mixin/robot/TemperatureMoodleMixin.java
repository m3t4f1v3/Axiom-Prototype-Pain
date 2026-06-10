package com.axiom.axiom_pain.mixin.robot;

import com.axiom.axiom_pain.AxiomPainConfig;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.zaharenko424.casualties_cubed.client.moodles.AbstractMoodleVisual;
import net.zaharenko424.casualties_cubed.client.moodles.TemperatureMoodle;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(TemperatureMoodle.class)
public abstract class TemperatureMoodleMixin extends AbstractMoodleVisual {

    @Shadow
    boolean low;

    @WrapMethod(method = "getTooltip", remap = false)
    List<Component> getTooltip(Player player, Operation<List<Component>> original) {
        if (!AxiomPainConfig.INSTANCE.isRobot(player)) return original.call(player);

        List<Component> componentList = new ArrayList();
        switch (this.getMoodleStatus()) {
            case LIGHT:
                if (this.low) {
                    componentList.add(Component.literal("Cold"));
                    componentList.add(Component.literal("Internals are a bit cold. Operations may start getting affected soon").withStyle(ChatFormatting.GRAY));
                } else {
                    componentList.add(Component.literal("Warm"));
                    componentList.add(Component.literal("Internals are a bit warm. Operations may start getting affected soon").withStyle(ChatFormatting.GRAY));
                }
                break;
            case NORMAL:
                if (this.low) {
                    componentList.add(Component.literal("Cold"));
                    componentList.add(Component.literal("Internals are cold. Operations are starting to slow down").withStyle(ChatFormatting.GRAY));
                } else {
                    componentList.add(Component.literal("Warm"));
                    componentList.add(Component.literal("Internals are warm. Operations are starting to slow down").withStyle(ChatFormatting.GRAY));
                }
                break;
            case HEAVY:
                if (this.low) {
                    componentList.add(Component.literal("Freezing").withStyle(ChatFormatting.GOLD));
                    componentList.add(Component.literal("Internals are freezing. Operations are struggling").withStyle(ChatFormatting.GRAY));
                } else {
                    componentList.add(Component.literal("Overheating").withStyle(ChatFormatting.GOLD));
                    componentList.add(Component.literal("Internals are too hot. Operations are struggling").withStyle(ChatFormatting.GRAY));
                }
                break;
            case CRITICAL:
                if (this.low) {
                    // This shouldn't happen
                    componentList.add(Component.literal("Freezing").withStyle(ChatFormatting.RED));
                    componentList.add(Component.literal("Internals are frozen. Damage to internals inbound").withStyle(ChatFormatting.GRAY));
                } else {
                    componentList.add(Component.literal("Burning up").withStyle(ChatFormatting.RED));
                    componentList.add(Component.literal("Internals are burning. Damage to internals inbound").withStyle(ChatFormatting.GRAY));
                }
        }
        return componentList;
    }
}
