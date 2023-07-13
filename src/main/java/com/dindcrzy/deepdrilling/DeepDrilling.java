package com.dindcrzy.deepdrilling;

import com.dindcrzy.deepdrilling.blocks.DDBlockEntities;
import com.dindcrzy.deepdrilling.blocks.DDBlocks;
import com.dindcrzy.deepdrilling.coreloot.DDCoreLoot;
import com.simibubi.create.Create;

import com.simibubi.create.foundation.data.CreateRegistrate;

import com.simibubi.create.foundation.item.ItemDescription;

import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;

import com.simibubi.create.foundation.item.TooltipModifier;

import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeepDrilling implements ModInitializer {
	public static final String ID = "deepdrilling";
	public static final String NAME = "Deep Drilling";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID);

	static {
		REGISTRATE.setTooltipModifierFactory(
			item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
				.andThen(TooltipModifier.mapNull(KineticStats.create(item))));
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Create addon mod [{}] is loading alongside Create [{}]!", NAME, Create.VERSION);
		LOGGER.info(EnvExecutor.unsafeRunForDist(
				() -> () -> "{} is accessing Porting Lib from the client!",
				() -> () -> "{} is accessing Porting Lib from the server!"
		), NAME);

		DDCoreLoot.init();

		DDBlocks.register();
		DDBlockEntities.register();

		// FabricBlockLootTableProvider.add(DDBlocks.ANDESITE_DRILL_BIT.get(), );

		REGISTRATE.register();

	}

	public static ResourceLocation id(String path) {
		return new ResourceLocation(ID, path);
	}
}
