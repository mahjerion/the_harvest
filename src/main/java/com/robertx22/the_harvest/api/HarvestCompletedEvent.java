package com.robertx22.the_harvest.api;

import com.robertx22.library_of_exile.events.base.ExileEvent;
import net.minecraft.world.entity.player.Player;

import java.util.List;

// Notification fired exactly once, when a Harvest instance's timer runs out (see HarvestMapData.tickSecond).
// the_harvest can't see the main mod's player Stat/buff pipeline, so it hands off the list of players
// present to the main mod glue package, which grants the Atlas "Bountiful Aftermath" buff to anyone
// with the perk allocated.
public class HarvestCompletedEvent extends ExileEvent {

    public final List<Player> players;

    public HarvestCompletedEvent(List<Player> players) {
        this.players = players;
    }
}
