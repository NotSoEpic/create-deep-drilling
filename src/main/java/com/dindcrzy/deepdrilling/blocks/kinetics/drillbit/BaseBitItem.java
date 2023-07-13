package com.dindcrzy.deepdrilling.blocks.kinetics.drillbit;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class BaseBitItem extends BlockItem {
	public BaseBitItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return true;
	}

	public static int getDamage(ItemStack item) {
		CompoundTag compound = item.getOrCreateTag();
		return compound.getInt("Damage");
	}
}
