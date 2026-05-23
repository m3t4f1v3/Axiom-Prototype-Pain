package com.axiom.axiom_pain.mixin.robot;

import com.axiom.axiom_pain.AxiomPainConfig;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.adinvas.casualties_cubed.client.moodles.DirtynessMoodle;
import net.adinvas.casualties_cubed.client.moodles.MoodleStatus;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DirtynessMoodle.class)
public class DirtynessMoodleMixin {

    @WrapMethod(method = "calculateStatus", remap = false)
    MoodleStatus calculateStatus(Player player, Operation<MoodleStatus> original) {
        if (AxiomPainConfig.INSTANCE.isRobot(player)) return MoodleStatus.NONE;
        return original.call(player);
    }
}
