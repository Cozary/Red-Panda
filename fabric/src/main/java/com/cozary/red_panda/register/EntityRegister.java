package com.cozary.red_panda.register;

import com.cozary.red_panda.entity.RedPandaEntity;
import com.cozary.red_panda.init.ModEntityTypes;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public final class EntityRegister {

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntityTypes.RED_PANDA.get(), RedPandaEntity.createAttributes());
    }
}
