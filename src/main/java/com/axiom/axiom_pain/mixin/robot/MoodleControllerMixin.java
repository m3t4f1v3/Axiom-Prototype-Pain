package com.axiom.axiom_pain.mixin.robot;

import com.axiom.axiom_pain.moodles.GunkMoodle;
import com.llamalad7.mixinextras.sugar.Local;
import net.zaharenko424.casualties_cubed.client.moodles.AbstractMoodleVisual;
import net.zaharenko424.casualties_cubed.client.moodles.MoodleController;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

//todo: undo once event is pushed
@Mixin(MoodleController.class)
public class MoodleControllerMixin {
    @Final
    @Shadow private static List<AbstractMoodleVisual> moodles;

    @Inject(method = "<init>", at = @At(value = "RETURN"), remap = false)
    private void addGunk(CallbackInfo ci) {
        moodles.add(new GunkMoodle());
    }
}
