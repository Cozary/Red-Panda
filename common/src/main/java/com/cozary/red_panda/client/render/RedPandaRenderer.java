package com.cozary.red_panda.client.render;

import com.cozary.red_panda.RedPanda;
import com.cozary.red_panda.client.model.RedPandaModel;
import com.cozary.red_panda.entity.RedPandaEntity;
import com.cozary.red_panda.util.ClientEventBusSubscriber;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RedPandaRenderer extends MobRenderer<RedPandaEntity, RedPandaModel<RedPandaEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(RedPanda.MOD_ID, "textures/entity/red_panda.png");

    public RedPandaRenderer(EntityRendererProvider.Context context) {
        super(context, new RedPandaModel<>(context.bakeLayer(ClientEventBusSubscriber.RED_PANDA)), 0.5F);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull RedPandaEntity p_114041_) {
        return TEXTURE;
    }

    @Override
    protected void scale(RedPandaEntity entity, @NotNull PoseStack poseStack, float p_115316_) {
        if (entity.isBaby()) poseStack.scale(0.6F, 0.6F, 0.6F);
        else poseStack.scale(1F, 1F, 1F);
    }
}
