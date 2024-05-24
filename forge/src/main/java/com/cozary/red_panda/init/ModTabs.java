package com.cozary.red_panda.init;

import com.cozary.red_panda.RedPanda;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RedPanda.MOD_ID);

    public static RegistryObject<CreativeModeTab> RED_PANDA_TAB = CREATIVE_MODE_TAB.register("red_panda", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModSpawnEggs.RED_PANDA_EGG.get()))
            .title(Component.translatable("itemGroup.red_panda"))
            .displayItems((parameters, output) -> ModSpawnEggs.SPAWNEGGS_TAB.forEach((item) -> output.accept(item.get())))
            .build());

}


