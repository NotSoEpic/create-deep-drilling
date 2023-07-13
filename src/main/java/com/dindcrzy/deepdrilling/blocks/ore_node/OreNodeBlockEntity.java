package com.dindcrzy.deepdrilling.blocks.ore_node;

import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class OreNodeBlockEntity extends SyncedBlockEntity {
	private ResourceLocation loot;
	private BlockState referenceModel;
	public OreNodeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	public void setLoot(ResourceLocation location) {
		if (location == null) {
			loot = new ResourceLocation("null");
		} else {
			loot = location;
		}
	}
	public ResourceLocation getLoot() {
		return loot == null ? new ResourceLocation("null") : loot;
	}

	public void setReferenceModel(BlockState state) {
		if (state == null || state.isAir()) {
			referenceModel = Blocks.BEDROCK.defaultBlockState();
		} else {
			referenceModel = state;
		}
	}
	public BlockState getReferenceModel() {
		return referenceModel == null ? Blocks.BEDROCK.defaultBlockState() : referenceModel;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		setLoot(new ResourceLocation(tag.getString("LootTable")));
		setReferenceModel(NbtUtils.readBlockState(tag.getCompound("Model")));
		if (level != null && !level.isClientSide) {
			setChanged();
			sendData();
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putString("LootTable", getLoot().toString());
		tag.put("Model", NbtUtils.writeBlockState(getReferenceModel()));
	}
}
