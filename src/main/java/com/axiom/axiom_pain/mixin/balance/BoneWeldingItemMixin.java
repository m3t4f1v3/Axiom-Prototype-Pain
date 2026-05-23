package com.axiom.axiom_pain.mixin.balance;

import net.adinvas.casualties_cubed.PlayerHealthProvider;
import net.adinvas.casualties_cubed.item.api.INbtDrivenDurability;
import net.adinvas.casualties_cubed.item.usable.BoneWeldingItem;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BoneWeldingItem.class)
public abstract class BoneWeldingItemMixin implements INbtDrivenDurability {


    @Overwrite(remap = false)
    public void onMedicalUse(Limb limb, ServerPlayer source, ServerPlayer target, ItemStack stack) {
        target.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent((h) -> {
            h.setLimbSkinHealth(limb, h.getLimbSkinHealth(limb) - 25.0F);
            h.setLimbMuscleHealth(limb, h.getLimbMuscleHealth(limb) - 26.0F);
            h.setLimbFracture(limb, 0f);
            h.setLimbBleedRate(limb, h.getLimbBleedRate(limb) + 4E-4F);
            h.setLimbPain(limb, h.getLimbPain(limb) + 30.0F);
            h.setBloodViscosity(h.getBloodViscosity() + 2.0F);
        });
        if (!source.isCreative()) {
            this.subNbtDurability(stack, 50.0F);
        }
    }
}
