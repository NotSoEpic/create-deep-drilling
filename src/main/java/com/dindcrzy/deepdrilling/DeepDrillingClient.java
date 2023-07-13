package com.dindcrzy.deepdrilling;

import net.fabricmc.api.ClientModInitializer;

public class DeepDrillingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		DDPartialModels.register();
	}
}
