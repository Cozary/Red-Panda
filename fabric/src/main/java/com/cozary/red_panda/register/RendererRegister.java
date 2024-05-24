package com.cozary.red_panda.register;

import com.cozary.red_panda.client.model.RedPandaModel;
import com.cozary.red_panda.client.render.RedPandaOnShoulderLayer;
import com.cozary.red_panda.client.render.RedPandaRenderer;
import com.cozary.red_panda.init.ModEntityTypes;
import com.cozary.red_panda.util.ClientEventBusSubscriber;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

@Environment(EnvType.CLIENT)
public class RendererRegister implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        EntityModelLayerRegistry.registerModelLayer(ClientEventBusSubscriber.RED_PANDA, RedPandaModel::createBodyLayer);
        EntityRendererRegistry.register(ModEntityTypes.RED_PANDA.get(), RedPandaRenderer::new);

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof PlayerRenderer) {
                registrationHelper.register(new RedPandaOnShoulderLayer<>(entityRenderer, context.getModelSet()));
            }
        });
    }
}
