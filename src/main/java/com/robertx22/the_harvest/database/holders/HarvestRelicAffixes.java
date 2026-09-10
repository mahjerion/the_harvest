package com.robertx22.the_harvest.database.holders;

import com.robertx22.library_of_exile.database.relic.affix.RelicAffix;
import com.robertx22.library_of_exile.database.relic.stat.RelicMod;
import com.robertx22.library_of_exile.registry.helpers.ExileKey;
import com.robertx22.library_of_exile.registry.helpers.ExileKeyHolder;
import com.robertx22.library_of_exile.registry.helpers.KeyInfo;
import com.robertx22.library_of_exile.registry.register_info.ModRequiredRegisterInfo;
import com.robertx22.the_harvest.main.HarvestMain;

public class HarvestRelicAffixes extends ExileKeyHolder<RelicAffix> {

    public static HarvestRelicAffixes INSTANCE = new HarvestRelicAffixes(HarvestMain.REGISTER_INFO);

    public HarvestRelicAffixes(ModRequiredRegisterInfo modRegisterInfo) {
        super(modRegisterInfo);
    }

    static String TYPE = HarvestMain.MODID;

    // the dungeon_realm relic type (its id is the modid; this addon doesn't depend on dungeon_realm).
    // Only dungeon relics enter the map device, so a "harvest inside this map" guarantee belongs on them,
    // not on harvest relics, which only go into the harvest altar.
    static String IMPLICIT_TYPE = "dungeon_realm";

    public ExileKey<RelicAffix, KeyInfo> BONUS_HARVEST_CHANCE = ExileKey.ofId(this, "bonus_harvest_chance", x -> {
        return new RelicAffix(x.GUID(), TYPE, new RelicMod(HarvestRelicStats.INSTANCE.BONUS_HARVEST_CHANCE, 3, 25));
    });

    public ExileKey<RelicAffix, KeyInfo> DOUBLE_HARVEST_CHANCE = ExileKey.ofId(this, "double_harvest_chance", x -> {
        return new RelicAffix(x.GUID(), TYPE, new RelicMod(HarvestRelicStats.INSTANCE.DOUBLE_HARVEST_CHANCE, 2, 10));
    });

    public ExileKey<RelicAffix, KeyInfo> MOB_SPAWN_CHANCE = ExileKey.ofId(this, "mob_spawns", x -> {
        return new RelicAffix(x.GUID(), TYPE, new RelicMod(HarvestRelicStats.INSTANCE.MOBS_SPAWNED, 3, 25));
    });

    public ExileKey<RelicAffix, KeyInfo> CONTENT = ExileKey.ofId(this, "harvest_content", x -> {
        return new RelicAffix(x.GUID(), TYPE, new RelicMod(HarvestRelicStats.INSTANCE.CONTENT, 25, 100));
    });

    // Implicit, rolled into a dungeon relic's dedicated implicit slot. Flat 100 so the guarantee doesn't
    // depend on the affix roll.
    public ExileKey<RelicAffix, KeyInfo> GUARANTEE_CONTENT = ExileKey.ofId(this, "guarantee_harvest_content", x -> {
        return new RelicAffix(x.GUID(), IMPLICIT_TYPE, new RelicMod(HarvestRelicStats.INSTANCE.GUARANTEE_CONTENT, 100, 100)).setImplicit();
    });

    @Override
    public void loadClass() {

    }
}
