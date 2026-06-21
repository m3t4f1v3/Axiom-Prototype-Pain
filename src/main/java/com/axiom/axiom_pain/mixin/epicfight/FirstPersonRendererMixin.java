package com.axiom.axiom_pain.mixin.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.FirstPersonRenderer;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;

@Mixin(FirstPersonRenderer.class)
public class FirstPersonRendererMixin {
    @Inject(
            method = "render(Lnet/minecraft/client/player/LocalPlayer;Lyesman/epicfight/client/world/capabilites/entitypatch/player/LocalPlayerPatch;Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;Lnet/minecraft/client/renderer/MultiBufferSource;Lcom/mojang/blaze3d/vertex/PoseStack;IF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderType;entityCutoutNoCull(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;",
                    ordinal = 1
            )
    )
    private void hideAmputatedArmsFirstPerson(LocalPlayer entity, LocalPlayerPatch localPlayerPatch,
                                                 LivingEntityRenderer<LocalPlayer, PlayerModel<LocalPlayer>> renderer,
                                                 MultiBufferSource buffer, PoseStack poseStack,
                                                 int packedLight, float partialTicks, CallbackInfo ci) {

        HumanoidMesh mesh = ((FirstPersonRenderer) (Object) this).getMeshProvider(localPlayerPatch).get();

        entity.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(data -> {
            if (data.isAmputated(Limb.LEFT_ARM)) {
                mesh.leftArm.setHidden(true);
                mesh.leftSleeve.setHidden(true);
            }
            if (data.isAmputated(Limb.RIGHT_ARM)) {
                mesh.rightArm.setHidden(true);
                mesh.rightSleeve.setHidden(true);
            }
        });
    }
}
