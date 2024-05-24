package com.cozary.red_panda.init;

import com.cozary.red_panda.RedPanda;
import com.google.common.collect.Sets;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class ModSpawnEggs {

    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, RedPanda.MOD_ID);

    public static LinkedHashSet<RegistryObject<Item>> SPAWNEGGS_TAB = Sets.newLinkedHashSet();

    public static final Supplier<Item> RED_PANDA_EGG = registerWithTab("red_panda_spawn_egg", () -> new SpawnEggItem(ModEntityTypes.RED_PANDA.get(), 0xFFFFFF, 0xffa500, new Item.Properties()));

    public static RegistryObject<Item> registerWithTab(final String name, final Supplier<? extends Item> supplier) {
        RegistryObject<Item> item = ITEMS.register(name, supplier);
        SPAWNEGGS_TAB.add(item);
        return item;
    }

    public static void loadClass() {
    }

}
