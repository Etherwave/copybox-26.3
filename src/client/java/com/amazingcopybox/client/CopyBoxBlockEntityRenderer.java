package com.amazingcopybox.client;


import com.amazingcopybox.CopyBoxBlockEntity;
//import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

import java.util.logging.Logger;

import static com.amazingcopybox.CopyBox.LOGGER;

public class CopyBoxBlockEntityRenderer implements BlockEntityRenderer<CopyBoxBlockEntity, CopyBoxBlockEntityRenderState> {
    public CopyBoxBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public CopyBoxBlockEntityRenderState createRenderState() {
//        LOGGER.info("createRenderState");
        return new CopyBoxBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(CopyBoxBlockEntity blockEntity,
                                   CopyBoxBlockEntityRenderState state,
                                   float tickProgress,
                                   Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
//        LOGGER.info("extractRenderState");
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);

        state.getItemRenderState().clear();
        ItemStack stack = blockEntity.getItem(0);
//        LOGGER.info("stack is empty: " + stack.isEmpty());
        if (!stack.isEmpty()) {
            Minecraft.getInstance().getItemModelResolver().updateForTopItem(
                state.getItemRenderState(), stack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0
            );
//            LOGGER.info("lightCoords: " + state.lightCoords); is 0
            BlockPos pos = blockEntity.getBlockPos().above(); // 顶面位置
            int sky = blockEntity.getLevel().getBrightness(LightLayer.SKY, pos);
            int block = blockEntity.getLevel().getBrightness(LightLayer.BLOCK, pos);
            state.lightCoords = (sky << 20) | (block << 4);
//            state.lightCoords = 0xF000F0; // 这是常见的全亮打包光照值
        }
    }

    @Override
    public void submit(CopyBoxBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        if (state.getItemRenderState().isEmpty()) return;
        matrices.pushPose();
        Transformation rotate = new Transformation(
                new Vector3f(0.5f, 1.0f, 0.5f), // 平移
                Axis.XP.rotationDegrees(90),               // 左旋转
                new Vector3f(0.5f, 0.5f, 0.5f), // 缩放
                new Quaternionf()       // 右旋转（单位四元数，不旋转）
        );
        matrices.mulPose(rotate);
        state.getItemRenderState().submit(
                matrices,
                queue,        // 传递 SubmitNodeCollector
                state.lightCoords, // 光照
                OverlayTexture.NO_OVERLAY,
                0             // outline color
        );

        matrices.popPose();
    }
}