package com.amazingcopybox;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;

public class NetherPortalBlockItem {

    // 为物品注册表创建一个 ResourceKey，命名空间用你的 mod id
    private static final ResourceKey<Item> NETHER_PORTAL_ITEM_KEY = ResourceKey.create(
            Registries.ITEM,
            CopyBox.id("nether_portal")
    );

    public static final Item NETHER_PORTAL_BLOCK_ITEM = Registry.register(
            BuiltInRegistries.ITEM,
            NETHER_PORTAL_ITEM_KEY,
            new BlockItem(
                    Blocks.NETHER_PORTAL, // 直接用原版方块
                    new Item.Properties()
                            .useBlockDescriptionPrefix()
                            .setId(NETHER_PORTAL_ITEM_KEY) // 用同一个 key 设置物品 ID
            )
    );

    public static void initialize() {
        // 空方法，用于触发静态初始化
    }
}