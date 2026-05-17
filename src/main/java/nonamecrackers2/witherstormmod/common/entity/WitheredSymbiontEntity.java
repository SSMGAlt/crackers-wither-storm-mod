package nonamecrackers2.witherstormmod.common.entity;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.BossEvent.BossBarColor;
import net.minecraft.world.BossEvent.BossBarOverlay;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent.Context;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SymbiontSpell;
import nonamecrackers2.witherstormmod.api.common.registry.WitherStormModRegistries;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.goal.symbiont.PrepareSpellGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.symbiont.SummonMobsGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.symbiont.UseSpellGoal;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSymbiontSpellTypes;
import nonamecrackers2.witherstormmod.common.serializer.WitherStormModDataSerializers;
import nonamecrackers2.witherstormmod.common.util.ConditionalLookController;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;
import nonamecrackers2.witherstormmod.common.world.tainting.WorldTainting;
import org.jetbrains.annotations.NotNull;

public class WitheredSymbiontEntity extends Monster implements BossThemeEntity {
   private static final EntityDataAccessor<WitheredSymbiontEntity.BossfightStage> BOSSFIGHT_STAGE = SynchedEntityData.defineId(
      WitheredSymbiontEntity.class, WitherStormModDataSerializers.BOSSFIGHT_STAGE_ENUM
   );
   private static final EntityDataAccessor<SpellType> SPELL_TYPE = SynchedEntityData.defineId(
      WitheredSymbiontEntity.class, WitherStormModDataSerializers.SPELL_TYPE
   );
   private static final EntityDataAccessor<Boolean> NON_BOSS_MODE = SynchedEntityData.defineId(WitheredSymbiontEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> RUSH_MODE = SynchedEntityData.defineId(WitheredSymbiontEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> SHOULD_NOT_GO_OVER_HALF = SynchedEntityData.defineId(
      WitheredSymbiontEntity.class, EntityDataSerializers.BOOLEAN
   );
   public static final Predicate<LivingEntity> TARGET_PREDICATE = entity -> entity.isAttackable() && entity instanceof Player;
   public static final Predicate<LivingEntity> MOB_TARGET_PREDICATE = entity -> entity.isAttackable()
      && !(entity instanceof WitherSickened)
      && !(entity instanceof WitheredSymbiontEntity)
      && !(entity instanceof WitherStormEntity)
      && !(entity instanceof WitherStormHeadEntity)
      && !(entity instanceof TentacleEntity)
      && !(entity instanceof EnderMan)
      && !(entity instanceof EnderDragon)
      && !(entity instanceof WitherBoss)
      && !(entity instanceof WitherSkeleton)
      && (
         entity instanceof AbstractVillager
            || entity instanceof AbstractGolem
            || entity instanceof Monster
            || entity instanceof Animal
            || entity instanceof NeutralMob
            || entity instanceof Player
      );
   public static final Predicate<LivingEntity> PULSE_PREDICATE = entity -> entity.isAttackable()
      && !(entity instanceof WitheredSymbiontEntity)
      && !(entity instanceof WitherStormEntity)
      && !(entity instanceof WitherStormHeadEntity)
      && !(entity instanceof TentacleEntity)
      && !(entity instanceof CommandBlockEntity);
   private static final SimpleWeightedRandomList<EntityType<? extends Mob>> SYMBIONT_NORMAL_MOBS;
   private static final SimpleWeightedRandomList<EntityType<? extends Mob>> SYMBIONT_HARDER_MOBS;

   static {
      SimpleWeightedRandomList.Builder<EntityType<? extends Mob>> bn = SimpleWeightedRandomList.builder();
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_ZOMBIE.get(), 8);
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_VILLAGER.get(), 4);
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_SKELETON.get(), 8);
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_SPIDER.get(), 4);
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_CREEPER.get(), 1);
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_SNOW_GOLEM.get(), 2);
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_CHICKEN.get(), 3);
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_COW.get(), 3);
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_MUSHROOM_COW.get(), 1);
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_PIG.get(), 3);
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_BEE.get(), 4);
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_PARROT.get(), 4);
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_WOLF.get(), 4);
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_CAT.get(), 4);
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_PILLAGER.get(), 3);
      bn.add((EntityType)WitherStormModEntityTypes.SICKENED_VINDICATOR.get(), 3);
      SYMBIONT_NORMAL_MOBS = bn.build();

      SimpleWeightedRandomList.Builder<EntityType<? extends Mob>> bh = SimpleWeightedRandomList.builder();
      bh.add((EntityType)WitherStormModEntityTypes.SICKENED_ZOMBIE.get(), 8);
      bh.add((EntityType)WitherStormModEntityTypes.SICKENED_VILLAGER.get(), 6);
      bh.add((EntityType)WitherStormModEntityTypes.SICKENED_SKELETON.get(), 8);
      bh.add((EntityType)WitherStormModEntityTypes.SICKENED_SPIDER.get(), 6);
      bh.add((EntityType)WitherStormModEntityTypes.SICKENED_CREEPER.get(), 1);
      bh.add((EntityType)WitherStormModEntityTypes.SICKENED_SNOW_GOLEM.get(), 1);
      bh.add((EntityType)WitherStormModEntityTypes.SICKENED_PHANTOM.get(), 3);
      bh.add((EntityType)WitherStormModEntityTypes.SICKENED_BEE.get(), 6);
      bh.add((EntityType)WitherStormModEntityTypes.SICKENED_PARROT.get(), 1);
      bh.add((EntityType)WitherStormModEntityTypes.SICKENED_WOLF.get(), 2);
      bh.add((EntityType)WitherStormModEntityTypes.SICKENED_CAT.get(), 2);
      bh.add((EntityType)WitherStormModEntityTypes.SICKENED_PILLAGER.get(), 3);
      bh.add((EntityType)WitherStormModEntityTypes.SICKENED_VINDICATOR.get(), 6);
      SYMBIONT_HARDER_MOBS = bh.build();
   }
   private List<Goal> bossFightGoals;
   private MeleeAttackGoal attackGoal;
   private PrepareSpellGoal prepareSpellGoal;
   private UseSpellGoal useSpellGoal;
   private SummonMobsGoal summonMobsGoal;
   private WitheredSymbiontEntity.DoNothingGoal doNothingGoal;
   private int stageTicks;
   private int spellCastingTime;
   private int nextSpellPickCount;
   private boolean isDoingSmash;
   private int smashAirTime;
   private List<LivingEntity> entitiesToThrow = new ArrayList<>();
   private int spellsUsed;
   private float crouchAnim;
   private float crouchAnimO;
   private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossBarColor.PURPLE, BossBarOverlay.PROGRESS);
   private int specialDeathTime;
   @Nullable
   private UUID summoner;
   private int attackDelay;
   private List<ItemStack> dropItems = Lists.newArrayList();
   private float tearAlpha;
   private float tearAlphaO;
   private List<UUID> fightContributors = Lists.newArrayList();
   @Nullable
   private SymbiontSpell spellInstance;

   public WitheredSymbiontEntity(EntityType<? extends WitheredSymbiontEntity> type, Level world) {
      super(type, world);
      this.xpReward = 150;
      this.lookControl = new ConditionalLookController<>(this, entity -> !entity.isVulnerable() && !entity.isDeadOrDying());
   }

   private static TargetingConditions protectPredicate(double radius) {
      return TargetingConditions.forNonCombat().range(radius).selector(entity -> entity instanceof Player);
   }

   public float getStepHeight() {
      return 1.0F;
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(BOSSFIGHT_STAGE, WitheredSymbiontEntity.BossfightStage.ATTACKING);
      this.entityData.define(SPELL_TYPE, (SpellType)WitherStormModSymbiontSpellTypes.EMPTY.get());
      this.entityData.define(NON_BOSS_MODE, false);
      this.entityData.define(RUSH_MODE, false);
      this.entityData.define(SHOULD_NOT_GO_OVER_HALF, true);
   }

   protected void registerGoals() {
      this.bossFightGoals = new ArrayList<>();
      this.attackGoal = new MeleeAttackGoal(this, 1.0, true);
      this.prepareSpellGoal = new PrepareSpellGoal(this);
      this.useSpellGoal = new UseSpellGoal(this);
      this.summonMobsGoal = new SummonMobsGoal(this, SYMBIONT_NORMAL_MOBS, SYMBIONT_HARDER_MOBS);
      this.doNothingGoal = new WitheredSymbiontEntity.DoNothingGoal(this);
      this.bossFightGoals.add(this.attackGoal);
      this.bossFightGoals.add(this.prepareSpellGoal);
      this.bossFightGoals.add(this.useSpellGoal);
      this.bossFightGoals.add(this.summonMobsGoal);
      this.bossFightGoals.add(this.doNothingGoal);
      this.goalSelector.addGoal(1, this.prepareSpellGoal);
      this.goalSelector.addGoal(2, this.useSpellGoal);
      this.goalSelector.addGoal(3, this.attackGoal);
      this.goalSelector.addGoal(4, new FloatGoal(this));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.7F));
      this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
      this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
      this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, TARGET_PREDICATE));
      if ((Boolean)WitherStormModConfig.SERVER.shouldSymbiontAttackMobs.get()) {
         this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, MOB_TARGET_PREDICATE));
      }
   }

   public void addAdditionalSaveData(@NotNull CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("IsNonBossMode", this.isNonBossMode());
      compound.putBoolean("IsRushMode", this.isRushMode());
      compound.putInt("Stage", this.getStage().ordinal());
      compound.putInt("StageTicks", this.getStageTicks());
      compound.putString("Spell", Objects.requireNonNull(WitherStormModRegistries.SPELL_TYPES.get().getKey(this.getSpell()), "Unregistered spell").toString());
      compound.putInt("SpellCastingTicks", this.spellCastingTime);
      compound.putInt("NextSpellPick", this.nextSpellPickCount);
      compound.putBoolean("Smashing", this.isSmashing());
      compound.putInt("SmashAirTime", this.smashAirTime);
      compound.putInt("SpellsUsed", this.getSpellsUsed());
      if (this.summoner != null) {
         compound.putUUID("Summoner", this.summoner);
      }

      compound.putInt("AttackDelay", this.attackDelay);
      if (!this.dropItems.isEmpty()) {
         ListTag dropItems = new ListTag();

         for (ItemStack stack : this.dropItems) {
            if (!stack.isEmpty()) {
               CompoundTag tag = new CompoundTag();
               stack.save(tag);
               dropItems.add(tag);
            }
         }

         compound.put("DropItems", dropItems);
      }

      compound.putBoolean("ShouldNotGoOverHalf", (Boolean)this.entityData.get(SHOULD_NOT_GO_OVER_HALF));
      ListTag fightContributors = new ListTag();

      for (UUID id : this.fightContributors) {
         fightContributors.add(NbtUtils.createUUID(id));
      }

      compound.put("FightContributors", fightContributors);
   }

   public void readAdditionalSaveData(@NotNull CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      if (compound.contains("IsNonBossMode")) {
         this.setNonBossMode(compound.getBoolean("IsNonBossMode"));
      }

      if (compound.contains("IsRushMode")) {
         this.setRushMode(compound.getBoolean("IsRushMode"));
      }

      if (compound.contains("Stage")) {
         int ordinal = compound.getInt("Stage");
         if (ordinal >= 0 && ordinal < WitheredSymbiontEntity.BossfightStage.values().length) {
            this.setStage(WitheredSymbiontEntity.BossfightStage.values()[ordinal]);
         }
      }

      this.setStageTicks(compound.getInt("StageTicks"));
      if (compound.contains("Spell", 8)) {
         String rawId = compound.getString("Spell");
         ResourceLocation loc = ResourceLocation.tryParse(rawId);
         if (loc != null) {
            SpellType type = (SpellType)WitherStormModRegistries.SPELL_TYPES.get().getValue(loc);
            if (type != null) {
               this.setSpell(type);
            }
         }
      }

      this.spellCastingTime = compound.getInt("SpellCastingTicks");
      this.nextSpellPickCount = compound.getInt("NextSpellPick");
      this.setSmashing(compound.getBoolean("Smashing"));
      this.smashAirTime = compound.getInt("SmashAirTime");
      this.spellsUsed = compound.getInt("SpellsUsed");
      if (compound.contains("Summoner")) {
         this.summoner = compound.getUUID("Summoner");
      }

      this.attackDelay = compound.getInt("AttackDelay");
      if (compound.contains("DropItems")) {
         ListTag dropItems = compound.getList("DropItems", 10);

         for (int i = 0; i < dropItems.size(); i++) {
            this.dropItems.add(ItemStack.of(dropItems.getCompound(i)));
         }
      }

      if (compound.contains("ShouldNotGoOverHalf")) {
         this.entityData.set(SHOULD_NOT_GO_OVER_HALF, compound.getBoolean("ShouldNotGoOverHalf"));
      }

      this.fightContributors.clear();

      for (Tag tag : compound.getList("FightContributors", 11)) {
         this.fightContributors.add(NbtUtils.loadUUID(tag));
      }
   }

   public static Builder createAttributes() {
      return Monster.createMonsterAttributes()
         .add(Attributes.MAX_HEALTH, 60.0)
         .add(Attributes.MOVEMENT_SPEED, 0.15)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
         .add(Attributes.ATTACK_DAMAGE, 16.0)
         .add(Attributes.FOLLOW_RANGE, 45.0);
   }

   protected int decreaseAirSupply(int supply) {
      return supply;
   }

   protected void doPush(@NotNull Entity entity) {
      if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)
         && entity instanceof LivingEntity
         && TARGET_PREDICATE.test((LivingEntity)entity)
         && this.getRandom().nextInt(20) == 0) {
         this.setTarget((LivingEntity)entity);
      }

      super.doPush(entity);
   }

   public void aiStep() {
      super.aiStep();
      this.stageTicks++;
      if (this.spellCastingTime > 0) {
         this.spellCastingTime--;
         this.doSpellCasting();
         if (this.spellCastingTime <= 0) {
            this.castSpell();
         }

         SpellType type = this.getSpell();
         if (type.doProtection()) {
            double radius = type.protectionRadius();

            for (Player player : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(radius))) {
               if (!this.entitiesToThrow.contains(player) && protectPredicate(radius).test(this, player)) {
                  this.entitiesToThrow.add(player);
                  if (!this.level().isClientSide) {
                     this.playSound((SoundEvent)WitherStormModSoundEvents.WITHERED_SYMBIONT_LAUNCH_MOB.get(), 16.0F, 1.0F);
                  }
               }
            }
         }

         for (int i = 0; i < this.entitiesToThrow.size(); i++) {
            LivingEntity entity = this.entitiesToThrow.get(i);
            if (protectPredicate(type.protectionRadius()).test(this, entity)) {
               Vec3 delta = this.position().subtract(entity.position()).normalize().add(0.0, -0.5, 0.0).scale(-type.protectionThrowStrength());
               entity.setDeltaMovement(delta);
            } else {
               this.entitiesToThrow.remove(i);
            }
         }
      }

      if (this.nextSpellPickCount > 0) {
         this.nextSpellPickCount--;
      }

      if (this.isSmashing()) {
         if (this.smashAirTime > 0) {
            this.smashAirTime--;
            if (this.smashAirTime <= 0) {
               this.setDeltaMovement(this.getDeltaMovement().x(), -5.0, this.getDeltaMovement().z());
            }
         } else if (this.onGround()) {
            this.setSmashing(false);
            if (!this.level().isClientSide()) {
               float strength = 1.5F;
               if (this.shouldIncreaseDifficulty()) {
                  strength = 2.5F;
               }

               this.level().explode(this, this.getX(), this.getY(), this.getZ(), strength, ExplosionInteraction.MOB);
            }
         }
      }

      if (this.isCastingSpell() || this.isSummoningMobs()) {
         for (int i = 0; i < 5; i++) {
            double x = this.getX() + this.random.nextGaussian() * 2.0;
            double y = this.getEyeY() + this.random.nextGaussian() * 2.0;
            double z = this.getZ() + this.random.nextGaussian() * 2.0;
            Vec3 delta = this.getEyePosition(1.0F).subtract(x, y, z).normalize().multiply(0.2, 0.2, 0.2);
            this.level().addParticle((ParticleOptions)WitherStormModParticleTypes.COMMAND_BLOCK.get(), x, y, z, delta.x(), delta.y(), delta.z());
         }
      }

      if (this.getDeltaMovement().horizontalDistanceSqr() > 2.5000003E-7F && this.random.nextInt(5) == 0) {
         int i = Mth.floor(this.getX());
         int j = Mth.floor(this.getY() - 0.2F);
         int k = Mth.floor(this.getZ());
         BlockPos pos = new BlockPos(i, j, k);
         BlockState state = this.level().getBlockState(pos);
         if (!state.is(Blocks.AIR)) {
            this.level()
               .addParticle(
                  new BlockParticleOption(ParticleTypes.BLOCK, state).setPos(pos),
                  this.getX() + (this.random.nextFloat() - 0.5) * this.getBbWidth(),
                  this.getY() + 0.1,
                  this.getZ() + (this.random.nextFloat() - 0.5) * this.getBbWidth(),
                  4.0 * (this.random.nextFloat() - 0.5),
                  0.5,
                  (this.random.nextFloat() - 0.5) * 4.0
               );
         }
      }

      if (!this.level().isClientSide() && this.getStage().shouldMoveToNextStage(this)) {
         this.nextStage();
      }

      if (this.attackDelay > 0) {
         this.attackDelay--;
         if (this.attackDelay <= 0 && this.isVulnerable()) {
            this.setStage(WitheredSymbiontEntity.BossfightStage.ATTACKING);
         }
      }
   }

   public void tick() {
      super.tick();
      this.crouchAnimO = this.crouchAnim;
      if (this.isVulnerable()) {
         this.crouchAnim = this.crouchAnim + ((1.0F - this.crouchAnim) * 0.1F + 0.02F);
         if (this.crouchAnim > 0.6F) {
            this.crouchAnim = 0.6F;
         }
      } else {
         this.crouchAnim = this.crouchAnim + (-this.crouchAnim * 0.4F - 0.1F);
         if (this.crouchAnim < 0.0F) {
            this.crouchAnim = 0.0F;
         }
      }

      this.tearAlphaO = this.tearAlpha;
      if (!(Boolean)WitherStormModConfig.SERVER.attackableWhenNotVulnerable.get() && !this.isVulnerable()) {
         if (this.tearAlpha > 0.0F) {
            this.tearAlpha -= 0.05F;
         }
      } else if (this.tearAlpha < 1.0F) {
         this.tearAlpha += 0.05F;
      }

      if (!this.level().isClientSide()) {
         this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
      }
   }

   public boolean doHurtTarget(Entity entity) {
      float f = this.getAttackDamage();
      float f1 = (int)f > 0 ? f / 2.0F + this.random.nextInt((int)f) : f;
      boolean flag = entity.hurt(this.damageSources().mobAttack(this), f1);
      if (flag) {
         entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, 0.8, 0.0));
         this.doEnchantDamageEffects(this, entity);
      }

      return flag;
   }

   private float getAttackDamage() {
      return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
   }

   protected SoundEvent getAmbientSound() {
      return this.isVulnerable() ? null : (SoundEvent)WitherStormModSoundEvents.WITHERED_SYMBIONT_AMBIENT.get();
   }

   public boolean killedEntity(ServerLevel level, LivingEntity entity) {
      return entity instanceof Mob mob && WorldTainting.getInstance().convertMob(mob, false);
   }

   protected SoundEvent getHurtSound(@NotNull DamageSource source) {
      return (SoundEvent)WitherStormModSoundEvents.WITHERED_SYMBIONT_HURT.get();
   }

   protected SoundEvent getDeathSound() {
      return this.isNonBossMode()
         ? (SoundEvent)WitherStormModSoundEvents.WITHERED_SYMBIONT_NORMAL_DEATH.get()
         : (SoundEvent)WitherStormModSoundEvents.WITHERED_SYMBIONT_DEATH.get();
   }

   public float getVoicePitch() {
      return this.isDeadOrDying() ? 1.0F : super.getVoicePitch();
   }

   protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
      this.playSound((SoundEvent)WitherStormModSoundEvents.WITHERED_SYMBIONT_STEP.get(), 0.3F, 1.0F);
   }

   public boolean canBeLeashed(@NotNull Player player) {
      return false;
   }

   public void checkDespawn() {
      if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
         this.discard();
      } else if (!this.isPersistenceRequired() && !this.requiresCustomPersistence()) {
         List<WitherStormEntity> entities = WorldUtil.getPerformantEntitiesOfClass(
            (ServerLevel)this.level(), WitherStormEntity.class, this.getBoundingBox().inflate(400.0)
         );
         if (entities.isEmpty()) {
            super.checkDespawn();
         } else {
            this.noActionTime = 0;
         }
      } else {
         this.noActionTime = 0;
      }
   }

   public boolean causeFallDamage(float p_225503_1_, float p_225503_2_, @NotNull DamageSource source) {
      return false;
   }

   @NotNull
   public MobType getMobType() {
      return WitherStormModMobTypes.SICKENED;
   }

   @NotNull
   public AABB getBoundingBoxForCulling() {
      return super.getBoundingBoxForCulling().inflate(3.0);
   }

   public boolean hurt(DamageSource source, float amount) {
      if (source.getDirectEntity() instanceof AbstractHurtingProjectile || source.getDirectEntity() instanceof AbstractArrow) {
         Projectile projectile = (Projectile)source.getDirectEntity();
         if (projectile.getOwner() instanceof WitherSickened || projectile.getOwner() instanceof WitheredSymbiontEntity) {
            return false;
         }
      }

      if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
         return super.hurt(source, amount);
      }

      if ((this.isCastingSpell() || !(Boolean)WitherStormModConfig.SERVER.attackableWhenNotVulnerable.get()) && !this.isVulnerable()) {
         return false;
      }

      Entity entity = source.getEntity();
      if (entity != null) {
         double angle = Math.atan2(entity.getX() - this.getX(), entity.getZ() - this.getZ()) * (180.0 / Math.PI);
         double angleDiff = (-this.yBodyRot - angle + 180.0 + 360.0) % 360.0;
         if (angleDiff <= 40.0 || angleDiff >= 320.0) {
            if (this.isVulnerable() && this.attackDelay <= 0) {
               this.attackDelay = 20;
            }

            if (!this.isVulnerable() && entity instanceof LivingEntity && !this.isDeadOrDying()) {
               ((SpellType)WitherStormModSymbiontSpellTypes.SMASH.get()).makeSpell(this).cast((LivingEntity)entity);
            }

            if (source.is(DamageTypeTags.IS_EXPLOSION)) {
               amount /= 4.0F;
            }

            if (this.shouldNotGoOverHalfHealth()) {
               float predictedHealth = this.getHealth() - amount;
               float maxHealthHalf = this.getMaxHealth() / 2.0F;
               amount = Math.min(amount - (maxHealthHalf - predictedHealth), amount);
            }

            float healthCurrent = this.getHealth();
            boolean flag = super.hurt(source, amount);
            float damageDealt = healthCurrent - this.getHealth();
            if (damageDealt >= 5.0F && entity instanceof Player && !this.fightContributors.contains(entity.getUUID())) {
               this.fightContributors.add(entity.getUUID());
            }

            return flag;
         }
      }

      return false;
   }

   public int getStageTicks() {
      return this.stageTicks;
   }

   public void setStageTicks(int ticks) {
      this.stageTicks = ticks;
   }

   public WitheredSymbiontEntity.BossfightStage getStage() {
      return (WitheredSymbiontEntity.BossfightStage)this.entityData.get(BOSSFIGHT_STAGE);
   }

   protected void clearBossFightGoals() {
      this.bossFightGoals.forEach(this.goalSelector::removeGoal);
   }

   protected void addBossFightGoal(int level, Goal goal) {
      this.goalSelector.addGoal(level, goal);
   }

   public void setStage(WitheredSymbiontEntity.BossfightStage stage) {
      if (this.getStage() != stage) {
         this.getStage().finish(this);
      }

      this.entityData.set(BOSSFIGHT_STAGE, stage);
      this.getStage().init(this);
   }

   public void nextStage() {
      WitheredSymbiontEntity.BossfightStage nextStage = this.getNextStage(1);
      if (nextStage == WitheredSymbiontEntity.BossfightStage.SUMMONING && this.isNonBossMode()) {
         this.setStage(this.getNextStage(2));
      } else {
         this.setStage(nextStage);
      }
   }

   private WitheredSymbiontEntity.BossfightStage getNextStage(int advance) {
      int next = this.getStage().ordinal() + advance;
      return next < WitheredSymbiontEntity.BossfightStage.values().length
         ? WitheredSymbiontEntity.BossfightStage.values()[next]
         : WitheredSymbiontEntity.BossfightStage.values()[0];
   }

   public SpellType getSpell() {
      return (SpellType)this.entityData.get(SPELL_TYPE);
   }

   public void setSpell(SpellType spell) {
      this.entityData.set(SPELL_TYPE, spell);
      if (!this.level().isClientSide()) {
         this.spellInstance = spell.makeSpell(this);
      }
   }

   @Nullable
   public SymbiontSpell getSpellInstance() {
      return this.spellInstance;
   }

   public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> parameter) {
      super.onSyncedDataUpdated(parameter);
      if (BOSSFIGHT_STAGE.equals(parameter)) {
         this.getStage().init(this);
      }
   }

   public boolean hasSpell() {
      return this.getSpell() != WitherStormModSymbiontSpellTypes.EMPTY.get();
   }

   public void beginSpellCasting() {
      if (!this.level().isClientSide() && this.spellInstance != null) {
         this.spellInstance.start(this.getTarget());
         this.spellCastingTime = this.getSpell().spellTime();
         WitherStormModPacketHandlers.MAIN
            .send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new WitheredSymbiontEntity.SetSpellTimeMessage(this.getId(), this.spellCastingTime));
      }
   }

   public boolean isCastingSpell() {
      return this.spellCastingTime > 0;
   }

   public boolean isSummoningMobs() {
      return this.getStage() == WitheredSymbiontEntity.BossfightStage.SUMMONING;
   }

   public boolean isVulnerable() {
      return this.getStage() == WitheredSymbiontEntity.BossfightStage.VULNERABLE;
   }

   public void breakSpell() {
      if (this.isCastingSpell()) {
         this.spellCastingTime = 0;
         if (!this.level().isClientSide() && this.spellInstance != null) {
            this.spellInstance.finish();
         }

         this.level().broadcastEntityEvent(this, (byte)11);
      }
   }

   public void handleEntityEvent(byte event) {
      if (event == 11) {
         this.breakSpell();
      } else if (event == 12) {
         this.activateAttackDelay();
      } else {
         super.handleEntityEvent(event);
      }
   }

   public void castSpell() {
      if (!this.level().isClientSide) {
         if (this.spellInstance != null) {
            if (this.getTarget() != null) {
               this.spellInstance.cast(this.getTarget());
            }

            this.spellInstance.finish();
         }
      } else {
         for (int i = 0; i < 10; i++) {
            this.level()
               .addParticle(
                  (ParticleOptions)WitherStormModParticleTypes.COMMAND_BLOCK.get(),
                  this.getX(),
                  this.getEyeY(),
                  this.getZ(),
                  this.random.nextGaussian() * 0.5,
                  this.random.nextGaussian() * 0.5,
                  this.random.nextGaussian() * 0.5
               );
         }
      }
   }

   public void doSpellCasting() {
      if (!this.level().isClientSide() && this.spellInstance != null) {
         int spellCastingTime = this.getSpell().spellTime() - this.spellCastingTime;
         if (this.getTarget() != null && this.getTarget().isAlive()) {
            this.spellInstance.doCasting(this.getTarget());
         } else if (spellCastingTime % 20 == 0) {
            this.breakSpell();
         }
      }
   }

   public boolean canPickSpell() {
      return !this.hasSpell() || this.nextSpellPickCount <= 0;
   }

   public void setAndCastSpell(SpellType type) {
      if (type != WitherStormModSymbiontSpellTypes.EMPTY.get() && !this.isVulnerable()) {
         this.nextSpellPickCount = 0;
         this.setSpell(type);
         this.useSpellGoal.nextAttackTickCount = this.tickCount + 1;
         this.playSound((SoundEvent)WitherStormModSoundEvents.WITHERED_SYMBIONT_PREPARE_SPELL.get(), 4.0F, 1.0F);
         this.nextSpellPickCount = 400 + this.getRandom().nextInt(400) - (this.shouldIncreaseDifficulty() ? 320 : 0);
         if (this.shouldNotGoOverHalfHealth() && this.getHealth() / this.getMaxHealth() <= 0.5F) {
            this.entityData.set(SHOULD_NOT_GO_OVER_HALF, false);
         }
      }
   }

   public void setSmashing(boolean flag) {
      this.isDoingSmash = flag;
      if (flag) {
         this.smashAirTime = 20;
      }
   }

   public boolean isSmashing() {
      return this.isDoingSmash;
   }

   public int getSpellsUsed() {
      return this.spellsUsed;
   }

   public void spellUsed() {
      this.spellsUsed++;
   }

   public float getVulnerableAnim(float partialTicks) {
      return Mth.lerp(partialTicks, this.crouchAnimO, this.crouchAnim);
   }

   public void startSeenByPlayer(@NotNull ServerPlayer player) {
      super.startSeenByPlayer(player);
      this.bossInfo.addPlayer(player);
   }

   public void stopSeenByPlayer(@NotNull ServerPlayer player) {
      super.stopSeenByPlayer(player);
      this.bossInfo.removePlayer(player);
   }

   public boolean shouldIncreaseDifficulty() {
      return this.isRushMode() || this.getHealth() / this.getMaxHealth() <= 0.5F;
   }

   public boolean shouldNotGoOverHalfHealth() {
      return !this.isNonBossMode() && (Boolean)this.entityData.get(SHOULD_NOT_GO_OVER_HALF);
   }

   protected void tickDeath() {
      if (this.isNonBossMode()) {
         super.tickDeath();
      } else {
         int totalTime = 320;
         this.specialDeathTime++;

         for (int i = 0; i < (totalTime - this.specialDeathTime) / 40; i++) {
            this.level()
               .addParticle(
                  ParticleTypes.LARGE_SMOKE,
                  this.getRandomX(1.0),
                  this.getRandomY(),
                  this.getRandomZ(1.0),
                  this.random.nextGaussian() * 0.02,
                  this.random.nextGaussian() * 0.02,
                  this.random.nextGaussian() * 0.02
               );
         }

         float speed = 3.0F;
         float f = Mth.degreesDifference(this.getXRot(), -50.0F);
         float f1 = Mth.clamp(f, -speed, speed);
         this.setXRot(this.getXRot() + f1);
         if (this.specialDeathTime == totalTime) {
            this.remove(RemovalReason.KILLED);
            if (!this.level().isClientSide() && this.dropItems != null) {
               List<Player> players = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(20.0), EntitySelector.NO_SPECTATORS);
               if (players.size() > 1 && !this.fightContributors.isEmpty()) {
                  for (UUID id : this.fightContributors) {
                     players.stream().filter(p -> p.getUUID().equals(id)).findFirst().ifPresent(player -> {
                        for (ItemStack stack : this.dropItems) {
                           if (!stack.isEmpty()) {
                              ItemStack copy = stack.copy();
                              if (!player.getInventory().add(copy)) {
                                 ItemEntity entity = this.spawnAtLocation(copy);
                                 assert entity != null;
                                 entity.moveTo(player.position());
                                 entity.setTarget(id);
                              }
                           }
                        }
                     });
                  }
               } else {
                  this.dropDrops();
               }
            }

            for (int i = 0; i < 20; i++) {
               this.level()
                  .addParticle(
                     ParticleTypes.POOF,
                     this.getRandomX(1.0),
                     this.getRandomY(),
                     this.getRandomZ(1.0),
                     this.random.nextGaussian() * 0.02,
                     this.random.nextGaussian() * 0.02,
                     this.random.nextGaussian() * 0.02
                  );
            }
         }
      }
   }

   private void dropDrops() {
      for (ItemStack stack : this.dropItems) {
         if (!stack.isEmpty()) {
            ItemEntity entity = this.spawnAtLocation(stack, 8.0F);
            assert entity != null;
            entity.setDeltaMovement(0.0, -0.08, 0.0);
            entity.setNoGravity(true);
         }
      }

      this.dropItems.clear();
   }

   public void die(@NotNull DamageSource source) {
      super.die(source);
      if (this.getKillCredit() instanceof Player player) {
         player.getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA).ifPresent(data -> {
            WitherStormEntity owner = this.getOwner();
            if (owner != null) {
               data.markKilledSymbiont(owner);
            }
         });
      }

      for (Player player : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(20.0))) {
         player.getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA)
            .ifPresent(
               data -> data.makeInvulnerable(
                  Mth.clamp((Integer)WitherStormModConfig.SERVER.playerInvulnerableTime.get(), 1, 10) * 1200 + player.getRandom().nextInt(1200)
               )
            );
      }
   }

   public void setOwner(WitherStormEntity entity) {
      this.summoner = entity.getUUID();
   }

   @Nullable
   public WitherStormEntity getOwner() {
      if (!this.level().isClientSide()) {
         ServerLevel world = (ServerLevel)this.level();

         for (Entity entity : world.getAllEntities()) {
            if (entity.getUUID().equals(this.summoner) && entity instanceof WitherStormEntity) {
               return (WitherStormEntity)entity;
            }
         }
      }

      return null;
   }

   @Override
   public SoundEvent getBossTheme() {
      return !this.shouldIncreaseDifficulty()
         ? (SoundEvent)WitherStormModSoundEvents.WITHERED_SYMBIONT_THEME.get()
         : (SoundEvent)WitherStormModSoundEvents.WITHERED_SYMBIONT_INTENSE_THEME.get();
   }

   @Override
   public boolean isStillAlive() {
      return this.isAlive();
   }

   @Override
   public Vec3 getPosition() {
      return this.position();
   }

   @Override
   public double distanceToPlay() {
      return 45.0;
   }

   @Override
   public int priority() {
      return 2;
   }

   @Override
   public int getFadeTime() {
      return 120;
   }

   @Override
   public boolean checkConfig() {
      return (Boolean)WitherStormModConfig.CLIENT.playSymbiontTheme.get();
   }

   public void activateAttackDelay() {
      this.attackDelay = 20;
      if (!this.level().isClientSide()) {
         this.level().broadcastEntityEvent(this, (byte)12);
      }
   }

   public boolean hasAttackDelay() {
      return this.attackDelay > 0;
   }

   public boolean isNonBossMode() {
      return (Boolean)this.entityData.get(NON_BOSS_MODE);
   }

   public void setNonBossMode(boolean mode) {
      this.entityData.set(NON_BOSS_MODE, mode);
      if (mode) {
         this.xpReward = 25;
      } else {
         this.xpReward = 150;
      }
   }

   public boolean isRushMode() {
      return (Boolean)this.entityData.get(RUSH_MODE);
   }

   public void setRushMode(boolean mode) {
      this.entityData.set(RUSH_MODE, mode);
   }

   protected void dropFromLootTable(@NotNull DamageSource source, boolean player) {
      ResourceLocation id = this.getLootTable();
      LootTable table = this.level().getServer().getLootData().getLootTable(id);
      net.minecraft.world.level.storage.loot.LootParams.Builder builder = new net.minecraft.world.level.storage.loot.LootParams.Builder(
            (ServerLevel)this.level()
         )
         .withParameter(LootContextParams.THIS_ENTITY, this)
         .withParameter(LootContextParams.ORIGIN, this.position())
         .withParameter(LootContextParams.DAMAGE_SOURCE, source)
         .withOptionalParameter(LootContextParams.KILLER_ENTITY, source.getEntity())
         .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, source.getDirectEntity());
      if (player && this.lastHurtByPlayer != null) {
         builder = builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer).withLuck(this.lastHurtByPlayer.getLuck());
      }

      LootParams params = builder.create(LootContextParamSets.ENTITY);
      this.dropItems = table.getRandomItems(params, this.getLootTableSeed());
   }

   protected boolean canRide(@NotNull Entity entity) {
      return super.canRide(entity) && !(entity instanceof Boat) && !(entity instanceof AbstractMinecart);
   }

   @Override
   public Component getWatermark() {
      return Component.translatable("witherstormmod.watermark.withered_symbiont_theme");
   }

   public float getTearAlpha(float partialTicks) {
      return Mth.lerp(partialTicks, this.tearAlphaO, this.tearAlpha);
   }

   public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, SpawnGroupData groupData, CompoundTag tag
   ) {
      List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(150.0), e -> e.isAlive() && !e.isSpectator());
      if (nearbyPlayers.size() > 1) {
         double healthAddition = nearbyPlayers.size() * (Double)WitherStormModConfig.SERVER.healthScalePerPlayer.get();
         if (healthAddition > 0.0) {
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH))
               .addPermanentModifier(new AttributeModifier("Health scaling", healthAddition, Operation.ADDITION));
            this.setHealth(this.getMaxHealth());
         }
      }

      return super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);
   }

   public LivingEntity getRandomNearbyTargetOrFallback(LivingEntity entity, Predicate<LivingEntity> selector) {
      List<LivingEntity> entities = this.getNearbyTargets(selector).filter(e -> e != entity).collect(Collectors.toList());
      if (!entities.isEmpty() && this.random.nextInt(entities.size() + 1) != 0) {
         Collections.shuffle(entities);
         return entities.get(0);
      } else {
         return entity;
      }
   }

   public Stream<LivingEntity> getNearbyTargets(Predicate<LivingEntity> selector) {
      double range = this.getAttributeValue(Attributes.FOLLOW_RANGE);
      TargetingConditions conditions = TargetingConditions.forCombat().range(this.getAttributeValue(Attributes.FOLLOW_RANGE)).selector(selector);
      return this.level().getNearbyEntities(LivingEntity.class, conditions, this, this.getBoundingBox().inflate(range)).stream().filter(e -> e != this);
   }

   public int getSpellCastingTime() {
      return this.spellCastingTime;
   }

   public float getJumpPower() {
      return super.getJumpPower();
   }

   public UseSpellGoal getUseSpellGoal() {
      return this.useSpellGoal;
   }

   public void setHalfHealthLimit(boolean flag) {
      this.entityData.set(SHOULD_NOT_GO_OVER_HALF, flag);
   }

   public int getNextSpellPickCount() {
      return this.nextSpellPickCount;
   }

   public void setNextSpellPickCount(int count) {
      this.nextSpellPickCount = count;
   }

   public enum BossfightStage {
      ATTACKING {
         @Override
         public void init(WitheredSymbiontEntity entity) {
            super.init(entity);
            if (!entity.level().isClientSide) {
               entity.spellsUsed = 0;
               entity.clearBossFightGoals();
               entity.addBossFightGoal(1, entity.prepareSpellGoal);
               entity.addBossFightGoal(2, entity.useSpellGoal);
               entity.addBossFightGoal(3, entity.attackGoal);
            }
         }

         @Override
         public void finish(WitheredSymbiontEntity entity) {
            super.finish(entity);
            if (!entity.level().isClientSide) {
               entity.spellsUsed = 0;
               entity.setSpell((SpellType)WitherStormModSymbiontSpellTypes.EMPTY.get());
            }
         }

         @Override
         public boolean shouldMoveToNextStage(WitheredSymbiontEntity entity) {
            return entity.getSpellsUsed() > 5 && !entity.isCastingSpell() && entity.getStageTicks() % 80 == 0 && entity.getTarget() != null;
         }
      },
      SUMMONING {
         @Override
         public void init(WitheredSymbiontEntity entity) {
            super.init(entity);
            if (!entity.level().isClientSide) {
               entity.clearBossFightGoals();
               entity.addBossFightGoal(1, entity.summonMobsGoal);
            }
         }
      },
      VULNERABLE {
         @Override
         public void init(WitheredSymbiontEntity entity) {
            super.init(entity);
            if (!entity.level().isClientSide) {
               entity.clearBossFightGoals();
               entity.addBossFightGoal(1, entity.doNothingGoal);
               entity.playSound((SoundEvent)WitherStormModSoundEvents.WITHERED_SYMBIONT_POWER_DOWN.get(), 4.0F, 1.0F);
            }
         }

         @Override
         public void finish(WitheredSymbiontEntity entity) {
            super.finish(entity);
         }

         @Override
         public boolean shouldMoveToNextStage(WitheredSymbiontEntity entity) {
            return entity.getStageTicks() > 4800;
         }
      };

      public boolean shouldDoNothing() {
         return false;
      }

      public void init(WitheredSymbiontEntity entity) {
         entity.setStageTicks(0);
      }

      public void finish(WitheredSymbiontEntity entity) {
      }

      public boolean shouldMoveToNextStage(WitheredSymbiontEntity entity) {
         return false;
      }
   }

   public static class DoNothingGoal extends Goal {
      protected final WitheredSymbiontEntity entity;

      public DoNothingGoal(WitheredSymbiontEntity entity) {
         this.entity = entity;
         this.setFlags(EnumSet.of(Flag.LOOK, Flag.TARGET, Flag.MOVE, Flag.JUMP));
      }

      public boolean canUse() {
         return this.entity.isVulnerable();
      }

      public void tick() {
         float speed = 3.0F;
         float f = Mth.degreesDifference(this.entity.getXRot(), 55.0F);
         float f1 = Mth.clamp(f, -speed, speed);
         this.entity.setXRot(this.entity.getXRot() + f1);
      }
   }

   public static class SetSpellTimeMessage extends Packet {
      private int id;
      private int time;

      public SetSpellTimeMessage(int id, int time) {
         super(true);
         this.id = id;
         this.time = time;
      }

      public SetSpellTimeMessage() {
         super(false);
      }

      public void decode(FriendlyByteBuf buffer) {
         this.id = buffer.readVarInt();
         this.time = buffer.readInt();
      }

      public void encode(FriendlyByteBuf buffer) {
         buffer.writeVarInt(this.id);
         buffer.writeInt(this.time);
      }

      public Runnable getProcessor(Context context) {
         return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            Optional<Level> optional = (Optional<Level>)LogicalSidedProvider.CLIENTWORLD.get(context.getDirection().getReceptionSide());
            optional.ifPresent(world -> {
               if (world.getEntity(this.id) instanceof WitheredSymbiontEntity symbiont) {
                  symbiont.spellCastingTime = this.time;
               }
            });
         });
      }
   }
}
