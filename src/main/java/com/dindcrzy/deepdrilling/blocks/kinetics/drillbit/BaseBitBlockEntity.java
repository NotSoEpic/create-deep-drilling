package com.dindcrzy.deepdrilling.blocks.kinetics.drillbit;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class BaseBitBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {
	protected int damage;
	public BaseBitBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}

	public abstract int getDurability();

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		if (super.addToGoggleTooltip(tooltip, isPlayerSneaking)) {
			double fractDamage = (double)damage / getDurability();
			ChatFormatting formatting;
			if (fractDamage < 0.25) {
				formatting = ChatFormatting.GREEN;
			} else if (fractDamage < 0.5) {
				formatting = ChatFormatting.YELLOW;
			} else if (fractDamage < 0.75) {
				formatting = ChatFormatting.GOLD;
			} else {
				formatting = ChatFormatting.RED;
			}
			Lang.number(getDurability() - damage)
					.style(formatting)
					.space()
					.add(Lang.text(String.format("/ %s", getDurability()))
						.style(ChatFormatting.GRAY))
					.forGoggles(tooltip);
			return true;
		}
		return false;
	}

	public ItemStack setItemDamage(ItemStack item) {
		CompoundTag tag = item.getOrCreateTag();
		tag.putInt("Damage", damage);
		return item;
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		damage = compound.getInt("Damage");
	}

	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		compound.putInt("Damage", damage);
	}

	public int getDamage() {
		return damage;
	}
	public void addDamage(int d) {
		damage += d;
		if (damage >= getDurability()) {
			level.destroyBlock(getBlockPos(), false);
		}
		setChanged();
		sendData();
	}
}
