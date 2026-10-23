package com.amazingcopybox;


import com.mojang.serialization.MapCodec;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;


public class CopyBoxBlock extends BaseEntityBlock {
    public CopyBoxBlock(Properties settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CopyBoxBlockEntity(pos, state);
    }

    private static BlockItemId create(String name) {
        Identifier id = CopyBox.id(name);
        return BlockItemId.create(id, id);
    }

    private static Block register(BlockItemId blockItemId,
                                  Function<Properties, Block> blockFactory,
                                  BlockBehaviour.Properties properties) {
        // 1. 创建并注册方块
        Block block = blockFactory.apply(properties.setId(blockItemId.block()));
        Registry.register(BuiltInRegistries.BLOCK, blockItemId.block(), block);
        return block;
    }

    public static final BlockItemId COPYBOX_BLOCK_ID = create("copybox_block");
    public static final Block COPYBOX_BLOCK = register(
            COPYBOX_BLOCK_ID,
            CopyBoxBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.GRASS)
    );

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof CopyBoxBlockEntity copyBoxBlockEntity) {
            player.openMenu(copyBoxBlockEntity);
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, CopyBoxBlockEntity.COPYBOX_BLOCK_ENTITY_TYPE, CopyBoxBlockEntity::tick);
    }


    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        // 只在服务端处理，且不是创造模式
        if (!level.isClientSide() && !player.isCreative()) {
            if (level.getBlockEntity(pos) instanceof CopyBoxBlockEntity copyBox) {
                // 1. 构造带数据的物品
                ItemStack stack = new ItemStack(CopyBoxBlockItem.COPYBOX_BLOCK_ITEM);
                ItemContainerContents contents = ItemContainerContents.fromItems(copyBox.getItems());
                stack.set(DataComponents.CONTAINER, contents);

                // 2. 清空 BlockEntity，防止原版战利品表再爆一次
                copyBox.clearContent();

                // 3. 把物品掉出来
                Block.popResource(level, pos, stack);
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    public static void initialize() {
    }
}