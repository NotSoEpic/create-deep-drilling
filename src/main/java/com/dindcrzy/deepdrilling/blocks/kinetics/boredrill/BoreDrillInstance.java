package com.dindcrzy.deepdrilling.blocks.kinetics.boredrill;

import com.dindcrzy.deepdrilling.DDPartialModels;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BoreDrillInstance extends SingleRotatingInstance<BoreDrillBlockEntity> {
	public BoreDrillInstance(MaterialManager materialManager, BoreDrillBlockEntity blockEntity) {
		super(materialManager, blockEntity);
	}

	@Override
	protected Instancer<RotatingData> getModel() {
		BlockState referenceState = blockEntity.getBlockState();
		Direction facing = referenceState.getValue(BlockStateProperties.FACING);
		return getRotatingMaterial().getModel(DDPartialModels.BORE_DRILL_SHAFT, referenceState, facing);
	}
}
