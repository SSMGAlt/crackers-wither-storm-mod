package nonamecrackers2.witherstormmod.common.item.crafting.builder;

import com.google.gson.JsonObject;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeSerializers;
import nonamecrackers2.witherstormmod.common.item.crafting.SuperBeaconRecipe;

public class ResummonSuperBeaconRecipeBuilder extends SuperBeaconRecipeBuilder {
   private final EntityType<?> entity;
   private final CompoundTag nbt;

   public ResummonSuperBeaconRecipeBuilder(SuperBeaconRecipe.Condition condition, EntityType<?> entity, CompoundTag tag) {
      super(condition);
      this.entity = entity;
      this.nbt = tag;
   }

   public Item getResult() {
      return null;
   }

   public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
      consumer.accept(
         new ResummonSuperBeaconRecipeBuilder.Result(id, this.condition, this.entity, this.nbt, this.group == null ? "" : this.group, this.ingredients)
      );
   }

   public void save(Consumer<FinishedRecipe> consumer) {
      this.save(consumer, defaultRecipeId(this.entity));
   }

   public void save(Consumer<FinishedRecipe> consumer, String string) {
      ResourceLocation id = defaultRecipeId(this.entity);
      ResourceLocation newId = new ResourceLocation(id.getNamespace(), string);
      if (id.equals(newId)) {
         throw new IllegalStateException("Recipe " + string + " should remove its 'save' argument as it is equal to the default one");
      }

      this.save(consumer, newId);
   }

   private static ResourceLocation defaultRecipeId(EntityType<?> type) {
      ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(type);
      return new ResourceLocation(id.getNamespace(), "summon_" + id.getPath());
   }

   public static class Result extends SuperBeaconRecipeBuilder.Result {
      private final EntityType<?> entity;
      private final CompoundTag nbt;

      public Result(
         ResourceLocation id, SuperBeaconRecipe.Condition condition, EntityType<?> entity, CompoundTag nbt, String group, List<Ingredient> ingredients
      ) {
         super(id, condition, group, ingredients);
         this.entity = entity;
         this.nbt = nbt;
      }

      @Override
      public void serializeRecipeData(JsonObject object) {
         super.serializeRecipeData(object);
         object.addProperty("entity", ForgeRegistries.ENTITY_TYPES.getKey(this.entity).toString());
         if (!this.nbt.isEmpty()) {
            object.addProperty("nbt", this.nbt.toString());
         }
      }

      public RecipeSerializer<?> getType() {
         return (RecipeSerializer<?>)WitherStormModRecipeSerializers.RESUMMON_SUPER_BEACON.get();
      }
   }
}
