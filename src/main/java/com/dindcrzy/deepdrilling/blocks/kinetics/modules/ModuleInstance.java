package com.dindcrzy.deepdrilling.blocks.kinetics.modules;

import com.dindcrzy.deepdrilling.DDPartialModels;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class ModuleInstance<T extends BaseModuleBlockEntity> extends SingleRotatingInstance<T> {
	public ModuleInstance(MaterialManager materialManager, T blockEntity) {
		super(materialManager, blockEntity);
	}

	@Override
	protected Instancer<RotatingData> getModel() {
		BlockState referenceState = blockEntity.getBlockState();
		Direction facing = Direction.fromAxisAndDirection(referenceState.getValue(BaseModuleBlock.AXIS), Direction.AxisDirection.POSITIVE);
		return getRotatingMaterial().getModel(DDPartialModels.MODULE_SHAFT, referenceState, facing);
	}
}
