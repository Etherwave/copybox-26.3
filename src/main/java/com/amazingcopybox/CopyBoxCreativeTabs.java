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
            .icon(() -> new ItemStack(CopyBoxItem.COPYBOX_ITEM))
            .title(Component.translatable("creativeTab.copybox"))
            .displayItems((params, output) -> {
                output.accept(CopyBoxItem.COPYBOX_ITEM);
                output.accept(CopyBoxBlockItem.COPYBOX_BLOCK_ITEM);
//                output.accept(ModItems.POISONOUS_APPLE);

                // The tab builder also accepts Blocks
//                output.accept(ModBlocks.CONDENSED_DIRT);
//                output.accept(ModBlocks.CONDENSED_OAK_LOG);

                // And custom ItemStacks
//                ItemStack stack = new ItemStack(Items.SEA_PICKLE);
//                stack.set(DataComponents.ITEM_NAME, Component.literal("Pickle Rick"));
//                stack.set(DataComponents.LORE, new ItemLore(List.of(Component.literal("I'm pickle riiick!!"))));
//                output.accept(stack);

//                ItemStack stack = new ItemStack(CopyBoxBlockItem.COPYBOX_BLOCK_ITEM);
////                stack.set(DataComponents.ITEM_NAME, Component.literal("Pickle Rick"));
//                stack.set(DataComponents.ITEM_NAME, Component.literal("copybox:copybox_block"));
////                stack.set(DataComponents.LORE, new ItemLore(List.of(Component.literal("I'm pickle riiick!!"))));
//                output.accept(stack);
            })
            .build();
    public static void register() {
        //        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_CREATIVE_TAB_KEY, CUSTOM_CREATIVE_TAB);
        Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                COPYBOX_CREATIVE_TAB_KEY,
                COPYBOX_CREATIVE_TAB);
    }

}
