package com.dindcrzy.deepdrilling.blocks.kinetics.modules;

import com.dindcrzy.deepdrilling.blocks.kinetics.boredrill.BoreDrillBlockEntity;
import com.dindcrzy.deepdrilling.blocks.kinetics.modules.collection.CollectionBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class BaseModuleBlockEntity extends KineticBlockEntity {
	protected BoreDrillBlockEntity drill;
	public BaseModuleBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}

	@Nullable
	public BoreDrillBlockEntity findConnectedDrill() {
		BoreDrillBlockEntity candidate = searchDrill(Direction.AxisDirection.POSITIVE);
		if (candidate != null && Objects.equals(candidate, drill)) {
			drill = candidate;
			return drill;
		}
		BoreDrillBlockEntity candidate2 = searchDrill(Direction.AxisDirection.NEGATIVE);
		if (candidate2 != null && Objects.equals(candidate2, drill)) {
			drill = candidate2;
			return drill;
		}
		if (candidate != null) {
			drill = candidate;
			return candidate;
		}
		drill = candidate2;
		return candidate2;
	}

	private static final int searchRange = 5;
	private BoreDrillBlockEntity searchDrill(Direction.AxisDirection axis) {
		BoreDrillBlockEntity candidate = null;
		for (int i = 1; i <= searchRange; i++) {
			Direction dir = Direction.fromAxisAndDirection(
					getBlockState().getValue(BaseModuleBlock.AXIS),
					axis
			);
			BlockEntity blockEntity = level.getBlockEntity(getBlockPos().relative(dir, i));
			if (blockEntity instanceof BoreDrillBlockEntity boreDrill) {
				if (Objects.equals(boreDrill, drill)) {
					return boreDrill;
				}
				candidate = boreDrill;
			} else if (!(blockEntity instanceof BaseModuleBlockEntity)) {
				return candidate;
			}
		}
		return candidate;
	}

	public void clearDrill() {
		drill = null;
	}

	@Override
	public void tick() {
		super.tick();
		if (getSpeed() != 0 && drill == null) {
			if (findConnectedDrill() != null) {
				drill.addModule(this);
			}
		}
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if (drill != null) {
			drill.removeModule(getBlockPos());
		}
	}

	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		if (drill != null)
			compound.put("Drill", NbtUtils.writeBlockPos(drill.getBlockPos()));
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		if (drill != null) {
			BlockEntity blockEntity = level.getBlockEntity(NbtUtils.readBlockPos(compound.getCompound("Drill")));
			if (blockEntity instanceof BoreDrillBlockEntity boreDrillBlockEntity) {
				drill = boreDrillBlockEntity;
				findConnectedDrill();
			}
		}
	}
}
