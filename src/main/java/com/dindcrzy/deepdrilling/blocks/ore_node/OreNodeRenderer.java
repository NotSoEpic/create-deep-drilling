package com.dindcrzy.deepdrilling.blocks.ore_node;

import com.mojang.blaze3d.vertex.PoseStack;

import com.simibubi.create.foundation.render.CachedBufferer;

import com.simibubi.create.foundation.render.SuperByteBuffer;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class OreNodeRenderer implements BlockEntityRenderer<OreNodeBlockEntity> {
	public OreNodeRenderer(BlockEntityRendererProvider.Context context) {
	}

	public SuperByteBuffer getModel(OreNodeBlockEntity blockEntity) {
		return CachedBufferer.block(blockEntity.getReferenceModel());
	}

	@Override
	public void render(OreNodeBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		getModel(blockEntity)
				.renderInto(poseStack,
						bufferSource.getBuffer(ItemBlockRenderTypes.getChunkRenderType(blockEntity.getBlockState())
				));
	}
}
