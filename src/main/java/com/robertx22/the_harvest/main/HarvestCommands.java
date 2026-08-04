package com.robertx22.the_harvest.main;

public class HarvestCommands {

    // "/the_harvest wipe_world_data" used to be registered here, and only here - dungeon_realm and
    // ancient_obelisks never had one, so anyone wiping all three on a restart timer was really only
    // wiping this league. It also replaced HarvestWorldData outright, which reset the instance counter
    // while leaving the map connections and map levels that counter's coordinates point at, and it
    // reported through getSource().getPlayer(), which is null (and so threw) when run from the console.
    // The library now registers the command for every map dimension - see MapDimensionConfig.register.
    public static void init() {
    }


}
