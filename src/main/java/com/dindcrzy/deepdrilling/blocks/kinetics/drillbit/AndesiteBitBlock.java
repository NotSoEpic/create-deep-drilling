package com.dindcrzy.deepdrilling.blocks.kinetics.drillbit;

import com.dindcrzy.deepdrilling.blocks.DDBlockEntities;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

public class AndesiteBitBlock extends BaseBitBlock<AndesiteBitBlockEntity> {
	@Override
	public Class<AndesiteBitBlockEntity> getBlockEntityClass() {
		return AndesiteBitBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends AndesiteBitBlockEntity> getBlockEntityType() {
		return DDBlockEntities.ANDESITE_DRILL_BIT.get();
	}

	public AndesiteBitBlock(Properties properties) {
		super(properties);
	}
}
