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
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class CopyBoxBlockItem implements ModInitializer {
    private static BlockItemId create(String name) {
        Identifier id = CopyBox.id(name);
        return BlockItemId.create(id, id);
    }

//    private static Block registerBlock(ResourceKey<Block> blockKey, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties props) {
//        Block block = factory.apply(props.setId(blockKey));
//        Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
//        return block;
//    }
//    private static Block register(BlockItemId id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
//        // Create the block instance
//        Block block = register(id.block(), blockFactory, properties);
//
//        // Create the block item instance
//        BlockItem blockItem = new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(id.item()));
//        Registry.register(BuiltInRegistries.ITEM, id.item(), blockItem);
//
//        return block;
//    }

    public record BlockWithItem(Block block, BlockItem item) {}
    private static BlockWithItem register(BlockItemId id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
        // 1. 创建并注册方块
        Block block = blockFactory.apply(properties.setId(id.block()));
        Registry.register(BuiltInRegistries.BLOCK, id.block(), block);

//        // 2. 创建并注册方块物品
        BlockItem blockItem = new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(id.item()));
        Registry.register(BuiltInRegistries.ITEM, id.item(), blockItem);
//        public static final Block COPYBOX_BLOCK_ITEM = register(
//                CopyBoxBlockItem.COPYBOX_BLOCK,
//                Block::new,
//                BlockBehaviour.Properties.of().sound(SoundType.GRASS));
//        return block;
        return new BlockWithItem(block, blockItem);
    }


    public static final BlockItemId COPYBOX_BLOCK_ID = create("copybox_block");
    // 2. 创建并注册方块物品
//    BlockItem blockItem = new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(id.item()));
//        Registry.register(BuiltInRegistries.ITEM, id.item(), blockItem);

//    public static final Block COPYBOX_BLOCK_ITEM = register(
//            COPYBOX_BLOCK,
//            Block::new,
//            BlockBehaviour.Properties.of().sound(SoundType.GRASS)
//    );
    public static final BlockWithItem COPYBOX = register(COPYBOX_BLOCK_ID,
            Block::new,
            BlockBehaviour.Properties.of().sound(SoundType.GRASS));
    public static final Block COPYBOX_BLOCK = COPYBOX.block;
    public static final BlockItem COPYBOX_BLOCK_ITEM = COPYBOX.item;


//    public static final ResourceKey<CreativeModeTab> COPYBOX_BLOCK_ITEM_CREATIVE_TAB_KEY = ResourceKey.create(
//            BuiltInRegistries.CREATIVE_MODE_TAB.key(), CopyBox.id("creative_tab")
//    );
//    public static final CreativeModeTab COPYBOX_BLOCK_ITEM_CREATIVE_TAB = FabricCreativeModeTab.builder()
//            .icon(() -> new ItemStack(CopyBoxBlockItem.COPYBOX_BLOCK_ITEM))
//            .title(Component.translatable("creativeTab.copybox_block"))
//            .displayItems((params, output) -> {
//                output.accept(CopyBoxBlockItem.COPYBOX_BLOCK_ITEM);
//            })
//            .build();

//    public static final Block CONDENSED_DIRT = register(
//            CopyBoxBlockItem.CONDENSED_DIRT,
//            Block::new,
//            BlockBehaviour.Properties.of().sound(SoundType.GRASS)
//    );

    public static void initialize() {
//        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register((creativeTab) -> {
//            creativeTab.accept(CopyBoxBlockItem.COPYBOX_BLOCK_ITEM.asItem());
//        });
//        Registry.register(
//                BuiltInRegistries.CREATIVE_MODE_TAB,
//                COPYBOX_BLOCK_ITEM_CREATIVE_TAB_KEY,
//                COPYBOX_BLOCK_ITEM_CREATIVE_TAB);
    }

    @Override
    public void onInitialize() {
        CopyBoxBlockItem.initialize();
    }
}
