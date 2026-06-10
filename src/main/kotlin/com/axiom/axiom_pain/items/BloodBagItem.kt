package com.axiom.axiom_pain.items

import com.axiom.axiom_pain.init.AxiomMedicalFluids
import net.minecraft.world.item.ItemStack
import net.zaharenko424.casualties_cubed.item.multi_tank.SyringeItem

class BloodBagItem: SyringeItem() {
    override fun getCapacity(): Int {
        return 750
    }

    override fun withDefFluid(): ItemStack {
        return withFluid(AxiomMedicalFluids.BLOOD)
    }
}