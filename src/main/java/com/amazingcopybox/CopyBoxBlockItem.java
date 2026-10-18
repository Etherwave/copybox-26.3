package com.amazingcopybox;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class CopyBoxBlockItem {

    private static BlockItem register(Block block, BlockItemId blockItemId) {
        // 创建并注册方块物品
        BlockItem blockItem = new BlockItem(
                block,
                new Item.Properties().useBlockDescriptionPrefix().setId(blockItemId.item()));
        Registry.register(BuiltInRegistries.ITEM, blockItemId.item(), blockItem);
        return blockItem;
    }

    public static final BlockItem COPYBOX_BLOCK_ITEM = register(CopyBoxBlock.COPYBOX_BLOCK, CopyBoxBlock.COPYBOX_BLOCK_ID);

    public static void initialize() {
    }
}
