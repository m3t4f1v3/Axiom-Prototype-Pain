package com.axiom.axiom_pain.moodles

import net.adinvas.casualties_cubed.client.moodles.MoodleController.registerMoodle

object AxiomMoodleController {
    init {
        registerMoodle(GunkMoodle())
    }

    fun register() {

    }
}