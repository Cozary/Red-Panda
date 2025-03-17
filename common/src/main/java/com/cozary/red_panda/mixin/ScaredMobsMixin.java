package com.cozary.red_panda.mixin;

import com.cozary.red_panda.entity.RedPandaEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Ravager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class ScaredMobsMixin {

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void addScaredGoal(CallbackInfo ci) {
        Mob mob = (Mob) (Object) this; // Cast the current entity

        // Check if the mob is in the scared list
        if (mob instanceof PolarBear || mob instanceof Ravager || mob instanceof Wolf || isIllager(mob)) {
            // Use the accessor to get goalSelector
            ((MobAccessor) mob).getGoalSelector().addGoal(1, new AvoidEntityGoal<>(
                    (PathfinderMob) mob, // The entity that will flee
                    RedPandaEntity.class, // Flee from Red Pandas
                    8.0F, // Detection range (8 blocks)
                    1.2D, // Walking speed multiplier
                    1.5D  // Sprinting speed multiplier
            ));
        }
    }

    // Helper method to check if a mob is an Illager
    private static boolean isIllager(Mob mob) {
        return mob instanceof AbstractIllager; // Covers Evokers, Vindicators, Pillagers, etc.
    }
}
