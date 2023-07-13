package com.dindcrzy.deepdrilling.blocks.kinetics.modules.collection;

import com.dindcrzy.deepdrilling.blocks.kinetics.modules.BaseModuleBlockEntity;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandlerContainer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("UnstableApiUsage")
public class CollectionBlockEntity extends BaseModuleBlockEntity implements Container {
	public final ItemStackHandlerContainer items;
	public CollectionBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
		items = new ItemStackHandlerContainer(18);
	}

	@Override
	public boolean canPlayerUse(Player player) {
		return super.canPlayerUse(player) && !isEmpty();
	}

	public void givePlayerItems(Player player, Level level) {
		for (int i = 0; i < 18; i++) {
			ItemStack item = items.getStackInSlot(i);
			if (!player.addItem(item)) { // failed to add whole stack to player inventory
				ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), item.copy());
				itemEntity.setDeltaMovement(Vec3.ZERO);
				itemEntity.setDefaultPickUpDelay();
				level.addFreshEntity(itemEntity);
				items.setStackInSlot(i, ItemStack.EMPTY);
			}
		}
	}

	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		compound.put("Inventory", items.serializeNBT());
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		items.deserializeNBT(compound.getCompound("Inventory"));
	}

	// returns leftover count
	// I have no clue what I'm doing, or if this is extremely overkill
	public long tryQuickStack(ItemStack stack) {
		Transaction transaction = Transaction.openOuter();
		long count = items.insert(ItemVariant.of(stack), stack.getCount(), transaction);
		transaction.commit();
		return stack.getCount() - count;
	}

	@Override
	public int getContainerSize() {
		return items.getContainerSize();
	}

	@Override
	public boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		return items.getItem(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		return items.removeItem(slot, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return items.removeItemNoUpdate(slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		items.setItem(slot, stack);
	}

	@Override
	public boolean stillValid(Player player) {
		return items.stillValid(player);
	}

	@Override
	public void clearContent() {
		items.clearContent();
	}
}
