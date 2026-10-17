package com.amazingcopybox.client;

import com.amazingcopybox.CopyBoxMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class CopyBoxClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		MenuScreens.register(CopyBoxMenuTypes.COPYBOX_BLOCK_ENTITY_MENU, CopyBoxBlockEntityMenuScreen::new);
	}
}