package com.dindcrzy.deepdrilling.blocks.kinetics.drillbit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BrassBitBlockEntity extends BaseBitBlockEntity {
	public BrassBitBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}

	public static int DURABILITY = 500;

	public int getDurability() {
		return DURABILITY;
	}
}
