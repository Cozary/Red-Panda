package com.cozary.red_panda.register;

import com.cozary.red_panda.RedPanda;
import com.cozary.red_panda.entity.RedPandaEntity;
import com.cozary.red_panda.init.ModEntityTypes;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RedPanda.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpawnPlacementRegister {

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(ModEntityTypes.RED_PANDA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RedPandaEntity::checkRedPandaEntitySpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
    }
}
