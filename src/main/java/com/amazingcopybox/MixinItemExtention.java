package com.amazingcopybox;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface MixinItemExtention {
    boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity);
}
