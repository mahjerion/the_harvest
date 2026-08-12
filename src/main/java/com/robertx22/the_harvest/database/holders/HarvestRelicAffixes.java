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

    // empty relic_type means "any relic type can roll this" - see RelicGenerator
    static String IMPLICIT_TYPE = "";

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

    // Implicit, rolled into a relic's dedicated implicit slot. Empty relic_type on purpose: a league
    // mechanic belongs to whichever mod registered it, not to a relic type, so any relic can roll it.
    // Flat 100 so the guarantee doesn't depend on the affix roll.
    public ExileKey<RelicAffix, KeyInfo> GUARANTEE_CONTENT = ExileKey.ofId(this, "guarantee_harvest_content", x -> {
        return new RelicAffix(x.GUID(), IMPLICIT_TYPE, new RelicMod(HarvestRelicStats.INSTANCE.GUARANTEE_CONTENT, 100, 100)).setImplicit();
    });

    @Override
    public void loadClass() {

    }
}
