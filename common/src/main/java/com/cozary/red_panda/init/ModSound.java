package com.cozary.red_panda.init;

import com.cozary.red_panda.RedPanda;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSound {

    public static final RegistrationProvider<SoundEvent> SOUNDS = RegistrationProvider.get(Registries.SOUND_EVENT, RedPanda.MOD_ID);


    public static final RegistryObject<SoundEvent> RED_PANDA_EAT = createSoundEvent("entity.red_panda.eat");
    public static final RegistryObject<SoundEvent> RED_PANDA_SLEEP = createSoundEvent("entity.red_panda.sleep");
    public static final RegistryObject<SoundEvent> RED_PANDA_HURT = createSoundEvent("entity.red_panda.hurt");
    public static final RegistryObject<SoundEvent> RED_PANDA_DEATH = createSoundEvent("entity.red_panda.death");
    public static final RegistryObject<SoundEvent> RED_PANDA_SNIFF = createSoundEvent("entity.red_panda.sniff");

    public static final RegistryObject<SoundEvent> RED_PANDA_SCREECH = createSoundEvent("entity.red_panda.screech");
    public static final RegistryObject<SoundEvent> RED_PANDA_AMBIENT = createSoundEvent("entity.red_panda.ambient");

    private static RegistryObject<SoundEvent> createSoundEvent(final String soundName) {
        return SOUNDS.register(soundName, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(RedPanda.MOD_ID, soundName)));
    }

    public static void loadClass() {
    }

}
