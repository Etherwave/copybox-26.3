package com.amazingcopybox.client;


import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;

// 放在 client 源集
public class CopyBoxBlockEntityRenderState extends BlockEntityRenderState {
    private ItemStack itemToRender = ItemStack.EMPTY;

    public ItemStack getItemToRender() { return this.itemToRender; }
    public void setItemToRender(ItemStack stack) { this.itemToRender = stack; }

    // CopyBoxBlockEntityRenderState 里加：
    private final ItemStackRenderState itemRenderState = new ItemStackRenderState();
    public ItemStackRenderState getItemRenderState() { return this.itemRenderState; }
}