package com.robertx22.the_harvest.api;

import com.robertx22.library_of_exile.events.base.ExileEvent;
import net.minecraft.world.entity.player.Player;

// Synchronous query, same reasoning as dungeon_realm's Get*Event hooks: the_harvest can't see the
// main mod's player Stat pipeline, so it asks for the Atlas "Harvest Extra Drops" bonus (percent) to
// scale the per-mob-kill Harvest loot-table trigger chance in HarvestMain's LivingDeathEvent listener.
public class GetHarvestLootBonusEvent extends ExileEvent {

    public final Player player;
    public float bonusPercent = 0;

    public GetHarvestLootBonusEvent(Player player) {
        this.player = player;
    }
}
