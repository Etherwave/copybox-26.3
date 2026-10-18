package com.amazingcopybox.client;


import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;

// 放在 client 源集
public class CopyBoxBlockEntityRenderState extends BlockEntityRenderState {
    private final ItemStackRenderState itemRenderState = new ItemStackRenderState();
    public ItemStackRenderState getItemRenderState() { return this.itemRenderState; }
}