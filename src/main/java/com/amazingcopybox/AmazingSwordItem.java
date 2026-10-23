package com.amazingcopybox;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Function;

public class AmazingSwordItem extends Item implements MixinItemExtention {

    public AmazingSwordItem(Properties settings) {
        super(settings);
    }

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
    public static final double attackRange = 6.0D; // 和当前攻击范围一致

    public static final ResourceKey<Item> AMAZING_SWORD_RK = create("amazing_sword");
    public static final Item AMAZING_SWORD_ITEM = register(
            AMAZING_SWORD_RK,
            AmazingSwordItem::new,
            new Item.Properties()
                    .attributes(ItemAttributeModifiers.builder()
                            // 攻击伤害：基础 1 + 999 = 1000
                            .add(Attributes.ATTACK_DAMAGE,
                                    new AttributeModifier(
                                            Item.BASE_ATTACK_DAMAGE_ID,
                                            999.0D,
                                            AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.MAINHAND)
                            // 攻击范围：3 + 7 = 10
                            .add(Attributes.ENTITY_INTERACTION_RANGE,
                                    new AttributeModifier(
                                            CopyBox.id("amazing_sword_reach"),
                                            attackRange,
                                            AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.MAINHAND)
                            // 攻击速度：保持正常剑的速度
                            .add(Attributes.ATTACK_SPEED,
                                    new AttributeModifier(
                                            Item.BASE_ATTACK_SPEED_ID,
                                            -1D,
                                            AttributeModifier.Operation.ADD_VALUE),
                                    EquipmentSlotGroup.MAINHAND)
                            .build())
    );

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level level = player.level();
        Vec3 eyePos = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        double distance = player.distanceTo(entity); // 和当前攻击范围一致

        // 粒子：只在客户端生成，立刻可见
        if (level.isClientSide()) {
            double baseAngle = Math.toDegrees(Math.atan2(-look.x, look.z));
            int particleCount = 6;

            for (int i = 0; i < particleCount; i++) {
                double angleDeg = -30 + 60.0 * i / (particleCount - 1); // -30 到 +30 度
                double angleRad = Math.toRadians(baseAngle + angleDeg);
                double x = eyePos.x - Math.sin(angleRad) * distance;
                double z = eyePos.z + Math.cos(angleRad) * distance;
                double y = player.getY() + 1.0D;

                // 用 DUST 粒子（彩色、显眼），颜色用红色
                level.addParticle(
                        new DustParticleOptions(0xFF0000, 1.0F), // 红色
                        x, y, z,
                        0, 0, 0
                );
            }
//            double baseAngle = Math.toDegrees(Math.atan2(-look.x, look.z));
            double angleRad = Math.toRadians(baseAngle); // 正前方
            double x = eyePos.x - Math.sin(angleRad) * distance;
            double z = eyePos.z + Math.cos(angleRad) * distance;
            double y = player.getY() + 1.0D;

            level.addParticle(ParticleTypes.SWEEP_ATTACK, x, y, z, 0, 0, 0);
        }
        // 只在服务端执行，避免客户端重复计算
        if (player.level().isClientSide()) {
            return false; // 客户端返回 false，让原版逻辑继续（不影响服务端判定）
        }

        float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        entity.hurt(level.damageSources().playerAttack(player), damage);
        // 吸血：回复 1 点生命（半颗心）
        player.heal(1.0F);

        // 播放横扫音效
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F);

        // 返回 true，让 Mixin 取消原版攻击（避免对被点击的目标造成两次伤害）
        return true;
    }

    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        // 只在服务端执行，避免客户端和服务端各冲一次导致距离翻倍或不同步
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS; // 客户端返回 SUCCESS 让手臂动画正常播放
        }

        // 获取玩家视线方向（水平方向，忽略俯仰，避免往地里或天上冲）
        Vec3 look = user.getLookAngle();
        double dashPower = 5D; // 突进力度，可以调，但别太大

        if (user instanceof ServerPlayer serverPlayer) {
            Vec3 motion = new Vec3(look.x * dashPower, user.getDeltaMovement().y, look.z * dashPower);
            user.setDeltaMovement(motion);
            // 直接发速度包，让客户端同步
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(user.getId(), motion));
            level.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENDER_DRAGON_FLAP, SoundSource.PLAYERS, 1.0F, 1.5F);
        }
        // 可选：加个冷却，防止无限右键飞行
        user.getCooldowns().addCooldown(stack, 1); // 1 tick 冷却，10ms

        return InteractionResult.SUCCESS;
    }

    public static void initialize() {}
}
