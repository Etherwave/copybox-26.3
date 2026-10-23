package com.amazingcopybox;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CopyBoxBlockEntity extends BlockEntity implements Container, MenuProvider, WorldlyContainer {
    @Override
    public int getContainerSize() {
        return this.getItems().size();
    }
    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack stack = this.getItem(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    @Override
    public ItemStack getItem(int slot) {
        return this.getItems().get(slot);
    }
    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemStack result = ContainerHelper.removeItem(this.getItems(), slot, count);

        if (!result.isEmpty()) {
            this.setChanged();
        }

        return result;
    }
    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.getItems(), slot);
    }
    @Override
    public void setItem(int slot, ItemStack stack) {
        this.getItems().set(slot, stack);

        stack.limitSize(this.getMaxStackSize(stack));

        this.setChanged();
    }
    @Override
    public void clearContent() {
        this.getItems().clear();
        this.setChanged();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        int[] slots = new int[CONTAINER_SIZE - 1];
        for (int i = 1; i < CONTAINER_SIZE; i++) {
            slots[i - 1] = i;
        }
        return slots;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        // 槽位 0 是源格子，禁止漏斗抽取
        return slot != 0;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction direction) {
        // 只允许漏斗向源格子（槽位 0）放入物品
        return slot == 0;
    }

    public CopyBoxBlockEntity(BlockPos pos, BlockState state) {
        super(CopyBoxBlockEntity.COPYBOX_BLOCK_ENTITY_TYPE, pos, state);
    }

    public static final int CONTAINER_ROWS = 3;
    public static final int CONTAINER_COLUMNS = 5;
    public static final int CONTAINER_SIZE = 1 + CONTAINER_ROWS * CONTAINER_COLUMNS;

    private final NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = CopyBox.id(name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
    public static final BlockEntityType<CopyBoxBlockEntity> COPYBOX_BLOCK_ENTITY_TYPE =
            register("copybox_block_entity_type", CopyBoxBlockEntity::new, CopyBoxBlock.COPYBOX_BLOCK);

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, this.items);
    }

    @Override
    @NonNull
    public Component getDisplayName() {
        return Component.translatable("block.copybox.copybox_block_entity");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new CopyBoxBlockEntityMenu(containerId, inventory, this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (level == null) return;

        BlockState state = getBlockState();
        level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
    }

    private int ticksSinceLast = 0;
    public static void tick(Level level,
                            BlockPos blockPos,
                            BlockState blockState,
                            CopyBoxBlockEntity entity) {
        entity.ticksSinceLast++;
        if (entity.ticksSinceLast < 60) return;
        entity.ticksSinceLast = 0;
        ItemStack source = entity.getItem(0);
        if (source.isEmpty()) return;
        for (int i = 1; i < entity.items.size(); i++) {
            if (!entity.items.get(i).isEmpty()) continue;
            ItemStack template = source.copy();
            entity.items.set(i, template.copy());
        }
        entity.setChanged();
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        // 把 items 列表包装成 ItemContainerContents 组件
        builder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.items));
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter getter) {
        super.applyImplicitComponents(getter);
        // 从物品的组件里把物品栏数据读回来
        ItemContainerContents contents = getter.get(DataComponents.CONTAINER);
        if (contents != null) {
            contents.copyInto(this.items);
        }
    }

    public static void initialize() {
    }
}
