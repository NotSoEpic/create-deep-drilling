package com.dindcrzy.deepdrilling.blocks.kinetics.modules.efficiency;

import com.dindcrzy.deepdrilling.blocks.DDBlockEntities;
import com.dindcrzy.deepdrilling.blocks.kinetics.modules.BaseModuleBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OverclockBlock extends BaseModuleBlock<OverclockBlockEntity> {
	public OverclockBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Class<OverclockBlockEntity> getBlockEntityClass() {
		return OverclockBlockEntity.class;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public BlockEntityType<? extends OverclockBlockEntity> getBlockEntityType() {
		return DDBlockEntities.DRILL_OVERCLOCK.get();
	}
}
