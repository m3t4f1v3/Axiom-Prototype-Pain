package com.axiom.axiom_pain.items

import com.axiom.axiom_pain.init.AxiomMedicalFluids
import net.adinvas.casualties_cubed.fluid_system.ModFluids
import net.adinvas.casualties_cubed.fluid_system.MultiTankHelper
import net.adinvas.casualties_cubed.item.multi_tank.SyringeItem
import net.minecraft.world.item.ItemStack
import net.minecraftforge.fluids.FluidStack

class BloodBagItem: SyringeItem() {
    override fun getCapacity(): Int {
        return 750
    }

    override fun setupDefault(pStack: ItemStack) {
        MultiTankHelper.addMedicalFluid(
            pStack,
            750.0f,
            AxiomMedicalFluids.BLOOD.getId().toString(),
            FluidStack(ModFluids.SRC_MEDICAL.get().getSource(), 1)
        )
    }
}