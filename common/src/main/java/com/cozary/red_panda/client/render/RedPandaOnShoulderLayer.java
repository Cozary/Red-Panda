package com.cozary.red_panda.client.render;

import com.cozary.red_panda.client.model.RedPandaModel;
import com.cozary.red_panda.init.ModEntityTypes;
import com.cozary.red_panda.util.ClientEventBusSubscriber;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class RedPandaOnShoulderLayer<T extends Player> extends RenderLayer<T, PlayerModel<T>> {
    private final RedPandaModel model;

    public RedPandaOnShoulderLayer(LivingEntityRenderer<?, ?> p_174511_, EntityModelSet p_174512_) {
        super((RenderLayerParent<T, PlayerModel<T>>) p_174511_);
        this.model = new RedPandaModel(p_174512_.bakeLayer(ClientEventBusSubscriber.RED_PANDA));
    }

    public void render(@NotNull PoseStack p_117307_, @NotNull MultiBufferSource p_117308_, int p_117309_, @NotNull T p_117310_, float p_117311_, float p_117312_, float p_117313_, float p_117314_, float p_117315_, float p_117316_) {
        this.render(p_117307_, p_117308_, p_117309_, p_117310_, p_117311_, p_117312_, p_117315_, p_117316_, true);
        this.render(p_117307_, p_117308_, p_117309_, p_117310_, p_117311_, p_117312_, p_117315_, p_117316_, false);
    }

    private void render(PoseStack p_117318_, MultiBufferSource p_117319_, int p_117320_, T p_117321_, float p_117322_, float p_117323_, float p_117324_, float p_117325_, boolean p_117326_) {
        CompoundTag compoundtag = p_117326_ ? p_117321_.getShoulderEntityLeft() : p_117321_.getShoulderEntityRight();
        EntityType.byString(compoundtag.getString("id")).filter((entityType) -> {
            return entityType == ModEntityTypes.RED_PANDA.get();
        }).ifPresent((entityType) -> {
            p_117318_.pushPose();
            p_117318_.translate(p_117326_ ? (double) 0.4F : (double) -0.4F, p_117321_.isCrouching() ? (double) -1.3F : -1.5D, 0.0D);
            VertexConsumer vertexconsumer = p_117319_.getBuffer(this.model.renderType(RedPandaRenderer.TEXTURE));
            this.model.renderOnShoulder(p_117318_, vertexconsumer, p_117320_, OverlayTexture.NO_OVERLAY, p_117322_, p_117323_, p_117324_, p_117325_, p_117321_.tickCount);
            p_117318_.popPose();
        });
    }
}
