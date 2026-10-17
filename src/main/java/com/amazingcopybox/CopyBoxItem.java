package com.amazingcopybox;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;

import java.util.function.Function;

public class CopyBoxItem implements ModInitializer {
    public static ResourceKey<Item> create(String name) {
        // Create the item key.
        return ResourceKey.create(Registries.ITEM, CopyBox.id(name));
    }

    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        // Create the item instance.
        Item item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static final ResourceKey<Item> COPYBOX = create("copybox");
    public static final Item COPYBOX_ITEM = register(CopyBoxItem.COPYBOX, Item::new, new Item.Properties());

    public static void initialize() {
        // Get the event for modifying entries in the ingredients group.
        // And register an event handler that adds our suspicious item to the ingredients group.
//        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
//                .register((creativeTab) -> creativeTab.accept(ModItems.COPYBOX_ITEM));
        // Register the group.
//    Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_CREATIVE_TAB_KEY, CUSTOM_CREATIVE_TAB);
//        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_CREATIVE_TAB_KEY, CUSTOM_CREATIVE_TAB);

        // Add the bone marrow to the composting registry with a 100% chance of increasing the composter's level.
//        CompostableRegistry.INSTANCE.add(ModItems.BONE_MARROW, 1.0f);

        // Remember, Minecraft deals with logical based-time using ticks.
        // 20 ticks = 1 second.
//        FuelValueEvents.BUILD.register((builder, context) -> {
//            builder.add(ModItems.QUARK_GLUON_PLASMA, 120 * 20);
//        });
    }
    @Override
    public void onInitialize() {
        CopyBoxItem.initialize();
    }
}


