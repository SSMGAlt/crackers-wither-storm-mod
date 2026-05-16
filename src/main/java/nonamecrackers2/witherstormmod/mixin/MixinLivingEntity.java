package nonamecrackers2.witherstormmod.mixin;

import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.accessor.LivingEntityAccessor;
import nonamecrackers2.witherstormmod.common.entity.WitherSickened;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCriteriaTriggers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModEntityTags;
import nonamecrackers2.witherstormmod.common.util.BrainInjectionHelper;
import nonamecrackers2.witherstormmod.common.util.PhlegmGravestoneHelper;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements LivingEntityAccessor {
   @Unique
   private boolean hasDeathProtection;

   private MixinLivingEntity() {
      super(null, null);
      throw new UnsupportedOperationException();
   }

   @Inject(method = "<init>", at = @At("TAIL"))
   public void constructorTail(EntityType<? extends LivingEntity> type, Level level, CallbackInfo ci) {
      BrainInjectionHelper.inject((LivingEntity)this);
   }

   @Inject(method = "dropAllDeathLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;dropExperience()V"), cancellable = true)
   public void witherstormmod$preventDrops_dropAllDeathLoot(DamageSource source, CallbackInfo ci) {
      MutableBoolean flag = new MutableBoolean();
      PhlegmGravestoneHelper.findPotentialPhlegmClusterPos((LivingEntity)this, source).ifPresent(pos -> {
         List<ItemStack> items = this.captureDrops().stream().<ItemStack>map(ItemEntity::getItem).toList();
         if (!items.isEmpty()) {
            PhlegmGravestoneHelper.spawnForEntity((LivingEntity)this, pos, items);
            this.captureDrops(null);
            flag.setTrue();
         }
      });
      if (flag.getValue()) {
         ci.cancel();
      }
   }

   @Inject(method = "checkTotemDeathProtection", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V"))
   public void witherstormmod$setHasDeathProtection_checkTotemDeathProtection(DamageSource damageSource, CallbackInfoReturnable<Boolean> ci) {
      this.hasDeathProtection = true;
   }

   @Inject(method = "checkTotemDeathProtection", at = @At("TAIL"), cancellable = true)
   public void witherstormmod$evolveWitherStormIfDying_checkTotemDeathProtection(DamageSource damageSource, CallbackInfoReturnable<Boolean> ci) {
      if (this instanceof WitherStormEntity storm && storm.isCompletelyInvulnerable() && storm.getPhase() < 4) {
         float health = storm.getHealth() / storm.getMaxHealth();
         if (health <= 0.1F) {
            storm.evolveToPhase(4);
            storm.setHealth(storm.getMaxHealth());
            ci.setReturnValue(true);
            if (damageSource.getEntity() instanceof ServerPlayer player) {
               WitherStormModCriteriaTriggers.NEARLY_KILL_WITHER_STORM.trigger(player, storm);
            }
         }
      }
   }

   @Inject(method = "removeAllEffects", at = @At("RETURN"))
   public void witherstormmod$resetHasDeathProtection_removeAllEffects(CallbackInfoReturnable<Boolean> ci) {
      if (!this.level().isClientSide) {
         this.hasDeathProtection = false;
      }
   }

   @Inject(method = "canAttack", at = @At("HEAD"), cancellable = true)
   public void witherstormmod$preventCertainMobsFromAttackingSickenedMobs_canAttack(LivingEntity entity, CallbackInfoReturnable<Boolean> ci) {
      if (this instanceof WitherBoss
         && (
            entity.getType().is(WitherStormModEntityTags.SICKENED_MOBS)
               || entity instanceof WitherSickened
               || entity.getMobType() == WitherStormModMobTypes.SICKENED
         )) {
         ci.setReturnValue(false);
      }
   }

   @Override
   public void setHasDeathProtection(boolean flag) {
      this.hasDeathProtection = flag;
   }

   @Override
   public boolean hasDeathProtection() {
      return this.hasDeathProtection;
   }
}
