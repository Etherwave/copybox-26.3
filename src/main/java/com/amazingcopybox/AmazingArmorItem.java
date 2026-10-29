package com.amazingcopybox;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;
import java.util.function.Function;

public class AmazingArmorItem extends Item {

    public AmazingArmorItem(Properties settings) {
        super(settings);
    }

    public static Item register(ResourceKey<Item> itemKey, Function<Properties, Item> itemFactory, Item.Properties settings) {
        // Create the item instance.
        Item item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static Item.Properties createArmorProperties(String name, ArmorType type) {
        // 1. 先拿到原版盔甲材质对应的属性修饰符（护甲值、韧性等）
        ItemAttributeModifiers armorAttrs = AMAZING_ARMOR_MATERIAL.createAttributes(type);

        // 2. 在它的基础上追加最大生命值
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        // 把 armorAttrs 里的条目全部复制进新 builder
        armorAttrs.modifiers().forEach(entry -> builder.add(entry.attribute(), entry.modifier(), entry.slot()));
        // 追加最大生命
        builder.add(Attributes.MAX_HEALTH,
                new AttributeModifier(
                        CopyBox.id(name + "_hp"),
                        25.0D,
                        AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.bySlot(type.getSlot()));
        return new Item.Properties()
                .humanoidArmor(AMAZING_ARMOR_MATERIAL, type)
                .durability(type.getDurability(BASE_DURABILITY))
                .attributes(builder.build());
    }

    public static final int BASE_DURABILITY = 150;
    public static final ResourceKey<EquipmentAsset> AMAZING_ARMOR_MATERIAL_RK = ResourceKey.create(EquipmentAssets.ROOT_ID, CopyBox.id("amazing_armor"));

    // 用泥土作为修复材料（合成材料）
    public static final TagKey<Item> REPAIRS_AMAZING_ARMOR = TagKey.create(
            BuiltInRegistries.ITEM.key(), CopyBox.id("repairs_amazing_armor")
    );

    // 材质：参考铁甲，但防御值拉满（原版上限30点，这里给足）
    public static final ArmorMaterial AMAZING_ARMOR_MATERIAL = new ArmorMaterial(
            33, // 基础耐久（钻石级别）
            Map.of(
                    ArmorType.HELMET, 20,
                    ArmorType.CHESTPLATE, 50,
                    ArmorType.LEGGINGS, 30,
                    ArmorType.BOOTS, 20
            ),
            50, // 附魔能力
            SoundEvents.ARMOR_EQUIP_IRON,
            2.0F, // 韧性
            1F, // 击退抗性
            REPAIRS_AMAZING_ARMOR,
            AMAZING_ARMOR_MATERIAL_RK // 纹理资产，暂时留空，游戏会警告但能运行
    );

    public static final ResourceKey<Item> AMAZING_HELMET_RK = ResourceKey.create(Registries.ITEM, CopyBox.id("amazing_helmet"));
    public static final ResourceKey<Item> AMAZING_CHESTPLATE_RK = ResourceKey.create(Registries.ITEM, CopyBox.id("amazing_chestplate"));
    public static final ResourceKey<Item> AMAZING_LEGGINGS_RK = ResourceKey.create(Registries.ITEM, CopyBox.id("amazing_leggings"));
    public static final ResourceKey<Item> AMAZING_BOOTS_RK = ResourceKey.create(Registries.ITEM, CopyBox.id("amazing_boots"));

    public static final Item AMAZING_HELMET_ITEM = register(
            AMAZING_HELMET_RK,
            Item::new,
            createArmorProperties("amazing_helmet", ArmorType.HELMET)
    );

    public static final Item AMAZING_CHESTPLATE_ITEM = register(
            AMAZING_CHESTPLATE_RK,
            Item::new,
            createArmorProperties("amazing_chestplate", ArmorType.CHESTPLATE)
    );
    public static final Item AMAZING_LEGGINGS_ITEM = register(
            AMAZING_LEGGINGS_RK,
            Item::new,
            createArmorProperties("amazing_leggings", ArmorType.LEGGINGS)
    );
    public static final Item AMAZING_BOOTS_ITEM = register(
            AMAZING_BOOTS_RK,
            Item::new,
            createArmorProperties("amazing_boots", ArmorType.BOOTS)
    );

    public static void addHp() {
        // 在 onInitialize 里：
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                int cnt = 0;
                if (player.getItemBySlot(EquipmentSlot.HEAD).is(AmazingArmorItem.AMAZING_HELMET_ITEM)) cnt++;
                if (player.getItemBySlot(EquipmentSlot.CHEST).is(AmazingArmorItem.AMAZING_CHESTPLATE_ITEM)) cnt++;
                if (player.getItemBySlot(EquipmentSlot.LEGS).is(AmazingArmorItem.AMAZING_LEGGINGS_ITEM)) cnt++;
                if (player.getItemBySlot(EquipmentSlot.FEET).is(AmazingArmorItem.AMAZING_BOOTS_ITEM)) cnt++;
                // 每 tick 回 10 点 = 每秒 200 点（20 tick）
                player.heal(cnt * 2.5F);
            }
        });
    }

    public static void initialize() {
        addHp();
    }
}