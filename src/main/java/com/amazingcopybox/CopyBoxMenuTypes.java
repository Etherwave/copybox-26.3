package com.amazingcopybox;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class CopyBoxMenuTypes {
    public static final MenuType<CopyBoxBlockEntityMenu> COPYBOX_BLOCK_ENTITY_MENU = register("copybox_block_entity_menu", CopyBoxBlockEntityMenu::new);

    public static <T extends AbstractContainerMenu> MenuType<T> register(
            String name,
            MenuType.MenuSupplier<T> constructor
    ) {
        return Registry.register(BuiltInRegistries.MENU, CopyBox.id(name), new MenuType<>(constructor, FeatureFlagSet.of()));
    }

    public static void initialize() {}
}
