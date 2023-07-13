package com.dindcrzy.deepdrilling.blocks.kinetics.boredrill;

import com.dindcrzy.deepdrilling.DDPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;

import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class BoreDrillRenderer extends KineticBlockEntityRenderer<BoreDrillBlockEntity> {
	public BoreDrillRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected SuperByteBuffer getRotatedModel(BoreDrillBlockEntity be, BlockState state) {
		return CachedBufferer.partialFacing(DDPartialModels.BORE_DRILL_SHAFT, state);
	}
}
