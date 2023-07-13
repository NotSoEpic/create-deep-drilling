package com.dindcrzy.deepdrilling;

import com.jozufozu.flywheel.core.PartialModel;

public class DDPartialModels {
	public static final PartialModel
			BORE_DRILL_SHAFT = block("bore_drill/shaft"),
		MODULE_SHAFT = block("bore_drill/shaft"),
		DRILL_BIT_ANDESITE = block("drill_bit/andesite"),
		DRILL_BIT_BRASS = block("drill_bit/brass");

	private static PartialModel block(String path) {
		return new PartialModel(DeepDrilling.id("block/" + path));
	}

	public static void register() {}
}
