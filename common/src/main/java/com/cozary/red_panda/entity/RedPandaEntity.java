package com.cozary.red_panda.entity;

import com.cozary.red_panda.init.ModEntityTypes;
import com.cozary.red_panda.init.ModSound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class RedPandaEntity extends ShoulderRidingEntity {

    static final Predicate<ItemEntity> ALLOWED_ITEMS = (itemEntity) -> {
        ItemStack itemstack = itemEntity.getItem();
        return !itemEntity.hasPickUpDelay() && itemEntity.isAlive() && ((itemstack.is(Blocks.BAMBOO.asItem()) || (itemstack.is(Items.SWEET_BERRIES) || (itemstack.is(Items.EGG)))));
    };
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> EAT_BAMBOO_COUNTER = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.INT);
    private static final Predicate<Entity> AVOID_PLAYERS = (p_28463_) -> {
        return !p_28463_.isDiscrete() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(p_28463_);
    };
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.SWEET_BERRIES, Items.EGG, Items.BAMBOO);

    public RedPandaEntity(EntityType<? extends RedPandaEntity> p_28451_, Level p_28452_) {
        super(p_28451_, p_28452_);
        this.lookControl = new RedPandaEntityLookControl();
        this.moveControl = new RedPandaEntityMoveControl();
        this.setPathfindingMalus(BlockPathTypes.DANGER_OTHER, 0.0F);
        this.setCanPickUpLoot(true);
        this.setTame(false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.MAX_HEALTH, 15.0D).add(Attributes.FOLLOW_RANGE, 32.0D).add(Attributes.ATTACK_DAMAGE, 2.5D);
    }

    public static boolean checkRedPandaEntitySpawnRules(EntityType<? extends RedPandaEntity> creeper, ServerLevelAccessor p_186215_, MobSpawnType p_186216_, BlockPos p_186217_, RandomSource p_186218_) {
        return p_186215_.getBlockState(p_186217_.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && isBrightEnoughToSpawn(p_186215_, p_186217_);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
        this.entityData.define(EAT_BAMBOO_COUNTER, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RedPandaEntityFloatGoal());
        this.goalSelector.addGoal(2, new LandOnOwnersShoulderGoal(this));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new RedPandaSearchForPlayerItemsGoal());
        this.goalSelector.addGoal(2, new RedPandaEntityPanicGoal(2.2D));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 16.0F, 1.6D, 1.4D, (p_28596_) -> {
            return AVOID_PLAYERS.test(p_28596_) && !(this.getOwnerUUID() == p_28596_.getUUID());
        }));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Wolf.class, 8.0F, 1.6D, 1.4D, (p_28590_) -> {
            return !((Wolf) p_28590_).isTame();
        }));
        this.goalSelector.addGoal(6, new SeekShelterGoal(1.25D));
        this.goalSelector.addGoal(7, new SleepGoal());
        this.goalSelector.addGoal(8, new RedPandaEntityFollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(1, new RedPandaEntityEatBambooGoal(1.2F, 12, 1));
        this.goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(12, new RedPandaEntityLookAtPlayerGoal(this, Player.class, 24.0F));
        this.goalSelector.addGoal(13, new PerchAndSearchGoal());
        this.goalSelector.addGoal(0, new FoxSearchForItemsGoal());

    }

    @Override
    protected void pickUpItem(@NotNull ItemEntity p_29121_) {
        if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && ALLOWED_ITEMS.test(p_29121_) && FOOD_ITEMS.test(p_29121_.getItem())) {
            this.onItemPickup(p_29121_);
            ItemStack itemstack = p_29121_.getItem();
            this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
            this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0F;
            this.take(p_29121_, itemstack.getCount());
            p_29121_.discard();
        }

    }


    @Override
    public boolean canSitOnShoulder() {
        return !this.isBaby();
    }

    @Override
    public @NotNull SoundEvent getEatingSound(@NotNull ItemStack p_28540_) {
        return ModSound.RED_PANDA_EAT.get();
    }

    @Override
    public void aiStep() {
        if (this.isSleeping() || this.isImmobile()) {
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }

        super.aiStep();
    }

    @Override
    protected boolean isImmobile() {
        return this.isDeadOrDying();
    }

    @Override
    public RedPandaEntity getBreedOffspring(@NotNull ServerLevel p_148912_, @NotNull AgeableMob p_148913_) {
        RedPandaEntity redPandaEntity = ModEntityTypes.RED_PANDA.get().create(p_148912_);
        UUID uuid = this.getOwnerUUID();
        if (uuid != null) {
            assert redPandaEntity != null;
            redPandaEntity.setOwnerUUID(uuid);
            redPandaEntity.setTame(true);
        }
        return redPandaEntity;
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand p_30413_) {
        ItemStack itemstack = player.getItemInHand(p_30413_);
        if (this.level().isClientSide) {
            boolean flag = this.isOwnedBy(player) || this.isTame() || FOOD_ITEMS.test(itemstack) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            if (this.isTame()) {
                if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    if (itemstack.getItem() != Items.BAMBOO)
                        this.heal(Objects.requireNonNull(itemstack.getItem().getFoodProperties()).getNutrition());
                    return InteractionResult.SUCCESS;
                }

            } else if (FOOD_ITEMS.test(itemstack)) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (this.random.nextInt(3) == 0) {
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.setOrderedToSit(true);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }

                return InteractionResult.SUCCESS;
            }

            return super.mobInteract(player, p_30413_);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_28487_, @NotNull DifficultyInstance p_28488_, @NotNull MobSpawnType p_28489_, @Nullable SpawnGroupData p_28490_, @Nullable CompoundTag p_28491_) {
        Holder<Biome> holder = p_28487_.getBiome(this.blockPosition());
        boolean flag = false;

        if (flag) {
            this.setAge(-24000);
        }

        return super.finalizeSpawn(p_28487_, p_28488_, p_28489_, p_28490_, p_28491_);
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose p_28500_, @NotNull EntityDimensions p_28501_) {
        return this.isBaby() ? p_28501_.height * 0.85F : 0.4F;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag p_28518_) {
        super.addAdditionalSaveData(p_28518_);
        p_28518_.putBoolean("Scared", this.isScared());
        p_28518_.putBoolean("Sleeping", this.isSleeping());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag p_28493_) {
        super.readAdditionalSaveData(p_28493_);
        this.setSleeping(p_28493_.getBoolean("Scared"));
        this.setSleeping(p_28493_.getBoolean("Sleeping"));
    }

    public boolean isSleeping() {
        return this.getFlag(32);
    }

    void setSleeping(boolean p_28627_) {
        this.setFlag(32, p_28627_);
    }

    public boolean isScared() {
        return this.getFlag(2);
    }

    void setScared(boolean p_28627_) {
        this.setFlag(2, p_28627_);
    }

    private void setFlag(int p_28533_, boolean p_28534_) {
        if (p_28534_) {
            this.entityData.set(DATA_FLAGS_ID, (byte) (this.entityData.get(DATA_FLAGS_ID) | p_28533_));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte) (this.entityData.get(DATA_FLAGS_ID) & ~p_28533_));
        }

    }

    private boolean getFlag(int p_28609_) {
        return (this.entityData.get(DATA_FLAGS_ID) & p_28609_) != 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isEffectiveAi()) {
            boolean flag = this.isInWater();
            if (flag || this.getTarget() != null || this.level().isThundering()) {
                this.wakeUp();
            }

        }
        this.handleEating();
    }

    public boolean isEating() {
        return this.entityData.get(EAT_BAMBOO_COUNTER) > 0;
    }

    public void eat(boolean p_29217_) {
        this.entityData.set(EAT_BAMBOO_COUNTER, p_29217_ ? 1 : 0);
    }

    private int getEatCounter() {
        return this.entityData.get(EAT_BAMBOO_COUNTER);
    }

    private void setEatCounter(int p_29215_) {
        this.entityData.set(EAT_BAMBOO_COUNTER, p_29215_);
    }

    private void handleEating() {
        if (!this.isEating() && !this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && this.random.nextInt(80) == 1) {
            this.eat(true);
        } else if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
            this.eat(false);
        }

        if (this.isEating()) {
            this.addEatingParticles();

            if (!this.level().isClientSide && this.getEatCounter() > 10) {
                if (!this.level().isClientSide) {
                    if (this.getItemBySlot(EquipmentSlot.MAINHAND).getCount() > 1) {
                        ItemStack newBamboo = new ItemStack(Items.BAMBOO);
                        newBamboo.setCount(this.getItemBySlot(EquipmentSlot.MAINHAND).getCount() - 1);
                        this.setItemSlot(EquipmentSlot.MAINHAND, newBamboo);
                        this.eat(false);
                        this.setEatCounter(0);
                        return;
                    } else {
                        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    }
                }

                this.eat(false);
                this.setEatCounter(0);
                return;
            }

            this.setEatCounter(this.getEatCounter() + 1);
        }

    }

    private void addEatingParticles() {
        if (this.getEatCounter() % 5 == 0) {
            this.playSound(ModSound.RED_PANDA_EAT.get(), 0.5F + 0.5F * (float) this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

            for (int i = 0; i < 6; ++i) {
                Vec3 vec3 = new Vec3(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) this.random.nextFloat() - 0.5D) * 0.1D);
                vec3 = vec3.xRot(-this.getXRot() * ((float) Math.PI / 180F));
                vec3 = vec3.yRot(-this.getYRot() * ((float) Math.PI / 180F));
                double d0 = (double) (-this.random.nextFloat()) * 0.6D - 0.3D;
                Vec3 vec31 = new Vec3(((double) this.random.nextFloat() - 0.5D) * 0.8D, d0, 1.0D + ((double) this.random.nextFloat() - 0.5D) * 0.4D);
                vec31 = vec31.yRot(-this.yBodyRot * ((float) Math.PI / 180F));
                vec31 = vec31.add(this.getX(), this.getEyeY() + 1.0D, this.getZ());
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItemBySlot(EquipmentSlot.MAINHAND)), vec31.x, vec31.y, vec31.z, vec3.x, vec3.y + 0.05D, vec3.z);
            }
        }

    }

    @Override
    public boolean isFood(@NotNull ItemStack p_28594_) {
        return FOOD_ITEMS.test(p_28594_);
    }

    @Override
    protected int calculateFallDamage(float p_28545_, float p_28546_) {
        return Mth.ceil((p_28545_ - 7.0F) * p_28546_);
    }

    void wakeUp() {
        this.setSleeping(false);
    }

    void clearStates() {
        this.setScared(false);
        this.setSleeping(false);
    }

    boolean canMove() {
        return !this.isSleeping() && !this.isScared();
    }

    @Override
    public void playAmbientSound() {
        SoundEvent soundevent = this.getAmbientSound();
        if (soundevent == ModSound.RED_PANDA_SCREECH.get()) {
            this.playSound(soundevent, 2.0F, this.getVoicePitch());
        } else {
            super.playAmbientSound();
        }

    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return ModSound.RED_PANDA_SLEEP.get();
        } else {
            if (!this.level().isDay() && this.random.nextFloat() < 0.1F) {
                List<Player> list = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(16.0D, 16.0D, 16.0D), EntitySelector.NO_SPECTATORS);
                if (list.isEmpty()) {
                    return ModSound.RED_PANDA_SCREECH.get();
                }
            }

            return ModSound.RED_PANDA_AMBIENT.get();
        }
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource p_28548_) {
        return ModSound.RED_PANDA_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSound.RED_PANDA_DEATH.get();
    }

    @Override
    public @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0D, 0.55F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
    }

    class RedPandaSearchForPlayerItemsGoal extends Goal {
        public RedPandaSearchForPlayerItemsGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean canUse() {
            List<Player> list = RedPandaEntity.this.level().getEntitiesOfClass(Player.class, RedPandaEntity.this.getBoundingBox().inflate(16.0D, 16.0D, 16.0D));
            if (!list.isEmpty()) {
                for (Player player : list) {
                    ItemStack itemStack = player.getItemBySlot(EquipmentSlot.MAINHAND);
                    if (!(itemStack.getItem() instanceof SwordItem)) {
                        setScared(false);
                        return false;
                    } else {
                        if (itemStack.getItem() instanceof SwordItem) {
                            setScared(true);
                            RedPandaEntity.this.getNavigation().stop();
                            RedPandaEntity.this.lookControl.setLookAt(player.getX(), player.getEyeY(), player.getZ());
                        }
                    }

                }
            }
            return false;
        }

        public void tick() {
            List<Player> list = RedPandaEntity.this.level().getEntitiesOfClass(Player.class, RedPandaEntity.this.getBoundingBox().inflate(16.0D, 16.0D, 16.0D));
            if (!list.isEmpty()) {
                for (Player player : list) {
                    if (player.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
                        setScared(false);
                    } else {
                        ItemStack itemStack = player.getItemBySlot(EquipmentSlot.MAINHAND);
                        if (itemStack.getItem() instanceof SwordItem) {
                            setScared(true);
                            RedPandaEntity.this.getNavigation().stop();
                            RedPandaEntity.this.lookControl.setLookAt(player.getX(), player.getEyeY(), player.getZ());
                        }
                    }

                }
            }

        }

        public void start() {
            List<Player> list = RedPandaEntity.this.level().getEntitiesOfClass(Player.class, RedPandaEntity.this.getBoundingBox().inflate(16.0D, 16.0D, 16.0D));
            if (!list.isEmpty()) {
                for (Player player : list) {
                    if (player.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
                        setScared(false);
                    } else {
                        ItemStack itemStack = player.getItemBySlot(EquipmentSlot.MAINHAND);
                        if (itemStack.getItem() instanceof SwordItem) {
                            setScared(true);
                            RedPandaEntity.this.getNavigation().stop();
                            RedPandaEntity.this.lookControl.setLookAt(player.getX(), player.getEyeY(), player.getZ());
                        }
                    }

                }
            }

        }
    }

    class FoxSearchForItemsGoal extends Goal {
        public FoxSearchForItemsGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            if (!RedPandaEntity.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
                return false;
            } else if (RedPandaEntity.this.getTarget() == null && RedPandaEntity.this.getLastHurtByMob() == null) {
                if (!RedPandaEntity.this.canMove()) {
                    return false;
                } else if (RedPandaEntity.this.getRandom().nextInt(reducedTickDelay(10)) != 0) {
                    return false;
                } else {
                    List<ItemEntity> list = RedPandaEntity.this.level().getEntitiesOfClass(ItemEntity.class, RedPandaEntity.this.getBoundingBox().inflate(16.0D, 16.0D, 16.0D), RedPandaEntity.ALLOWED_ITEMS);
                    return !list.isEmpty() && RedPandaEntity.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
                }
            } else {
                return false;
            }
        }

        public void tick() {
            List<ItemEntity> list = RedPandaEntity.this.level().getEntitiesOfClass(ItemEntity.class, RedPandaEntity.this.getBoundingBox().inflate(16.0D, 16.0D, 16.0D), RedPandaEntity.ALLOWED_ITEMS);
            ItemStack itemstack = RedPandaEntity.this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (itemstack.isEmpty() && !list.isEmpty()) {
                RedPandaEntity.this.getNavigation().moveTo(list.get(0), 1.2F);
            }

        }

        public void start() {
            List<ItemEntity> list = RedPandaEntity.this.level().getEntitiesOfClass(ItemEntity.class, RedPandaEntity.this.getBoundingBox().inflate(16.0D, 16.0D, 16.0D), RedPandaEntity.ALLOWED_ITEMS);
            if (!list.isEmpty()) {
                RedPandaEntity.this.getNavigation().moveTo(list.get(0), 1.2F);
            }

        }
    }

    public class RedPandaEntityAlertableEntitiesSelector implements Predicate<LivingEntity> {
        public boolean test(LivingEntity p_28653_) {
            if (p_28653_ instanceof RedPandaEntity) {
                return false;
            } else if (!(p_28653_ instanceof Monster)) {
                if (p_28653_ instanceof TamableAnimal) {
                    return !((TamableAnimal) p_28653_).isTame();
                } else if (!(p_28653_ instanceof Player) || !p_28653_.isSpectator() && !((Player) p_28653_).isCreative()) {
                    if (!(RedPandaEntity.this.getOwnerUUID() == p_28653_.getUUID())) {
                        return false;
                    } else {
                        return !p_28653_.isSleeping() && !p_28653_.isDiscrete();
                    }
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    abstract class RedPandaEntityBehaviorGoal extends Goal {
        private final TargetingConditions alertableTargeting = TargetingConditions.forCombat().range(12.0D).ignoreLineOfSight().selector(RedPandaEntity.this.new RedPandaEntityAlertableEntitiesSelector());

        protected boolean hasShelter() {
            BlockPos blockpos = new BlockPos((int) RedPandaEntity.this.getX(), (int) RedPandaEntity.this.getBoundingBox().maxY, (int) RedPandaEntity.this.getZ());
            return !RedPandaEntity.this.level().canSeeSky(blockpos) && RedPandaEntity.this.getWalkTargetValue(blockpos) >= 0.0F;
        }

        protected boolean alertable() {
            return !RedPandaEntity.this.level().getNearbyEntities(LivingEntity.class, this.alertableTargeting, RedPandaEntity.this, RedPandaEntity.this.getBoundingBox().inflate(12.0D, 6.0D, 12.0D)).isEmpty();
        }
    }

    public class RedPandaEntityEatBambooGoal extends MoveToBlockGoal {
        protected int ticksWaited;

        public RedPandaEntityEatBambooGoal(double p_28675_, int p_28676_, int p_28677_) {
            super(RedPandaEntity.this, p_28675_, p_28676_, p_28677_);
        }

        public double acceptedDistance() {
            return 2.0D;
        }

        public boolean shouldRecalculatePath() {
            return this.tryTicks % 100 == 0;
        }

        protected boolean isValidTarget(LevelReader p_28680_, @NotNull BlockPos p_28681_) {
            BlockState blockstate = p_28680_.getBlockState(p_28681_);
            return blockstate.is(Blocks.BAMBOO);
        }

        public void tick() {
            if (this.isReachedTarget()) {
                if (this.ticksWaited >= 40) {
                    this.onReachedTarget();
                } else {
                    ++this.ticksWaited;
                }
            } else if (!this.isReachedTarget() && RedPandaEntity.this.random.nextFloat() < 0.05F) {
                RedPandaEntity.this.playSound(ModSound.RED_PANDA_SNIFF.get(), 1.0F, 1.0F);
            }

            super.tick();
        }

        protected void onReachedTarget() {
            if (RedPandaEntity.this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                BlockState blockstate = RedPandaEntity.this.level().getBlockState(this.blockPos);
                if (blockstate.is(Blocks.BAMBOO)) {
                    this.pickBreakBamboo(blockstate);
                }
            }
        }

        private void pickBreakBamboo(BlockState p_148929_) {
            RedPandaEntity.this.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
            RedPandaEntity.this.level().destroyBlock(this.blockPos, true);
        }

        public boolean canUse() {
            return !RedPandaEntity.this.isSleeping() && !RedPandaEntity.this.isScared() && super.canUse();
        }

        public void start() {
            this.ticksWaited = 0;
            super.start();
        }
    }

    class RedPandaEntityFloatGoal extends FloatGoal {
        public RedPandaEntityFloatGoal() {
            super(RedPandaEntity.this);
        }

        public void start() {
            super.start();
            RedPandaEntity.this.clearStates();
        }

        public boolean canUse() {
            return RedPandaEntity.this.isInWater() && RedPandaEntity.this.getFluidHeight(FluidTags.WATER) > 0.25D || RedPandaEntity.this.isInLava();
        }
    }

    class RedPandaEntityFollowParentGoal extends FollowParentGoal {
        private final RedPandaEntity RedPandaEntity;

        public RedPandaEntityFollowParentGoal(RedPandaEntity p_28696_, double p_28697_) {
            super(p_28696_, p_28697_);
            this.RedPandaEntity = p_28696_;
        }

        public boolean canUse() {
            return super.canUse();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        public void start() {
            this.RedPandaEntity.clearStates();
            super.start();
        }
    }

    class RedPandaEntityLookAtPlayerGoal extends LookAtPlayerGoal {
        public RedPandaEntityLookAtPlayerGoal(Mob p_28707_, Class<? extends LivingEntity> p_28708_, float p_28709_) {
            super(p_28707_, p_28708_, p_28709_);
        }

        public boolean canUse() {
            return super.canUse();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }
    }

    public class RedPandaEntityLookControl extends LookControl {
        public RedPandaEntityLookControl() {
            super(RedPandaEntity.this);
        }

        public void tick() {
            if (!RedPandaEntity.this.isSleeping() && !RedPandaEntity.this.isScared()) {
                super.tick();
            }

        }

        protected boolean resetXRotOnTick() {
            return !RedPandaEntity.this.isCrouching();
        }
    }

    class RedPandaEntityMoveControl extends MoveControl {
        public RedPandaEntityMoveControl() {
            super(RedPandaEntity.this);
        }

        public void tick() {
            if (RedPandaEntity.this.canMove()) {
                super.tick();
            }

        }
    }

    class RedPandaEntityPanicGoal extends PanicGoal {
        public RedPandaEntityPanicGoal(double p_28734_) {
            super(RedPandaEntity.this, p_28734_);
        }

        public boolean shouldPanic() {
            return super.shouldPanic();
        }
    }

    class PerchAndSearchGoal extends RedPandaEntityBehaviorGoal {
        private double relX;
        private double relZ;
        private int lookTime;
        private int looksRemaining;

        public PerchAndSearchGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            return RedPandaEntity.this.getLastHurtByMob() == null && RedPandaEntity.this.getRandom().nextFloat() < 0.02F && !RedPandaEntity.this.isSleeping() && !RedPandaEntity.this.isScared() && RedPandaEntity.this.getTarget() == null && RedPandaEntity.this.getNavigation().isDone() && !this.alertable() && !RedPandaEntity.this.isCrouching();
        }

        public boolean canContinueToUse() {
            return this.looksRemaining > 0;
        }

        public void start() {
            this.resetLook();
            this.looksRemaining = 2 + RedPandaEntity.this.getRandom().nextInt(3);
            RedPandaEntity.this.getNavigation().stop();
        }

        public void stop() {
        }

        public void tick() {
            --this.lookTime;
            if (this.lookTime <= 0) {
                --this.looksRemaining;
                this.resetLook();
            }

            RedPandaEntity.this.getLookControl().setLookAt(RedPandaEntity.this.getX() + this.relX, RedPandaEntity.this.getEyeY(), RedPandaEntity.this.getZ() + this.relZ, (float) RedPandaEntity.this.getMaxHeadYRot(), (float) RedPandaEntity.this.getMaxHeadXRot());
        }

        private void resetLook() {
            double d0 = (Math.PI * 2D) * RedPandaEntity.this.getRandom().nextDouble();
            this.relX = Math.cos(d0);
            this.relZ = Math.sin(d0);
            this.lookTime = this.adjustedTickDelay(80 + RedPandaEntity.this.getRandom().nextInt(20));
        }
    }

    class SeekShelterGoal extends FleeSunGoal {
        private int interval = reducedTickDelay(100);

        public SeekShelterGoal(double p_28777_) {
            super(RedPandaEntity.this, p_28777_);
        }

        public boolean canUse() {
            if (!RedPandaEntity.this.isSleeping() && !RedPandaEntity.this.isScared() && this.mob.getTarget() == null) {
                if (RedPandaEntity.this.level().isThundering()) {
                    return true;
                } else if (this.interval > 0) {
                    --this.interval;
                    return false;
                } else {
                    this.interval = 100;
                    BlockPos blockpos = this.mob.blockPosition();
                    return RedPandaEntity.this.level().isDay() && RedPandaEntity.this.level().canSeeSky(blockpos) && !((ServerLevel) RedPandaEntity.this.level()).isVillage(blockpos) && this.setWantedPos();
                }
            } else {
                return false;
            }
        }

        public void start() {
            RedPandaEntity.this.clearStates();
            super.start();
        }
    }

    class SleepGoal extends RedPandaEntityBehaviorGoal {
        private static final int WAIT_TIME_BEFORE_SLEEP = reducedTickDelay(140);
        private int countdown = RedPandaEntity.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);

        public SleepGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            if (RedPandaEntity.this.xxa == 0.0F && RedPandaEntity.this.yya == 0.0F && RedPandaEntity.this.zza == 0.0F) {
                return this.canSleep() || RedPandaEntity.this.isSleeping() && !RedPandaEntity.this.isScared();
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return this.canSleep();
        }

        private boolean canSleep() {
            if (this.countdown > 0) {
                --this.countdown;
                return false;
            } else {
                return RedPandaEntity.this.level().isDay() && this.hasShelter() && !this.alertable();
            }
        }

        public void stop() {
            this.countdown = RedPandaEntity.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
            RedPandaEntity.this.clearStates();
        }

        public void start() {
            RedPandaEntity.this.setSleeping(true);
            RedPandaEntity.this.getNavigation().stop();
            RedPandaEntity.this.getMoveControl().setWantedPosition(RedPandaEntity.this.getX(), RedPandaEntity.this.getY(), RedPandaEntity.this.getZ(), 0.0D);
        }
    }

}