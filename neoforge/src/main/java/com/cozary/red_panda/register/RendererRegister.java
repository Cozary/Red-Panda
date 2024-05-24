package com.cozary.red_panda.register;

import com.cozary.red_panda.RedPanda;
import com.cozary.red_panda.client.model.RedPandaModel;
import com.cozary.red_panda.client.render.RedPandaOnShoulderLayer;
import com.cozary.red_panda.client.render.RedPandaRenderer;
import com.cozary.red_panda.init.ModEntityTypes;
import com.cozary.red_panda.util.ClientEventBusSubscriber;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;


@Mod.EventBusSubscriber(modid = RedPanda.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RendererRegister {

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.RED_PANDA.get(), RedPandaRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ClientEventBusSubscriber.RED_PANDA, RedPandaModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        event.getSkins().forEach(skinTypeName -> {
            if (event.getSkin(skinTypeName) instanceof PlayerRenderer renderer) {
                renderer.addLayer(new RedPandaOnShoulderLayer<>(renderer, event.getEntityModels()));
            }
        });
    }

}
