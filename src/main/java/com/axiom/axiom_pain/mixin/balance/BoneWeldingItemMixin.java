package com.axiom.axiom_pain.mixin.balance;

import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.item.api.INbtDrivenDurability;
import net.zaharenko424.casualties_cubed.item.usable.BoneWeldingItem;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BoneWeldingItem.class)
public abstract class BoneWeldingItemMixin implements INbtDrivenDurability {

    @Overwrite(remap = false)
    public void onMedicalUse(ServerPlayer source, ServerPlayer target, Limb limb, ItemStack stack) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent((h) -> {
            h.applyPain(limb, h.getLimb(limb).getPain() - 25.0F);
            h.applyMuscleDamage(limb, h.getLimb(limb).getMuscleHealth() - 26.0F, target);
            h.getLimb(limb).setFracture(0f);
            h.getLimb(limb).setBleedRate(h.getLimb(limb).getBleedRate() + 4E-4F);
            h.applyPain(limb, h.getLimb(limb).getPain() + 30.0F);
            h.setBloodViscosity(h.getBloodViscosity() + 2.0F);
        });
        if (!source.isCreative()) {
            this.subNbtDurability(stack, 50.0F);
        }
    }
}
