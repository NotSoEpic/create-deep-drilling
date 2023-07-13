package com.dindcrzy.deepdrilling.blocks.kinetics.drillbit;

import com.dindcrzy.deepdrilling.DDPartialModels;
import com.dindcrzy.deepdrilling.blocks.kinetics.boredrill.BoreDrillBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;

import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class BaseBitRenderer<T extends BaseBitBlockEntity> extends KineticBlockEntityRenderer<T> {
	public BaseBitRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected SuperByteBuffer getRotatedModel(T be, BlockState state) {
		return CachedBufferer.partialFacing(BaseBitInstance.findModel(state), state);
	}
}
