package com.robertx22.the_harvest.api;

import com.robertx22.library_of_exile.events.base.ExileEventCaller;

public class HarvestExileEvents {

    public static ExileEventCaller<GetHarvestLootBonusEvent> GET_HARVEST_LOOT_BONUS = new ExileEventCaller<>();
    public static ExileEventCaller<HarvestCompletedEvent> HARVEST_COMPLETED = new ExileEventCaller<>();

    public static void init() {

    }
}
