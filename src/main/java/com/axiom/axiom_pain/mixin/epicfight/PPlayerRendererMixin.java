package com.axiom.axiom_pain.mixin.epicfight;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.patched.entity.PPlayerRenderer;
import yesman.epicfight.client.world.capabilites.entitypatch.player.AbstractClientPlayerPatch;

@Mixin(PPlayerRenderer.class)
public class PPlayerRendererMixin {
    @Inject(method = "prepareModel", at = @At("TAIL"), remap = false)
    private void reapplyAmputation(HumanoidMesh mesh, AbstractClientPlayer entity,
                                   AbstractClientPlayerPatch<AbstractClientPlayer> entitypatch,
                                   PlayerRenderer renderer, CallbackInfo ci) {

        entity.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            if (data.isAmputated(Limb.HEAD)) {
                mesh.head.setHidden(true);
                mesh.hat.setHidden(true);
            }
            if (data.isAmputated(Limb.LEFT_ARM)) {
                mesh.leftArm.setHidden(true);
                mesh.leftSleeve.setHidden(true);
            }
            if (data.isAmputated(Limb.RIGHT_ARM)) {
                mesh.rightArm.setHidden(true);
                mesh.rightSleeve.setHidden(true);
            }
            if (data.isAmputated(Limb.LEFT_LEG)) {
                mesh.leftLeg.setHidden(true);
                mesh.leftPants.setHidden(true);
            }
            if (data.isAmputated(Limb.RIGHT_LEG)) {
                mesh.rightLeg.setHidden(true);
                mesh.rightPants.setHidden(true);
            }
        });
    }
}