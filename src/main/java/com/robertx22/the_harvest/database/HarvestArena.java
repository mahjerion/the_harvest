package com.robertx22.the_harvest.database;

import com.robertx22.library_of_exile.database.mob_list.MobList;
import com.robertx22.library_of_exile.database.mob_list.MobListTags;
import com.robertx22.library_of_exile.dimension.structure.SimplePrebuiltMapData;
import com.robertx22.library_of_exile.registry.ExileRegistryType;
import com.robertx22.library_of_exile.registry.IAutoGson;
import com.robertx22.library_of_exile.registry.JsonExileRegistry;
import com.robertx22.library_of_exile.tags.ExileTagRequirement;

public class HarvestArena implements JsonExileRegistry<HarvestArena>, IAutoGson<HarvestArena> {
    public static HarvestArena SERIALIZER = new HarvestArena(new SimplePrebuiltMapData(1, ""), 0, "empty");

    public SimplePrebuiltMapData simple_prebuilt_map = null;

    public int weight = 1000;
    public String id = "";
    public ExileTagRequirement<MobList> mob_list_tag_check = new ExileTagRequirement().createBuilder().mustHave(MobListTags.HARVEST).build();

    public HarvestArena(SimplePrebuiltMapData simple_prebuilt_map, int weight, String id) {
        this.simple_prebuilt_map = simple_prebuilt_map;
        this.weight = weight;
        this.id = id;
    }

    @Override
    public ExileRegistryType getExileRegistryType() {
        return HarvestDatabase.HARVEST_ARENA;
    }

    @Override
    public Class<HarvestArena> getClassForSerialization() {
        return HarvestArena.class;
    }

    @Override
    public String GUID() {
        return id;
    }

    @Override
    public int Weight() {
        return weight;
    }
}
