package com.dindcrzy.deepdrilling.blocks.kinetics.drillbit;

import com.dindcrzy.deepdrilling.DDPartialModels;
import com.dindcrzy.deepdrilling.blocks.DDBlocks;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BaseBitInstance<T extends BaseBitBlockEntity> extends SingleRotatingInstance<T> {
	public BaseBitInstance(MaterialManager materialManager, T blockEntity) {
		super(materialManager, blockEntity);
	}

	@Override
	protected Instancer<RotatingData> getModel() {
		BlockState referenceState = blockEntity.getBlockState();
		Direction facing = referenceState.getValue(BlockStateProperties.FACING);
		return getRotatingMaterial().getModel(findModel(referenceState), referenceState, facing);
	}

	public static PartialModel findModel(BlockState state) {
		if (DDBlocks.ANDESITE_DRILL_BIT.has(state)) {
			return DDPartialModels.DRILL_BIT_ANDESITE;
		} else if (DDBlocks.BRASS_DRILL_BIT.has(state)) {
			return DDPartialModels.DRILL_BIT_BRASS;
		}
		return DDPartialModels.DRILL_BIT_ANDESITE;
	}
}
