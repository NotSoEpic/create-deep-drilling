package com.dindcrzy.deepdrilling.blocks.kinetics.drillbit;

import com.dindcrzy.deepdrilling.blocks.DDBlockEntities;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class BrassBitBlock extends BaseBitBlock<BrassBitBlockEntity> {
	@Override
	public Class<BrassBitBlockEntity> getBlockEntityClass() {
		return BrassBitBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends BrassBitBlockEntity> getBlockEntityType() {
		return DDBlockEntities.BRASS_DRILL_BIT.get();
	}

	public BrassBitBlock(Properties properties) {
		super(properties);
	}
}
