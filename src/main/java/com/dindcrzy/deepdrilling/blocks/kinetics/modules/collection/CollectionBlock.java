package com.dindcrzy.deepdrilling.blocks.kinetics.modules.collection;

import com.dindcrzy.deepdrilling.blocks.DDBlockEntities;
import com.dindcrzy.deepdrilling.blocks.kinetics.modules.BaseModuleBlock;

import com.simibubi.create.AllItems;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.logistics.vault.ItemVaultBlockEntity;
import com.simibubi.create.foundation.item.ItemHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CollectionBlock extends BaseModuleBlock<CollectionBlockEntity> {
	public CollectionBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Class<CollectionBlockEntity> getBlockEntityClass() {
		return CollectionBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends CollectionBlockEntity> getBlockEntityType() {
		return DDBlockEntities.DRILL_COLLECTION.get();
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (AllItems.WRENCH.isIn(player.getItemInHand(hand))) {
			return super.use(state, level, pos, player, hand, hit);
		}

		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		if (level.getBlockEntity(pos) instanceof CollectionBlockEntity collectionBlockEntity) {
			if (collectionBlockEntity.canPlayerUse(player)) {
				collectionBlockEntity.givePlayerItems(player, level);
				return InteractionResult.CONSUME;
			}
		}

		return super.use(state, level, pos, player, hand, hit);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof CollectionBlockEntity collectionBlockEntity) {
				ItemHelper.dropContents(level, pos, collectionBlockEntity.items);
			}
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}
}
