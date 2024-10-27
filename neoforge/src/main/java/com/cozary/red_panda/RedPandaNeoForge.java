package com.cozary.red_panda;


import com.cozary.red_panda.entity.RedPandaEntity;
import com.cozary.red_panda.init.ModEntityTypes;
import com.cozary.red_panda.init.ModSpawnEggs;
import com.cozary.red_panda.init.ModTabs;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(RedPanda.MOD_ID)
public class RedPandaNeoForge {

    public RedPandaNeoForge(IEventBus eventBus) {
        RedPanda.LOG.info("Hello NeoForge world!");
        RedPanda.init();
        ModTabs.init(eventBus);

        ModSpawnEggs.loadClass();
    }
}