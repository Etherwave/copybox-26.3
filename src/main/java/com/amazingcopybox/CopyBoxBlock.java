package com.amazingcopybox;


import com.mojang.serialization.MapCodec;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
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
//
//        if (!(level.getBlockEntity(pos) instanceof CopyBoxBlockEntity copyBoxBlockEntity)) {
//            return super.useWithoutItem(state, level, pos, player, hit);
//        }

//        copyBoxBlockEntity.incrementClicks();

//        if (level.isClientSide()) {
//            player.sendOverlayMessage(
//                    Component.literal(
//                            "You've clicked the block for the "
//                                    + copyBoxBlockEntity.getClicks() + "th time."));
//        }
//        if (!player.getItemInHand(hand).isEmpty() && copyBoxBlockEntity.isEmpty()) {
//            copyBoxBlockEntity.setItem(0, player.getItemInHand(hand).copy());
//            player.getItemInHand(hand).setCount(0);
//        }
        return InteractionResult.SUCCESS;
    }

//    @Override
//    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
//        if (!(world.getBlockEntity(pos) instanceof CopyBoxBlockEntity copyBoxBlockEntity)) {
//            return InteractionResult.PASS;
//        }
//        if (!player.getItemInHand(hand).isEmpty() && copyBoxBlockEntity.isEmpty()) {
//            copyBoxBlockEntity.setItem(0, player.getItemInHand(hand).copy());
//            player.getItemInHand(hand).setCount(0);
//        }
//        return InteractionResult.SUCCESS;
//    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, CopyBoxBlockEntity.COPYBOX_BLOCK_ENTITY_TYPE, CopyBoxBlockEntity::tick);
    }

    public static void initialize() {
    }
//
//    @Override
//    public void onInitialize() {
//        CopyBoxBlock.initialize();
//    }
}