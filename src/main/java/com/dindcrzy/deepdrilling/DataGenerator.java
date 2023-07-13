package com.dindcrzy.deepdrilling;

import com.dindcrzy.deepdrilling.blocks.DDBlocks;

import com.dindcrzy.deepdrilling.blocks.kinetics.drillbit.BaseBitBlock;

import com.simibubi.create.AllItems;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;

import com.dindcrzy.deepdrilling.blocks.DDBlockTags;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class DataGenerator implements DataGeneratorEntrypoint {
	public static class BlockTagGen extends FabricTagProvider.BlockTagProvider {
		public BlockTagGen(FabricDataGenerator dataGenerator) {
			super(dataGenerator);
		}

		// https://fabricmc.net/wiki/tutorial:tags
		@Override
		protected void generateTags() {
			getOrCreateTagBuilder(DDBlockTags.DRILL_THROUGH)
					.forceAddTag(BlockTags.BASE_STONE_OVERWORLD) // um excuse me what the actual fuck how is a vanilla tag not present
					.forceAddTag(BlockTags.BASE_STONE_NETHER)
					.addOptionalTag(new ResourceLocation("c:ores"))
					.add(Blocks.BEDROCK)
					.add(DDBlocks.ORE_NODE.get());

			getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
					.add(DDBlocks.BORE_DRILL.get())
					.add(DDBlocks.ANDESITE_DRILL_BIT.get())
					.add(DDBlocks.DRILL_COLLECTION.get())
					.add(DDBlocks.DRILL_OVERCLOCK.get());

			getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
					.add(DDBlocks.BORE_DRILL.get())
					.add(DDBlocks.ANDESITE_DRILL_BIT.get())
					.add(DDBlocks.DRILL_COLLECTION.get())
					.add(DDBlocks.DRILL_OVERCLOCK.get());
		}
	}

	public static class ModelGenerator extends FabricModelProvider {
		public ModelGenerator(FabricDataGenerator dataGenerator) {
			super(dataGenerator);
		}

		@Override
		public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
			blockStateModelGenerator.createTrivialCube(DDBlocks.ORE_NODE.get());
			blockStateModelGenerator.createAxisAlignedPillarBlockCustomModel(DDBlocks.DRILL_COLLECTION.get(),
					DeepDrilling.id("block/modules/collector"));
			blockStateModelGenerator.createAxisAlignedPillarBlockCustomModel(DDBlocks.DRILL_OVERCLOCK.get(),
					DeepDrilling.id("block/modules/overclock"));
		}

		@Override
		public void generateItemModels(ItemModelGenerators itemModelGenerator) {

		}
	}

	public static class TranslationGenerator extends FabricLanguageProvider {

		protected TranslationGenerator(FabricDataGenerator dataGenerator) {
			super(dataGenerator);
		}

		@Override
		public void generateTranslations(TranslationBuilder translationBuilder) {
			translationBuilder.add(DDBlocks.BORE_DRILL.get(), "Bore Drill");
			translationBuilder.add(DDBlocks.ANDESITE_DRILL_BIT.get(), "Andesite Drill Bit");
			translationBuilder.add(DDBlocks.BRASS_DRILL_BIT.get(), "Brass Drill Bit");
			translationBuilder.add(DDBlocks.ORE_NODE.get(), "Test Core");
			translationBuilder.add(DDBlocks.DRILL_COLLECTION.get(), "Drill Collector");
			translationBuilder.add(DDBlocks.DRILL_OVERCLOCK.get(), "Drill Efficiency");
		}
	}

	public static class LootTableGenerator extends FabricBlockLootTableProvider {

		protected LootTableGenerator(FabricDataGenerator dataGenerator) {
			super(dataGenerator);
		}

		@Override
		protected void generateBlockLootTables() {
			dropSelf(DDBlocks.BORE_DRILL.get());
			BaseBitBlock.createDrillBitDrop(this, DDBlocks.ANDESITE_DRILL_BIT.get());
			dropSelf(DDBlocks.DRILL_COLLECTION.get());
			dropSelf(DDBlocks.DRILL_OVERCLOCK.get());
		}
	}

	public static class OreNodeLootGenerator extends SimpleFabricLootTableProvider {
		private final Map<ResourceLocation, LootTable.Builder> entries = new HashMap<>();
		private static final String ANDESITE_BIT = "deepdrilling:andesite_drill_bit";
		private static final String BRASS_BIT = "deepdrilling:brass_drill_bit";
		public OreNodeLootGenerator(FabricDataGenerator dataGenerator) {
			super(dataGenerator, LootContextParamSet.builder().build());
		}

		protected void generateOreNodeLootTables() {
			ResourceLocation SOFT_EARTH = add(DeepDrilling.id("soft_eath"), LootTable.lootTable().withPool(
					LootPool.lootPool()
							.add(LootItem.lootTableItem(Items.DIRT).setWeight(20))
							.add(LootItem.lootTableItem(Items.GRAVEL).setWeight(10))
							.add(LootItem.lootTableItem(Items.SAND).setWeight(15))
							.add(LootItem.lootTableItem(Items.CLAY_BALL).setWeight(10))
							.setRolls(UniformGenerator.between(3, 5))
			));
			ResourceLocation HARD_EARTH = add(DeepDrilling.id("hard_earth"), LootTable.lootTable().withPool(
					LootPool.lootPool()
							.add(LootItem.lootTableItem(Items.COBBLESTONE).setWeight(20))
							.add(LootItem.lootTableItem(Items.COBBLED_DEEPSLATE).setWeight(15))
							.add(LootItem.lootTableItem(Items.DIORITE).setWeight(10))
							.add(LootItem.lootTableItem(Items.GRANITE).setWeight(10))
							.add(LootItem.lootTableItem(Items.ANDESITE).setWeight(10))
							.setRolls(UniformGenerator.between(3, 5))
			));

			ResourceLocation COAL_ORE = add(DeepDrilling.id("coal_ore"), LootTable.lootTable().withPool(
					LootPool.lootPool().add(LootItem.lootTableItem(Items.COAL)).setRolls(UniformGenerator.between(3, 5))
			));
			ResourceLocation IRON_ORE = add(DeepDrilling.id("iron_ore"), LootTable.lootTable().withPool(
					LootPool.lootPool().add(LootItem.lootTableItem(Items.RAW_IRON)).setRolls(UniformGenerator.between(3, 5))
			));
			ResourceLocation GOLD_ORE = add(DeepDrilling.id("gold_ore"), LootTable.lootTable().withPool(
					LootPool.lootPool().add(LootItem.lootTableItem(Items.RAW_GOLD)).setRolls(UniformGenerator.between(3, 5))
			));
			ResourceLocation COPPER_ORE = add(DeepDrilling.id("copper_ore"), LootTable.lootTable().withPool(
					LootPool.lootPool().add(LootItem.lootTableItem(Items.RAW_COPPER)).setRolls(UniformGenerator.between(8, 12))
			));
			ResourceLocation ZINC_ORE = add(DeepDrilling.id("zinc_ore"), LootTable.lootTable().withPool(
					LootPool.lootPool().add(LootItem.lootTableItem(AllItems.RAW_ZINC.get())).setRolls(UniformGenerator.between(3, 5))
			));
			ResourceLocation LAPIS_ORE = add(DeepDrilling.id("lapis_ore"), LootTable.lootTable().withPool(
					LootPool.lootPool().add(LootItem.lootTableItem(Items.LAPIS_LAZULI)).setRolls(UniformGenerator.between(10, 15))
			));
			ResourceLocation REDSTONE_ORE = add(DeepDrilling.id("redstone_ore"), LootTable.lootTable().withPool(
					LootPool.lootPool().add(LootItem.lootTableItem(Items.REDSTONE)).setRolls(UniformGenerator.between(3, 5))
			));
			ResourceLocation DIAMOND_ORE = add(DeepDrilling.id("diamond_ore"), LootTable.lootTable().withPool(
					LootPool.lootPool().add(LootItem.lootTableItem(Items.DIAMOND)).setRolls(UniformGenerator.between(3, 5))
			));
			ResourceLocation EMERALD_ORE = add(DeepDrilling.id("emerald_ore"), LootTable.lootTable().withPool(
					LootPool.lootPool().add(LootItem.lootTableItem(Items.EMERALD)).setRolls(UniformGenerator.between(3, 5))
			));

			add(DeepDrilling.id("coal_node"), LootTable.lootTable().withPool(
					LootPool.lootPool()
							.add(LootTableReference.lootTableReference(HARD_EARTH).setWeight(30))
							.add(LootTableReference.lootTableReference(SOFT_EARTH).setWeight(20))
							.add(LootTableReference.lootTableReference(COAL_ORE).setWeight(47))
							.add(LootTableReference.lootTableReference(DIAMOND_ORE).setWeight(3))
							.setRolls(UniformGenerator.between(1, 2))
			));
			add(DeepDrilling.id("iron_node"), LootTable.lootTable().withPool(
					LootPool.lootPool()
							.add(LootTableReference.lootTableReference(HARD_EARTH).setWeight(60))
							.add(LootTableReference.lootTableReference(IRON_ORE).setWeight(30))
							.add(LootTableReference.lootTableReference(REDSTONE_ORE).setWeight(10))
							.setRolls(UniformGenerator.between(1, 2))
			));
			add(DeepDrilling.id("copper_node"), LootTable.lootTable().withPool(
					LootPool.lootPool()
							.add(LootTableReference.lootTableReference(SOFT_EARTH).setWeight(60))
							.add(LootTableReference.lootTableReference(COPPER_ORE).setWeight(40))
							.setRolls(UniformGenerator.between(1, 2))
			));
			add(DeepDrilling.id("gold_node"), LootTable.lootTable().withPool(
					LootPool.lootPool()
							.add(LootTableReference.lootTableReference(HARD_EARTH).setWeight(60))
							.add(LootTableReference.lootTableReference(GOLD_ORE).setWeight(40))
							.setRolls(UniformGenerator.between(1, 2))
			));
			add(DeepDrilling.id("zinc_node"), LootTable.lootTable().withPool(
					LootPool.lootPool()
							.add(LootTableReference.lootTableReference(SOFT_EARTH).setWeight(70))
							.add(LootTableReference.lootTableReference(ZINC_ORE).setWeight(30))
							.setRolls(UniformGenerator.between(1, 2))
			));
			add(DeepDrilling.id("rare_earths_node"), LootTable.lootTable().withPool(
					LootPool.lootPool()
							.add(LootTableReference.lootTableReference(HARD_EARTH).setWeight(60))
							.add(LootTableReference.lootTableReference(LAPIS_ORE).setWeight(30))
							.add(LootTableReference.lootTableReference(EMERALD_ORE).setWeight(5))
							.add(LootTableReference.lootTableReference(DIAMOND_ORE).setWeight(5))
							.setRolls(UniformGenerator.between(1, 2))
			));
		}

		protected ResourceLocation add(ResourceLocation location, LootTable.Builder table) {
			location = new ResourceLocation(location.getNamespace(), "drilling_nodes/" + location.getPath());
			if (entries.containsKey(location)) {
				DeepDrilling.LOGGER.warn("Duplicate core loot table being registered: " + location);
			}
			entries.put(location, table);
			return location;
		}

		@Override
		public void accept(BiConsumer<ResourceLocation, LootTable.Builder> resourceLocationBuilderBiConsumer) {
			generateOreNodeLootTables();
			entries.forEach(resourceLocationBuilderBiConsumer);
		}

		@Override
		public String getName() {
			return "Ore Node Loot Tables";
		}
	}

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		DeepDrilling.LOGGER.info("Running DataGen");
		fabricDataGenerator.addProvider(BlockTagGen::new);
		fabricDataGenerator.addProvider(ModelGenerator::new);
		fabricDataGenerator.addProvider(TranslationGenerator::new);
		fabricDataGenerator.addProvider(LootTableGenerator::new);
		fabricDataGenerator.addProvider(OreNodeLootGenerator::new);
	}
}
