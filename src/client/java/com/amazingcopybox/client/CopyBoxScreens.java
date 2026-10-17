package com.amazingcopybox.client;

import com.amazingcopybox.CopyBoxBlockEntityMenu;
import com.amazingcopybox.CopyBoxMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class CopyBoxScreens implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(CopyBoxMenuTypes.COPYBOX_BLOCK_ENTITY_MENU, CopyBoxBlockEntityMenuScreen::new);
    }
}
