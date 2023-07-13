package com.dindcrzy.deepdrilling.blocks;

import com.dindcrzy.deepdrilling.blocks.kinetics.boredrill.BoreDrillBlockEntity;
import com.dindcrzy.deepdrilling.blocks.kinetics.boredrill.BoreDrillInstance;
import com.dindcrzy.deepdrilling.blocks.kinetics.boredrill.BoreDrillRenderer;
import com.dindcrzy.deepdrilling.blocks.kinetics.drillbit.AndesiteBitBlockEntity;
import com.dindcrzy.deepdrilling.blocks.kinetics.drillbit.BaseBitInstance;
import com.dindcrzy.deepdrilling.blocks.kinetics.drillbit.BaseBitRenderer;
import com.dindcrzy.deepdrilling.blocks.kinetics.drillbit.BrassBitBlockEntity;
import com.dindcrzy.deepdrilling.blocks.kinetics.modules.ModuleInstance;
import com.dindcrzy.deepdrilling.blocks.kinetics.modules.ModuleRenderer;
import com.dindcrzy.deepdrilling.blocks.kinetics.modules.collection.CollectionBlockEntity;
import com.dindcrzy.deepdrilling.blocks.kinetics.modules.efficiency.OverclockBlockEntity;
import com.dindcrzy.deepdrilling.blocks.ore_node.OreNodeBlockEntity;
import com.dindcrzy.deepdrilling.blocks.ore_node.OreNodeRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.dindcrzy.deepdrilling.DeepDrilling.REGISTRATE;


public class DDBlockEntities {
	public static final BlockEntityEntry<OreNodeBlockEntity> ORE_NODE = REGISTRATE
			.blockEntity("ore_node", OreNodeBlockEntity::new)
			.validBlocks(DDBlocks.ORE_NODE)
			.renderer(() -> OreNodeRenderer::new)
			.register();
	public static final BlockEntityEntry<BoreDrillBlockEntity> BORE_DRILL = REGISTRATE
			.blockEntity("bore_drill", BoreDrillBlockEntity::new)
			.instance(() -> BoreDrillInstance::new, false)
			.validBlocks(DDBlocks.BORE_DRILL)
			.renderer(() -> BoreDrillRenderer::new)
			.register();

	public static final BlockEntityEntry<AndesiteBitBlockEntity> ANDESITE_DRILL_BIT = REGISTRATE
			.blockEntity("andesite_drill_bit", AndesiteBitBlockEntity::new)
			.instance(() -> BaseBitInstance::new, false)
			.validBlocks(DDBlocks.ANDESITE_DRILL_BIT)
			.renderer(() -> BaseBitRenderer::new)
			.register();

	public static final BlockEntityEntry<BrassBitBlockEntity> BRASS_DRILL_BIT = REGISTRATE
			.blockEntity("brass_drill_bit", BrassBitBlockEntity::new)
			.instance(() -> BaseBitInstance::new, false)
			.validBlocks(DDBlocks.BRASS_DRILL_BIT)
			.renderer(() -> BaseBitRenderer::new)
			.register();

	public static final BlockEntityEntry<CollectionBlockEntity> DRILL_COLLECTION = REGISTRATE
			.blockEntity("drill_collection", CollectionBlockEntity::new)
			.instance(() -> ModuleInstance::new)
			.validBlocks(DDBlocks.DRILL_COLLECTION)
			.renderer(() -> ModuleRenderer::new)
			.register();

	public static final BlockEntityEntry<OverclockBlockEntity> DRILL_OVERCLOCK = REGISTRATE
			.blockEntity("drill_efficiency", OverclockBlockEntity::new)
			.instance(() -> ModuleInstance::new)
			.validBlocks(DDBlocks.DRILL_OVERCLOCK)
			.renderer(() -> ModuleRenderer::new)
			.register();

	// gets all static fields to load
	public static void register() {}
}
