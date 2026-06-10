package com.axiom.axiom_pain.items

import com.axiom.axiom_pain.init.AxiomMedicalFluids
import net.zaharenko424.casualties_cubed.item.multi_tank.SyringeItem
import net.minecraft.world.item.ItemStack
import net.minecraftforge.fluids.FluidStack

class OilBagItem: SyringeItem() {
    override fun getCapacity(): Int {
        return 750
    }

    override fun withDefFluid(): ItemStack {
        return withFluid(AxiomMedicalFluids.OIL)
    }
}