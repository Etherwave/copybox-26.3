package com.amazingcopybox.mixin;

import com.amazingcopybox.MixinItemExtention;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyReturnValue(method = "createAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder copybox$modifyReach(AttributeSupplier.Builder original) {
        // 把实体交互距离的基值从 3 改成 10
        // 注意：这里改的是“基值”，所有玩家都会受影响
        return original.add(Attributes.ENTITY_INTERACTION_RANGE, 10.0D);
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void sb$itemAttack(Entity targetEntity, CallbackInfo ci) {
        Item item = getMainHandItem().getItem();
        if ((item instanceof MixinItemExtention mixinItemExtentionItem)) {
            if (mixinItemExtentionItem.onLeftClickEntity(getMainHandItem(), (Player) (Object) this, targetEntity))
                ci.cancel();
        }
    }
}
