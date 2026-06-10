package com.axiom.axiom_pain

import com.axiom.axiom_pain.init.AxiomMedicalFluids
import com.axiom.axiom_pain.init.AxiomParticleTypes
import com.axiom.axiom_pain.init.AxiomParticles
import com.axiom.axiom_pain.init.ItemRegistry
import com.axiom.axiom_pain.keybind.KeyBindHandler.registerKeybindings
import net.zaharenko424.casualties_cubed.item.api.INbtDrivenDurability
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import java.util.function.Supplier


@Mod("axiom_pain")
object AxiomPain {

    const val MODID = "axiom_pain"
    val LOGGER: Logger = LogManager.getLogger(MODID)

    init {
        LOGGER.log(Level.INFO, "$MODID has started!")

        MOD_BUS.addListener(::onClientSetup)
        FORGE_BUS.addListener(::onClientTick)
        MOD_BUS.addListener(::buildContents)


        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AxiomPainConfig.SPEC);

        AxiomMedicalFluids.register(MOD_BUS)
        ItemRegistry.register(MOD_BUS)
        AxiomParticleTypes.register(MOD_BUS)

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, Supplier {
            Runnable {
                MOD_BUS.addListener(AxiomParticles::registerParticles)
            }
        })

    }

    fun buildContents(event: BuildCreativeModeTabContentsEvent) {

        if (event.tabKey.location() == ResourceLocation.fromNamespaceAndPath("casualties_cubed", "main")) {
            ItemRegistry.ITEMS.entries.forEach {
                val stack: ItemStack = ItemStack(it.get())


                (it.get() as? INbtDrivenDurability)?.getNbtDurability(stack)

                event.accept(stack)
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client... with Axiom Pain!")
        MOD_BUS.addListener(::registerKeybindings)
    }

    private fun onClientTick(event: TickEvent.ClientTickEvent) {
        BloodParticleRenderer.tick(event)
    }

}