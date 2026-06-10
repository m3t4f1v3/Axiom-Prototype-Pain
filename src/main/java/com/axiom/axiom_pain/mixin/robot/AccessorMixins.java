package com.axiom.axiom_pain.mixin.robot;

import net.zaharenko424.casualties_cubed.client.gui.HealthScreen;
import net.zaharenko424.casualties_cubed.client.gui.minigames.*;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(HealthScreen.class)
interface HealthScreenAccessor {

    @Accessor("target")
    Player getPlayer();
}

@Mixin(BandageMinigameScreen.class)
interface BandageScreenAccessor {

    @Accessor("target")
    Player getPlayer();
}

@Mixin(AmputationMinigameScreen.class)
interface AmputationScreenAccessor {

    @Accessor("target")
    Player getPlayer();
}

@Mixin(DislocationMinigameScreen.class)
interface DislocationScreenAccessor {

    @Accessor("target")
    Player getPlayer();
}

@Mixin(CPRMinigameScreen.class)
interface CPRScreenAccessor {

    @Accessor("target")
    Player getPlayer();
}

@Mixin(ShrapnelMinigameScreen.class)
interface ShrapnelScreenAccessor {

    @Accessor("target")
    Player getPlayer();
}

@Mixin(InjectMingameScreen.class)
interface InjectScreenAccessor {

    @Accessor("target")
    Player getPlayer();
}