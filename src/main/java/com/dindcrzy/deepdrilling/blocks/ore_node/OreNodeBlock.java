package com.dindcrzy.deepdrilling.blocks.ore_node;

import com.dindcrzy.deepdrilling.blocks.DDBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

public class OreNodeBlock extends BaseEntityBlock {
	public OreNodeBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new OreNodeBlockEntity(DDBlockEntities.ORE_NODE.get(), pos, state);
	}

	public static ResourceLocation getLootTable(ItemStack stack) {
		CompoundTag compoundTag = stack.getOrCreateTag();
		return new ResourceLocation(compoundTag.getString("LootTable"));
	}

	public static BlockState getModel(ItemStack stack) {
		CompoundTag compoundTag = stack.getOrCreateTag();
		return NbtUtils.readBlockState(compoundTag.getCompound("Model"));
	}

	/*@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);

		if (!level.isClientSide && level.getBlockEntity(pos) instanceof OreNodeBlockEntity oreNodeBlockEntity) {
			oreNodeBlockEntity.setLoot(getLootTable(stack));
			oreNodeBlockEntity.setReferenceModel(getModel(stack));
			oreNodeBlockEntity.setChanged();
			oreNodeBlockEntity.sendData();
		}
	}*/

	@Override
	protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
		if (level.getBlockEntity(pos) instanceof OreNodeBlockEntity oreNodeBlockEntity) {
			super.spawnDestroyParticles(level, player, pos, oreNodeBlockEntity.getReferenceModel());
		} else {
			super.spawnDestroyParticles(level, player, pos, Blocks.BEDROCK.defaultBlockState());
		}
	}
}
