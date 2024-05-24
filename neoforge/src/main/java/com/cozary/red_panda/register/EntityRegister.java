package com.cozary.red_panda.register;

import com.cozary.red_panda.RedPanda;
import com.cozary.red_panda.entity.RedPandaEntity;
import com.cozary.red_panda.init.ModEntityTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod.EventBusSubscriber(modid = RedPanda.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegister {


    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.RED_PANDA.get(), RedPandaEntity.createAttributes().build());

    }
}
