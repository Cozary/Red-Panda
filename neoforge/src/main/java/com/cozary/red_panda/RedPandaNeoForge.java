package com.cozary.red_panda;


import com.cozary.red_panda.entity.RedPandaEntity;
import com.cozary.red_panda.init.ModEntityTypes;
import com.cozary.red_panda.init.ModSpawnEggs;
import com.cozary.red_panda.init.ModTabs;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(RedPanda.MOD_ID)
public class RedPandaNeoForge {

    public RedPandaNeoForge(IEventBus eventBus) {
        eventBus.addListener(this::setupCommon);


        RedPanda.LOG.info("Hello NeoForge world!");
        RedPanda.init();
        ModTabs.init(eventBus);

        ModSpawnEggs.loadClass();
    }

    public void setupCommon(final FMLCommonSetupEvent event) {

        event.enqueueWork(() -> {
                    SpawnPlacements.register(ModEntityTypes.RED_PANDA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RedPandaEntity::checkRedPandaEntitySpawnRules);
                }
        );
    }
}