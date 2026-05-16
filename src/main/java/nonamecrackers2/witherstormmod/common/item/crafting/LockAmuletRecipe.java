package nonamecrackers2.witherstormmod.common.item.crafting;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers;
import nonamecrackers2.witherstormmod.common.item.AmuletItem;

public class LockAmuletRecipe extends CustomRecipe {
   public LockAmuletRecipe(ResourceLocation id, CraftingBookCategory category) {
      super(id, category);
   }

   public boolean matches(CraftingContainer container, Level level) {
      boolean flag = true;
      int totalAmulets = 0;

      for (int i = 0; i < container.getContainerSize(); i++) {
         ItemStack stack = container.getItem(i);
         if (stack.getItem() instanceof AmuletItem) {
            totalAmulets++;
         }

         if (!stack.isEmpty() && !(stack.getItem() instanceof AmuletItem) || stack.getOrCreateTag().getBoolean("Locked") || totalAmulets > 1) {
            flag = false;
         }
      }

      return flag;
   }

   public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
      for (int i = 0; i < container.getContainerSize(); i++) {
         ItemStack slotItem = container.getItem(i);
         if (slotItem.getItem() instanceof AmuletItem) {
            ItemStack stack = slotItem.copy();
            stack.getOrCreateTag().putBoolean("Locked", true);
            return stack;
         }
      }

      return ItemStack.EMPTY;
   }

   public boolean canCraftInDimensions(int width, int height) {
      return width * height >= 1;
   }

   public RecipeSerializer<?> getSerializer() {
      return (RecipeSerializer<?>)WitherStormModRecipeSerializers.LOCK_AMULET.get();
   }
}
