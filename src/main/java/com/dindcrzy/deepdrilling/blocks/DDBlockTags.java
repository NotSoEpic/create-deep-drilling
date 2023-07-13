package com.dindcrzy.deepdrilling.blocks;

import com.dindcrzy.deepdrilling.DeepDrilling;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class DDBlockTags {
	public static final TagKey<Block> DRILL_THROUGH = new TagKey<>(Registry.BLOCK.key(), DeepDrilling.id("drill_through"));
}
