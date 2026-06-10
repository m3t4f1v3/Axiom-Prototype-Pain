package com.axiom.axiom_pain.init

import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.fluids.ForgeFlowingFluid
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import net.zaharenko424.casualties_cubed.fluid_system.MedicalFluid
import net.zaharenko424.casualties_cubed.fluid_system.MedicalFluidType


object AxiomMedicalFluids {

    val FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, "axiom_pain")
    val FLUIDS = DeferredRegister.create(ForgeRegistries.Keys.FLUIDS, "axiom_pain")

    val BLOOD_TYPE: RegistryObject<FluidType?>? =
        FLUID_TYPES.register("blood") { MedicalFluidType(FluidType.Properties.create(), 8192000) }

    val BLOOD: RegistryObject<MedicalFluid?>? = FLUIDS.register(
        "blood",
        {
            MedicalFluid(
                ForgeFlowingFluid.Properties(AxiomMedicalFluids.BLOOD_TYPE, AxiomMedicalFluids.BLOOD, AxiomMedicalFluids.BLOOD),
                AxiomMedicalEffects.BLOOD
            )
        })

    val OIL_TYPE: RegistryObject<FluidType?>? =
        FLUID_TYPES.register("oil") { MedicalFluidType(FluidType.Properties.create(), 16) }

    val OIL: RegistryObject<MedicalFluid?>? = FLUIDS.register(
        "oil",
        {
            MedicalFluid(
                ForgeFlowingFluid.Properties(AxiomMedicalFluids.OIL_TYPE, AxiomMedicalFluids.OIL, AxiomMedicalFluids.OIL),
                AxiomMedicalEffects.OIL
            )
        })


    fun register(bus: IEventBus?) {
        FLUID_TYPES.register(bus)
        FLUIDS.register(bus)
    }
}