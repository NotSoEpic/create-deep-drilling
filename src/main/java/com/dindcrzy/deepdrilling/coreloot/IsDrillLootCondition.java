package com.dindcrzy.deepdrilling.coreloot;

import com.dindcrzy.deepdrilling.DeepDrilling;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class IsDrillLootCondition implements LootItemCondition {
	public static final LootItemConditionType LOOT_CONDITION_TYPE = new LootItemConditionType(new IsDrillLootCondition.Serializer());
	final Set<ResourceLocation> drills;
	public IsDrillLootCondition(Set<ResourceLocation> drills) {
		this.drills = drills;
	}
	public IsDrillLootCondition(String... drills) {
		this.drills = new HashSet<>();
		Arrays.stream(drills).forEach(s -> this.drills.add(new ResourceLocation(s)));
	}
	@Override
	public LootItemConditionType getType() {
		return LOOT_CONDITION_TYPE;
	}

	@Override
	public boolean test(LootContext lootContext) {
		ResourceLocation drill = lootContext.getParam(DDCoreLoot.DRILL_TYPE_CTX);
		return drills.contains(drill);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IsDrillLootCondition drillLootCondition) {
			return drills.equals(drillLootCondition.drills);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return Objects.hash(drills);
	}

	public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<IsDrillLootCondition> {
		@Override
		public void serialize(JsonObject json, IsDrillLootCondition value, JsonSerializationContext serializationContext) {
			JsonArray drills = new JsonArray();
			for (ResourceLocation drill : value.drills) {
				drills.add(drill.toString());
			}
			json.add("drills", drills);
		}

		@Override
		public IsDrillLootCondition deserialize(JsonObject json, JsonDeserializationContext serializationContext) {
			Set<ResourceLocation> drills = new HashSet<>();
			if (json.get("drills").isJsonArray()) {
				json.getAsJsonArray("drills").forEach(
						jsonElement -> drills.add(new ResourceLocation(jsonElement.getAsString()))
				);
			}
			return new IsDrillLootCondition(drills);
		}
	}
}
