package com.robertx22.the_harvest.structure;

import com.robertx22.library_of_exile.utils.LoadSave;
import com.robertx22.the_harvest.main.HarvestMain;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HarvestMapCap implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public Level world;

    public static final ResourceLocation RESOURCE = new ResourceLocation(HarvestMain.MODID, "world_data");
    public static Capability<HarvestMapCap> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {
    });

    transient final LazyOptional<HarvestMapCap> supp = LazyOptional.of(() -> this);

    public HarvestMapCap(Level world) {
        this.world = world;
    }

    public static HarvestMapCap get(Level entity) {
        return entity.getServer().overworld().getCapability(INSTANCE).orElse(new HarvestMapCap(entity));
    }

    public HarvestWorldData data = new HarvestWorldData();

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == INSTANCE) {
            return supp.cast();
        }
        return LazyOptional.empty();

    }

    @Override
    public CompoundTag serializeNBT() {
        var nbt = new CompoundTag();

        try {
            LoadSave.Save(data, nbt, "data");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

        try {
            this.data = LoadSave.loadOrBlank(HarvestWorldData.class, new HarvestWorldData(), nbt, "data", new HarvestWorldData());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
