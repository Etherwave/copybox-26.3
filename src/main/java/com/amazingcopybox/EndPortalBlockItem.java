package com.amazingcopybox;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;

public class EndPortalBlockItem {

    private static final ResourceKey<Item> END_PORTAL_ITEM_KEY = ResourceKey.create(
            Registries.ITEM,
            CopyBox.id("end_portal")
    );

    public static final Item END_PORTAL_BLOCK_ITEM = Registry.register(
            BuiltInRegistries.ITEM,
            END_PORTAL_ITEM_KEY,
            new BlockItem(
                    Blocks.END_PORTAL, // 关键：用原版末地传送门方块
                    new Item.Properties()
                            .useBlockDescriptionPrefix()
                            .setId(END_PORTAL_ITEM_KEY)
            )
    );

    public static void initialize() {}
}