//package com.amazingcopybox;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockBehaviour;
//import net.minecraft.registry.Registry;
//import net.minecraft.registry.Registries;
//import net.minecraft.registry.entry.RegistryEntry;
//import net.minecraft.util.Identifier;
//
//public class ModBlocks {
//    // 注册一个名为 "copy_chest" 的方块
//    public static final Block COPY_CHEST = register(
//            "copy_chest",
//            Block::new,
//            BlockBehaviour.Properties.of().strength(2.5f).requiresCorrectToolForDrops()
//    );
//
//    private static Block register(String name, java.util.function.Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
//        Identifier id = Identifier.of("copybox", name); // 把 "copybox" 换成你的模组ID
//        Block block = factory.apply(settings.registryKey(net.minecraft.registry.RegistryKey.of(Registries.BLOCK.getKey(), id)));
//
//        // 同时注册对应的方块物品，让它能拿在手里
//        Registry.register(Registries.BLOCK, id, block);
//        Registry.register(Registries.ITEM, id, new net.minecraft.item.BlockItem(block, new net.minecraft.item.Item.Settings().registryKey(net.minecraft.registry.RegistryKey.of(Registries.ITEM.getKey(), id))));
//
//        return block;
//    }
//
//    // 这个空方法用来触发类的静态加载
//    public static void initialize() {}
//}