package com.dindcrzy.deepdrilling.blocks.kinetics.drillbit;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;

import static net.minecraft.data.loot.BlockLoot.applyExplosionCondition;

public abstract class BaseBitBlock<T extends BaseBitBlockEntity> extends DirectionalKineticBlock implements IBE<T> {
	public static final ResourceLocation DAMAGE = new ResourceLocation("damage");
	public BaseBitBlock(Properties properties) {
		super(properties);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof BaseBitBlockEntity baseBitBlockEntity) {
			builder = builder.withDynamicDrop(DAMAGE, (context, consumer) -> consumer.accept(
					baseBitBlockEntity.setItemDamage(state.getBlock().asItem().getDefaultInstance())
			));
		}

		return super.getDrops(state, builder);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public Direction.Axis getRotationAxis(BlockState state) {
		return state.getValue(FACING).getAxis();
	}

	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return face == state.getValue(FACING).getOpposite();
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);

		if (level.getBlockEntity(pos) instanceof BaseBitBlockEntity bitBlockEntity) {
			bitBlockEntity.addDamage(BaseBitItem.getDamage(stack));
		}
	}

	public static void createDrillBitDrop(BlockLoot blockLoot, BaseBitBlock<?> baseBitBlock) {
		blockLoot.add(baseBitBlock, LootTable.lootTable()
			.withPool(
				applyExplosionCondition(
					baseBitBlock,
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(
							LootItem.lootTableItem(baseBitBlock)
								.apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Damage", "Damage"))
						)
				)
			)
		);
	}
}
