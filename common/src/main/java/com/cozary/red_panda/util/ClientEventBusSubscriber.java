package com.cozary.red_panda.util;

import com.cozary.red_panda.RedPanda;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ClientEventBusSubscriber {

    public static ModelLayerLocation RED_PANDA = new ModelLayerLocation(new ResourceLocation(RedPanda.MOD_ID, "red_panda"), "red_panda");

}
