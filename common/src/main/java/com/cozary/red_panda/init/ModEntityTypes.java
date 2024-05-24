package com.cozary.red_panda.init;

import com.cozary.red_panda.RedPanda;
import com.cozary.red_panda.entity.RedPandaEntity;
import com.google.common.collect.Sets;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class ModEntityTypes {

    public static final RegistrationProvider<EntityType<?>> ENTITY_TYPES = RegistrationProvider.get(Registries.ENTITY_TYPE, RedPanda.MOD_ID);
    public static LinkedHashSet<RegistryObject<EntityType<?>>> ENTITY_LIST = Sets.newLinkedHashSet();

    public static final RegistryObject<EntityType<RedPandaEntity>> RED_PANDA = registerEntitiesList("red_panda", () -> EntityType.Builder.of(RedPandaEntity::new, MobCategory.CREATURE)
            .sized(0.6F, 0.7F).clientTrackingRange(32)
            .build(new ResourceLocation(RedPanda.MOD_ID, "red_panda").toString()));

    @SuppressWarnings("unchecked")
    public static <T extends EntityType<?>> RegistryObject<T> registerEntitiesList(final String name, final Supplier<? extends T> supplier) {
        RegistryObject<T> entity = ENTITY_TYPES.register(name, supplier);
        ENTITY_LIST.add((RegistryObject<EntityType<?>>) entity);
        return entity;
    }

    public static void loadClass() {
    }

}
