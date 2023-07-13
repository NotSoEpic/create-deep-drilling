package com.dindcrzy.deepdrilling.blocks;

import com.dindcrzy.deepdrilling.blocks.kinetics.boredrill.BoreDrillBlock;

import com.dindcrzy.deepdrilling.blocks.kinetics.drillbit.AndesiteBitBlock;
import com.dindcrzy.deepdrilling.blocks.kinetics.drillbit.AndesiteBitBlockEntity;
import com.dindcrzy.deepdrilling.blocks.kinetics.drillbit.BaseBitBlock;
import com.dindcrzy.deepdrilling.blocks.kinetics.drillbit.BaseBitItem;
import com.dindcrzy.deepdrilling.blocks.kinetics.drillbit.BrassBitBlock;
import com.dindcrzy.deepdrilling.blocks.kinetics.drillbit.BrassBitBlockEntity;
import com.dindcrzy.deepdrilling.blocks.kinetics.modules.collection.CollectionBlock;
import com.dindcrzy.deepdrilling.blocks.kinetics.modules.efficiency.OverclockBlock;
import com.dindcrzy.deepdrilling.blocks.ore_node.OreNodeBlock;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;

import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

import static com.dindcrzy.deepdrilling.DeepDrilling.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class DDBlocks {
	public static final BlockEntry<OreNodeBlock> ORE_NODE = REGISTRATE
			.block("ore_node", OreNodeBlock::new)
			.properties(p -> p.destroyTime(-1).explosionResistance(3600000.0F))
			.simpleItem()
			.register();

	public static final BlockEntry<BoreDrillBlock> BORE_DRILL = REGISTRATE
			.block("bore_drill", BoreDrillBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.transform(axeOrPickaxe())
			.blockstate(BlockStateGen.directionalBlockProvider(true))
			.transform(BlockStressDefaults.setImpact(32.0))
			//.onRegister(movementBehaviour(new DrillMovementBehaviour()))
			.item()
			//.tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<AndesiteBitBlock> ANDESITE_DRILL_BIT = REGISTRATE
			.block("andesite_drill_bit", AndesiteBitBlock::new)
			.item(BaseBitItem::new)
			.properties(p -> p.durability(AndesiteBitBlockEntity.DURABILITY))
			.build()
			.loot(BaseBitBlock::createDrillBitDrop)
			.transform(BlockStressDefaults.setImpact(4.0))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.register();

	public static final BlockEntry<BrassBitBlock> BRASS_DRILL_BIT = REGISTRATE
			.block("brass_drill_bit", BrassBitBlock::new)
			.item(BaseBitItem::new)
			.properties(p -> p.durability(BrassBitBlockEntity.DURABILITY))
			.build()
			.loot(BaseBitBlock::createDrillBitDrop)
			.transform(BlockStressDefaults.setImpact(4.0))
			.properties(BlockBehaviour.Properties::noOcclusion)
			.register();

	public static final BlockEntry<CollectionBlock> DRILL_COLLECTION = REGISTRATE
			.block("drill_collection", CollectionBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(BlockBehaviour.Properties::noOcclusion)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.blockstate(BlockStateGen.axisBlockProvider(true))
			.transform(BlockStressDefaults.setImpact(2.0))
			.transform(axeOrPickaxe())
			.simpleItem()
			.register();

	public static final BlockEntry<OverclockBlock> DRILL_OVERCLOCK = REGISTRATE
			.block("drill_overclock", OverclockBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(BlockBehaviour.Properties::noOcclusion)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.blockstate(BlockStateGen.axisBlockProvider(true))
			.transform(BlockStressDefaults.setImpact(12.0))
			.transform(axeOrPickaxe())
			.simpleItem()
			.register();

	// gets all static fields to load
	public static void register() {}
}
