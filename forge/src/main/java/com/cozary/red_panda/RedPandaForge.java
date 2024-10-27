package com.cozary.red_panda;

import com.cozary.red_panda.entity.RedPandaEntity;
import com.cozary.red_panda.init.ModEntityTypes;
import com.cozary.red_panda.init.ModSpawnEggs;
import com.cozary.red_panda.init.ModTabs;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RedPanda.MOD_ID)
public class RedPandaForge {

    public RedPandaForge() {

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        RedPanda.LOG.info("Hello Forge world!");
        RedPanda.init();

        ModTabs.CREATIVE_MODE_TAB.register(eventBus);
        ModSpawnEggs.loadClass();

    }

}