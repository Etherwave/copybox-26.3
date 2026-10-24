package com.amazingcopybox;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CopyBoxCreativeTabs {

    public static final ResourceKey<CreativeModeTab> COPYBOX_CREATIVE_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), CopyBox.id("copybox_creative_tab")
    );
    public static final CreativeModeTab COPYBOX_CREATIVE_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(CopyBoxBlockItem.COPYBOX_BLOCK_ITEM))
            .title(Component.translatable("creativeTab.copybox_block"))
            .displayItems((params, output) -> {
                output.accept(CopyBoxBlockItem.COPYBOX_BLOCK_ITEM);
                output.accept(AmazingSwordItem.AMAZING_SWORD_ITEM);
                output.accept(NetherPortalBlockItem.NETHER_PORTAL_BLOCK_ITEM);
                output.accept(EndPortalBlockItem.END_PORTAL_BLOCK_ITEM);
            })
            .build();
    public static void register() {
        Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                COPYBOX_CREATIVE_TAB_KEY,
                COPYBOX_CREATIVE_TAB);
    }

}
