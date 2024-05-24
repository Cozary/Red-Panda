package com.cozary.red_panda.register;

import com.cozary.red_panda.RedPanda;
import com.cozary.red_panda.entity.RedPandaEntity;
import com.cozary.red_panda.init.ModEntityTypes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RedPanda.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegister {


    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.RED_PANDA.get(), RedPandaEntity.createAttributes().build());

    }
}
