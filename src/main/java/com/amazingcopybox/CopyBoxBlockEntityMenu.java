package com.amazingcopybox;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CopyBoxBlockEntityMenu extends AbstractContainerMenu {
    private static final int SOURCE_SLOTS = 1;
    private static final int TARGET_ROWS = 3;
    private static final int TARGET_COLUMNS = 5;
    private static final int TARGET_SLOTS = TARGET_ROWS * TARGET_COLUMNS; // 15
    private static final int SLOTS_COUNT = SOURCE_SLOTS + TARGET_SLOTS;    // 16

    private static final int CONTAINER_INDEX_START = 0;
    private static final int CONTAINER_INDEX_END = SLOTS_COUNT;
    private static final int INVENTORY_INDEX_START = CONTAINER_INDEX_END;
    private static final int INVENTORY_INDEX_END = INVENTORY_INDEX_START + Inventory.INVENTORY_SIZE;

    // 源格子位置（左边单独一个）
    private static final int SOURCE_SLOT_X = 23;
    private static final int SOURCE_SLOT_Y = 36;

    // 目标格子区域起点（右边 3x5）
    private static final int TARGET_START_X = 80;
    private static final int TARGET_START_Y = 18;

    private static final int INVENTORY_START_X = 8;
    private static final int INVENTORY_START_Y = 84;

    private final Container container;

    // Client-side constructor
    public CopyBoxBlockEntityMenu(final int containerId, final Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(SLOTS_COUNT));
    }

    // Server-side constructor
    public CopyBoxBlockEntityMenu(final int containerId, final Inventory inventory, final Container container) {
        super(CopyBoxMenuTypes.COPYBOX_BLOCK_ENTITY_MENU, containerId);
        checkContainerSize(container, SLOTS_COUNT);
        this.container = container;

        // Some containers do custom logic when opened by a player.
        container.startOpen(inventory.player);

        // Add the slots for our container
        this.addContainerSlots();

        // Add the player inventory slots.
        this.addStandardInventorySlots(inventory, INVENTORY_START_X, INVENTORY_START_Y);
    }
    private void addContainerSlots() {
        // 槽位 0：源格子，单独放在左边
        this.addSlot(new Slot(this.container, 0, SOURCE_SLOT_X, SOURCE_SLOT_Y));

        // 槽位 1~15：目标格子，3 行 5 列
        for (int row = 0; row < TARGET_ROWS; row++) {
            for (int col = 0; col < TARGET_COLUMNS; col++) {
                int index = SOURCE_SLOTS + row * TARGET_COLUMNS + col; // 1 + ...
                int x = TARGET_START_X + col * SLOT_SIZE;
                int y = TARGET_START_Y + row * SLOT_SIZE;
                this.addSlot(new Slot(this.container, index, x, y));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack clicked = stack.copy();

        if (slotIndex < SLOTS_COUNT) {
            // If the clicked slot is in the container, try moving the item to the player inventory.
            // When moving into the player's inventory, we iterate over slots in a reversed order; starting from the last hotbar slot to the first inventory slot.
            if (!this.moveItemStackTo(stack, INVENTORY_INDEX_START, INVENTORY_INDEX_END, /* backwards: */ true)) {
                return ItemStack.EMPTY;
            }
        } else {
            // Else, move the item from the player inventory to the container.
            if (!this.moveItemStackTo(stack, CONTAINER_INDEX_START, CONTAINER_INDEX_END, /* backwards: */ false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return clicked;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}