package com.amazingcopybox.client;

import com.amazingcopybox.CopyBoxBlockEntity;
import com.amazingcopybox.CopyBoxMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class CopyBoxClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		MenuScreens.register(CopyBoxMenuTypes.COPYBOX_BLOCK_ENTITY_MENU, CopyBoxBlockEntityMenuScreen::new);
		BlockEntityRenderers.register(
				CopyBoxBlockEntity.COPYBOX_BLOCK_ENTITY_TYPE,  // 你的 BlockEntityType
				CopyBoxBlockEntityRenderer::new
		);
	}
}