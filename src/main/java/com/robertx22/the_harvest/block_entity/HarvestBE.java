package com.robertx22.the_harvest.block_entity;

import com.robertx22.library_of_exile.database.relic.stat.RelicStatsContainer;
import com.robertx22.library_of_exile.dimension.MapDimensions;
import com.robertx22.library_of_exile.dimension.device.IMapDeviceBlockEntity;
import com.robertx22.library_of_exile.dimension.device.MapDeviceKind;
import com.robertx22.the_harvest.block.HarvestBlock;
import com.robertx22.the_harvest.item.HarvestItemNbt;
import com.robertx22.the_harvest.item.HarvestMapItem;
import com.robertx22.the_harvest.main.HarvestEntries;
import com.robertx22.the_harvest.structure.HarvestMapCap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.function.Supplier;

public class HarvestBE extends BlockEntity implements ContainerListener, IMapDeviceBlockEntity {


    public boolean gaveMap = false;
    public int x = -1;
    public int z = -1;
    public String currentWorldUUID = "";

    // slot 0 = the harvest map, 1..4 = relics. see IMapDeviceBlockEntity
    public SimpleContainer deviceInv = new SimpleContainer(SIZE);

    private static final String INV_KEY = "device_inv";

    @Override
    public boolean isActivated() {
        if (currentWorldUUID.isEmpty() || !currentWorldUUID.equals(HarvestMapCap.get(ServerLifecycleHooks.getCurrentServer().overworld()).data.data.uuid)) {
            return false;
        }
        return x != -1 || z != -1;
    }

    public void setGaveMap() {
        this.gaveMap = true;
        this.setChanged();
    }

    public HarvestBE(BlockPos pPos, BlockState pBlockState) {
        super(HarvestEntries.HARVEST_BE.get(), pPos, pBlockState);
        this.deviceInv.addListener(this);
    }

    // ------------------------------------------------------------------ IMapDeviceBlockEntity

    @Override
    public SimpleContainer getDeviceInventory() {
        return deviceInv;
    }

    @Override
    public MapDeviceKind getDeviceKind() {
        return MapDeviceKind.HARVEST;
    }

    @Override
    public boolean hasMapSlot(Level level) {
        // inside a map the harvest hands out one free run instead of taking a map item
        return !MapDimensions.isMap(level);
    }

    @Override
    public boolean acceptsMapItem(ItemStack stack) {
        return !stack.isEmpty() && HarvestItemNbt.HARVEST_MAP.has(stack);
    }

    @Override
    public boolean isFreeRunAvailable(Level level) {
        return MapDimensions.isMap(level) && !gaveMap;
    }

    @Override
    public boolean startMap(Player player, Supplier<RelicStatsContainer> relicStats) {
        ItemStack stack;
        if (isFreeRunAvailable(player.level())) {
            setGaveMap();
            stack = HarvestMapItem.blankMap(HarvestEntries.HARVEST_MAP_ITEM.get().getDefaultInstance(), true);
        } else {
            stack = deviceInv.getItem(MAP_SLOT);
            if (!acceptsMapItem(stack)) {
                return false;
            }
        }
        HarvestBlock.startNewMap(player, stack, this, relicStats);
        return true;
    }

    @Override
    public boolean joinMap(Player player) {
        if (!isActivated()) {
            return false;
        }
        HarvestBlock.joinCurrentMap(player, this);
        return true;
    }

    // ------------------------------------------------------------------ nbt

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean("gave", gaveMap);
        nbt.putInt("xp", x);
        nbt.putInt("zp", z);
        nbt.putString("uid", currentWorldUUID);
        nbt.put(INV_KEY, deviceInv.createTag());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.gaveMap = pTag.getBoolean("gave");
        this.x = pTag.getInt("xp");
        this.z = pTag.getInt("zp");
        this.currentWorldUUID = pTag.getString("uid");
        if (pTag.contains(INV_KEY)) {
            deviceInv.fromTag(pTag.getList(INV_KEY, 10));
        }
    }

    @Override
    public void containerChanged(Container pContainer) {
        this.setChanged();
    }

}
