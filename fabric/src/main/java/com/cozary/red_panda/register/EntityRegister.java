package com.cozary.red_panda.register;

import com.cozary.red_panda.entity.RedPandaEntity;
import com.cozary.red_panda.init.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public final class EntityRegister implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricDefaultAttributeRegistry.register(ModEntityTypes.RED_PANDA.get(), RedPandaEntity.createAttributes());

    }
}
