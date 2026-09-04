package com.robertx22.the_harvest.block;

import com.robertx22.library_of_exile.components.PlayerDataCapability;
import com.robertx22.library_of_exile.database.relic.stat.RelicStatsContainer;
import com.robertx22.library_of_exile.events.base.ExileEvents;
import com.robertx22.library_of_exile.utils.TeleportUtils;
import com.robertx22.the_harvest.block_entity.HarvestBE;
import com.robertx22.the_harvest.item.HarvestItemMapData;
import com.robertx22.the_harvest.item.HarvestItemNbt;
import com.robertx22.the_harvest.main.HarvestMain;
import com.robertx22.the_harvest.structure.HarvestMapCap;
import com.robertx22.the_harvest.structure.HarvestMapData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class HarvestBlock extends BaseEntityBlock {
    public HarvestBlock() {
        super(BlockBehaviour.Properties.of().strength(10).noOcclusion().lightLevel(x -> 10));
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {

        List<ItemStack> all = new ArrayList<>();

        BlockEntity blockentity = pParams.getOptionalParameter(LootContextParams.BLOCK_ENTITY);

        if (blockentity instanceof HarvestBE be) {
            all.add(asItem().getDefaultInstance());

            for (int i = 0; i < be.deviceInv.getContainerSize(); i++) {
                var s = be.deviceInv.getItem(i);
                if (!s.isEmpty()) {
                    all.add(s.copy());
                }
            }
        }

        return all;
    }


    /**
     * @param relics resolved once, right before the instance data is written. The device GUI consumes relic
     *               uses inside this supplier. May yield null when no relics are slotted.
     */
    public static void startNewMap(Player p, ItemStack stack, HarvestBE be, Supplier<RelicStatsContainer> relics) {

        HarvestItemMapData map = HarvestItemNbt.HARVEST_MAP.loadFrom(stack);

        var count = map.getOrSetStartPos(p.level(), stack);
        var start = HarvestMain.HARVEST_MAP_STRUCTURE.getStartFromCounter(count.x, count.z);
        var pos = TeleportUtils.getSpawnTeleportPos(HarvestMain.HARVEST_MAP_STRUCTURE, start.getMiddleBlockPosition(5));

        var pdata = PlayerDataCapability.get(p);

        var data = new HarvestMapData();

        data.onStartMap(p);

        data.item = map;
        data.x = start.x;
        data.z = start.z;

        // kept on the harvest's own data rather than in LibMapCap: that store is keyed by grid position
        // with no dimension, so a harvest instance would overwrite the dungeon instance on the same key.
        // HarvestMain's GRAB_LIB_MAP_DATA listener hands these back to LibMapCap.getData.
        RelicStatsContainer relicStats = relics == null ? null : relics.get();
        if (relicStats != null) {
            data.relicStats = relicStats;
            data.hasRelics = true;
        }

        be.x = count.x;
        be.z = count.z;

        be.currentWorldUUID = HarvestMapCap.get(p.level()).data.data.uuid;

        be.setChanged();

        // the stack is the one in the device's map slot (or the free in-map blank), so this empties the slot
        stack.shrink(1);
        be.deviceInv.setChanged();

        HarvestMapCap.get(p.level()).data.data.setData(p, data, HarvestMain.HARVEST_MAP_STRUCTURE, start.getMiddleBlockPosition(5));

        // the instance is created here, so this is the one entry that gets the spawn grace
        pdata.mapTeleports.entranceTeleportLogic(p, HarvestMain.DIMENSION_KEY, pos, true);

    }

    public static void joinCurrentMap(Player p, HarvestBE be) {

        var start = HarvestMain.HARVEST_MAP_STRUCTURE.getStartFromCounter(be.x, be.z);
        var pos = TeleportUtils.getSpawnTeleportPos(HarvestMain.HARVEST_MAP_STRUCTURE, start.getMiddleBlockPosition(5));
        var pdata = PlayerDataCapability.get(p);
        // no grace: this rejoins an instance that is already running, whether that's the owner coming
        // back or someone joining their run. its mobs already exist, so the grace would protect nobody
        // and anyInGrace would stall the harvest logic for everyone in it.
        pdata.mapTeleports.entranceTeleportLogic(p, HarvestMain.DIMENSION_KEY, pos, false);
    }


    @Override
    public InteractionResult use(BlockState pState, Level world, BlockPos pPos, Player p, InteractionHand pHand, BlockHitResult pHit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        var be = world.getBlockEntity(pPos);

        if (!(be instanceof HarvestBE)) {
            HarvestMain.debugMsg(p, "Missing Block entity");
            return InteractionResult.SUCCESS;
        }

        // slotting the map and relics, starting and joining all go through the shared device GUI, which
        // the main mod opens for this player
        ExileEvents.OPEN_MAP_DEVICE.callEvents(new ExileEvents.OpenMapDeviceEvent(p, world, pPos));
        return InteractionResult.SUCCESS;
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new HarvestBE(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return new BlockEntityTicker<T>() {
            @Override
            public void tick(Level pLevel, BlockPos pPos, BlockState pState, T pBlockEntity) {
                // todo
            }
        };
    }

}
