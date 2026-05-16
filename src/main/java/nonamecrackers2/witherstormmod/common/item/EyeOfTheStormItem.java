package nonamecrackers2.witherstormmod.common.item;

import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import nonamecrackers2.witherstormmod.common.entity.TentacleSpike;

public class EyeOfTheStormItem extends CommandBlockSwordItem {
   public static final UUID DAMAGE_MODIFIER_ID = UUID.fromString("823350e7-4c91-4a1f-8c01-8735113f066e");
   public static final String ENTITY_HEALTH_RATIO = "EntityHealthRatio";

   public EyeOfTheStormItem(Tier tier, int damage, float attackSpeed, Properties properties) {
      super(tier, damage, attackSpeed, properties);
   }

   public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
      CompoundTag tag = stack.getOrCreateTag();
      if (!(entity instanceof LivingEntity living && (!(living instanceof Player) || !((Player)living).getAbilities().instabuild))) {
         tag.remove("EntityHealthRatio");
      } else {
         tag.putFloat("EntityHealthRatio", living.getHealth() / living.getMaxHealth());
      }
   }

   public void appendHoverText(ItemStack stack, Level level, List<Component> text, TooltipFlag flag) {
      text.add(Component.translatable("item.witherstormmod.eye_of_the_storm.author").withStyle(ChatFormatting.DARK_GRAY));
   }

   public boolean hurtEnemy(ItemStack stack, LivingEntity hit, LivingEntity living) {
      if (!super.hurtEnemy(stack, hit, living)) {
         return false;
      }

      if (living.getRandom().nextFloat() > living.getHealth() / living.getMaxHealth() || living instanceof Player player && player.getAbilities().instabuild) {
         double minHeight = Math.min(hit.getY(), living.getY());
         double maxHeight = Math.max(hit.getY(), living.getY()) + 1.0;
         int total = 5;
         int spread = 2;
         float hitAngle = (float)Mth.atan2(hit.getZ() - living.getZ(), hit.getX() - living.getX());
         float damageModifier = EnchantmentHelper.getDamageBonus(stack, hit.getMobType());
         createSpike(living, hit.getX(), hit.getZ(), minHeight, maxHeight, hitAngle, 0, damageModifier);

         for (int i = 0; i < total; i++) {
            float angle = (float)i / total * (float) Math.PI * 2.0F + hitAngle;

            for (int j = 0; j < spread; j++) {
               double x = hit.getX() + (double)Mth.cos(angle) * (j + 1);
               double z = hit.getZ() + (double)Mth.sin(angle) * (j + 1);
               createSpike(living, x, z, minHeight, maxHeight, angle, (j + 1) * 5 + living.getRandom().nextInt(4) - 2, damageModifier);
            }
         }
      }

      return true;
   }

   private static void createSpike(LivingEntity owner, double x, double z, double minHeight, double maxHeight, float yRot, int delay, float damageModifier) {
      BlockPos blockPos = BlockPos.containing(x, maxHeight, z);
      boolean flag = false;
      double d0 = 0.0;

      do {
         BlockPos below = blockPos.below();
         BlockState state = owner.level().getBlockState(below);
         if (state.isFaceSturdy(owner.level(), below, Direction.UP)) {
            if (!owner.level().isEmptyBlock(blockPos)) {
               BlockState state1 = owner.level().getBlockState(blockPos);
               VoxelShape shape = state1.getCollisionShape(owner.level(), blockPos);
               if (!shape.isEmpty()) {
                  d0 = shape.max(Axis.Y);
               }
            }

            flag = true;
            break;
         }

         blockPos = blockPos.below();
      } while (blockPos.getY() >= Mth.floor(minHeight) - 1);

      if (flag) {
         owner.level().addFreshEntity(new TentacleSpike(owner.level(), x, blockPos.getY() + d0, z, yRot, delay, owner, damageModifier));
      }
   }
}
