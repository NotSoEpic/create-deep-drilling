package com.dindcrzy.deepdrilling.coreloot;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.function.Consumer;

public class DDCoreLoot {
	public static final LootContextParam<ResourceLocation> DRILL_TYPE_CTX = create("deepdrilling:drill_type");
	public static final LootContextParam<Float> DRILL_DAMAGE_CTX = create("deepdrilling:drill_damage");
	private static <T>LootContextParam<T> create(String id) {
		return new LootContextParam<>(new ResourceLocation(id));
	}

	public static final LootContextParamSet CORE = register("deepdrilling:core", builder ->
			builder.required(DRILL_TYPE_CTX)
					.required(DRILL_DAMAGE_CTX));

	private static LootContextParamSet register(String registryName, Consumer<LootContextParamSet.Builder> builderConsumer) {
		LootContextParamSet.Builder builder = new LootContextParamSet.Builder();
		builderConsumer.accept(builder);
		return builder.build();
	}

	public static final LootItemConditionType DRILL_TYPE_CND = register("deepdrilling:drill_type", IsDrillLootCondition.LOOT_CONDITION_TYPE);

	private static LootItemConditionType register(String registryName, LootItemConditionType conditionType) {
		return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(registryName), conditionType);
	}

	public static void init() {
		// gets static members to load properly
	}
}
