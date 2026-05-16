package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.FoodProperties.Builder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.SimpleFoiledItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nonamecrackers2.witherstormmod.common.item.AmuletItem;
import nonamecrackers2.witherstormmod.common.item.CommandBlockAxeItem;
import nonamecrackers2.witherstormmod.common.item.CommandBlockHoeItem;
import nonamecrackers2.witherstormmod.common.item.CommandBlockPickaxeItem;
import nonamecrackers2.witherstormmod.common.item.CommandBlockShovelItem;
import nonamecrackers2.witherstormmod.common.item.CommandBlockSwordItem;
import nonamecrackers2.witherstormmod.common.item.EyeOfTheStormItem;
import nonamecrackers2.witherstormmod.common.item.FormidiBladeItem;
import nonamecrackers2.witherstormmod.common.item.FormidibombItem;
import nonamecrackers2.witherstormmod.common.item.GoldenAppleStewItem;
import nonamecrackers2.witherstormmod.common.item.PhasometerItem;
import nonamecrackers2.witherstormmod.common.item.TaintedCarvedPumpkinItem;
import nonamecrackers2.witherstormmod.common.item.WitheredNetherStarItem;
import nonamecrackers2.witherstormmod.common.util.WitherStormModItemTier;

public class WitherStormModItems {
   public static final FoodProperties GOLDEN_APPLE_STEW_FOOD = new Builder()
      .nutrition(5)
      .saturationMod(1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2600, 0), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 0), 1.0F)
      .alwaysEat()
      .build();
   public static final FoodProperties WITHERED_FLESH_FOOD = new Builder()
      .nutrition(4)
      .saturationMod(0.1F)
      .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 800, 0), 0.8F)
      .effect(() -> new MobEffectInstance(MobEffects.WITHER, 400, 0), 1.0F)
      .meat()
      .build();
   public static final FoodProperties WITHERED_SPIDER_EYE_FOOD = new Builder()
      .nutrition(2)
      .saturationMod(0.8F)
      .effect(() -> new MobEffectInstance(MobEffects.POISON, 200, 0), 1.0F)
      .effect(() -> new MobEffectInstance(MobEffects.WITHER, 400, 0), 1.0F)
      .build();
   public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "witherstormmod");
   public static final RegistryObject<Item> WITHERED_BONE = ITEMS.register("withered_bone", () -> new Item(new Properties().rarity(Rarity.UNCOMMON)));
   public static final RegistryObject<Item> WITHERED_FLESH = ITEMS.register(
      "withered_flesh", () -> new Item(new Properties().food(WITHERED_FLESH_FOOD).rarity(Rarity.UNCOMMON))
   );
   public static final RegistryObject<Item> TAINTED_DUST = ITEMS.register(
      "tainted_dust", () -> new ItemNameBlockItem((Block)WitherStormModBlocks.TAINTED_DUST.get(), new Properties().rarity(Rarity.UNCOMMON))
   );
   public static final RegistryObject<Item> WITHERED_SPIDER_EYE = ITEMS.register(
      "withered_spider_eye", () -> new Item(new Properties().rarity(Rarity.UNCOMMON).food(WITHERED_SPIDER_EYE_FOOD))
   );
   public static final RegistryObject<Item> GOLDEN_APPLE_STEW = ITEMS.register(
      "golden_apple_stew", () -> new GoldenAppleStewItem(new Properties().rarity(Rarity.RARE).food(GOLDEN_APPLE_STEW_FOOD).stacksTo(1))
   );
   public static final RegistryObject<Item> AMULET = ITEMS.register(
      "amulet", () -> new AmuletItem(new Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON))
   );
   public static final RegistryObject<Item> COMMAND_BLOCK_BOOK = ITEMS.register(
      "command_block_book", () -> new SimpleFoiledItem(new Properties().rarity(Rarity.RARE).stacksTo(1).fireResistant())
   );
   public static final RegistryObject<Item> WITHERED_NETHER_STAR = ITEMS.register(
      "withered_nether_star", () -> new WitheredNetherStarItem(new Properties().rarity(Rarity.EPIC).fireResistant())
   );
   public static final RegistryObject<Item> SICKENED_CREEPER_SPAWN_EGG = ITEMS.register(
      "sickened_creeper_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_CREEPER, 9851315, 3278099, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_SKELETON_SPAWN_EGG = ITEMS.register(
      "sickened_skeleton_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_SKELETON, 13606575, 3612758, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_SPIDER_SPAWN_EGG = ITEMS.register(
      "sickened_spider_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_SPIDER, 2827051, 16056399, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_VILLAGER_SPAWN_EGG = ITEMS.register(
      "sickened_villager_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_VILLAGER, 8551284, 11305627, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_ZOMBIE_SPAWN_EGG = ITEMS.register(
      "sickened_zombie_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_ZOMBIE, 4808027, 10648470, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_PHANTOM_SPAWN_EGG = ITEMS.register(
      "sickened_phantom_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_PHANTOM, 6967167, 16713046, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_CHICKEN_SPAWN_EGG = ITEMS.register(
      "sickened_chicken_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_CHICKEN, 5977232, 5570648, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_PARROT_SPAWN_EGG = ITEMS.register(
      "sickened_parrot_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_PARROT, 7032441, 5911693, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_WOLF_SPAWN_EGG = ITEMS.register(
      "sickened_wolf_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_WOLF, 4866401, 7960207, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_CAT_SPAWN_EGG = ITEMS.register(
      "sickened_cat_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_CAT, 1775149, 9595267, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_COW_SPAWN_EGG = ITEMS.register(
      "sickened_cow_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_COW, 3613496, 10066329, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_PIG_SPAWN_EGG = ITEMS.register(
      "sickened_pig_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_PIG, 6706811, 5786734, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_MUSHROOM_COW_SPAWN_EGG = ITEMS.register(
      "sickened_mushroom_cow_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_MUSHROOM_COW, 8200599, 11887564, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_BEE_SPAWN_EGG = ITEMS.register(
      "sickened_bee_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_BEE, 10711155, 3023140, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_PILLAGER_SPAWN_EGG = ITEMS.register(
      "sickened_pillager_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_PILLAGER, 4403259, 10190758, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_VINDICATOR_SPAWN_EGG = ITEMS.register(
      "sickened_vindicator_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_VINDICATOR, 10190758, 3422273, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_IRON_GOLEM_SPAWN_EGG = ITEMS.register(
      "sickened_iron_golem_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_IRON_GOLEM, 13541842, 15270143, new Properties())
   );
   public static final RegistryObject<Item> SICKENED_SNOW_GOLEM_SPAWN_EGG = ITEMS.register(
      "sickened_snow_golem_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.SICKENED_SNOW_GOLEM, 15589887, 12754175, new Properties())
   );
   public static final RegistryObject<Item> TENTACLE_SPAWN_EGG = ITEMS.register(
      "tentacle_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.TENTACLE, 722193, 1379103, new Properties())
   );
   public static final RegistryObject<Item> WITHERED_SYMBIONT_SPAWN_EGG = ITEMS.register(
      "withered_symbiont_spawn_egg", () -> new ForgeSpawnEggItem(WitherStormModEntityTypes.WITHERED_SYMBIONT, 2233397, 16056568, new Properties())
   );
   public static final RegistryObject<Item> COMMAND_BLOCK_SWORD = ITEMS.register(
      "command_block_sword",
      () -> new CommandBlockSwordItem(WitherStormModItemTier.COMMAND_BLOCK, 3, -2.4F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> COMMAND_BLOCK_PICKAXE = ITEMS.register(
      "command_block_pickaxe",
      () -> new CommandBlockPickaxeItem(WitherStormModItemTier.COMMAND_BLOCK, 1, -2.8F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> COMMAND_BLOCK_AXE = ITEMS.register(
      "command_block_axe", () -> new CommandBlockAxeItem(WitherStormModItemTier.COMMAND_BLOCK, 5, -3.0F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> COMMAND_BLOCK_SHOVEL = ITEMS.register(
      "command_block_shovel",
      () -> new CommandBlockShovelItem(WitherStormModItemTier.COMMAND_BLOCK, 1.5F, -3.4F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> COMMAND_BLOCK_HOE = ITEMS.register(
      "command_block_hoe", () -> new CommandBlockHoeItem(WitherStormModItemTier.COMMAND_BLOCK, -4, 0.0F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> WOOD_COMMAND_BLOCK_SWORD = ITEMS.register(
      "wooden_command_block_sword",
      () -> new CommandBlockSwordItem(WitherStormModItemTier.WOOD_CMD, 3, -2.4F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> WOOD_COMMAND_BLOCK_PICKAXE = ITEMS.register(
      "wooden_command_block_pickaxe",
      () -> new CommandBlockPickaxeItem(WitherStormModItemTier.WOOD_CMD, 1, -2.8F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> WOOD_COMMAND_BLOCK_AXE = ITEMS.register(
      "wooden_command_block_axe",
      () -> new CommandBlockAxeItem(WitherStormModItemTier.WOOD_CMD, 6, -3.2F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> WOOD_COMMAND_BLOCK_SHOVEL = ITEMS.register(
      "wooden_command_block_shovel",
      () -> new CommandBlockShovelItem(WitherStormModItemTier.WOOD_CMD, 1.5F, -3.0F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> WOOD_COMMAND_BLOCK_HOE = ITEMS.register(
      "wooden_command_block_hoe",
      () -> new CommandBlockHoeItem(WitherStormModItemTier.WOOD_CMD, -4, 3.0F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> STONE_COMMAND_BLOCK_SWORD = ITEMS.register(
      "stone_command_block_sword",
      () -> new CommandBlockSwordItem(WitherStormModItemTier.STONE_CMD, 3, -2.4F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> STONE_COMMAND_BLOCK_PICKAXE = ITEMS.register(
      "stone_command_block_pickaxe",
      () -> new CommandBlockPickaxeItem(WitherStormModItemTier.STONE_CMD, 1, -2.8F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> STONE_COMMAND_BLOCK_AXE = ITEMS.register(
      "stone_command_block_axe",
      () -> new CommandBlockAxeItem(WitherStormModItemTier.STONE_CMD, 6, -3.2F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> STONE_COMMAND_BLOCK_SHOVEL = ITEMS.register(
      "stone_command_block_shovel",
      () -> new CommandBlockShovelItem(WitherStormModItemTier.STONE_CMD, 1.5F, -2.0F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> STONE_COMMAND_BLOCK_HOE = ITEMS.register(
      "stone_command_block_hoe",
      () -> new CommandBlockHoeItem(WitherStormModItemTier.STONE_CMD, -3, -3.0F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> IRON_COMMAND_BLOCK_SWORD = ITEMS.register(
      "iron_command_block_sword",
      () -> new CommandBlockSwordItem(WitherStormModItemTier.IRON_CMD, 4, -2.8F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> IRON_COMMAND_BLOCK_PICKAXE = ITEMS.register(
      "iron_command_block_pickaxe",
      () -> new CommandBlockPickaxeItem(WitherStormModItemTier.IRON_CMD, 3, -3.2F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> IRON_COMMAND_BLOCK_AXE = ITEMS.register(
      "iron_command_block_axe", () -> new CommandBlockAxeItem(WitherStormModItemTier.IRON_CMD, 6, -3.1F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> IRON_COMMAND_BLOCK_SHOVEL = ITEMS.register(
      "iron_command_block_shovel",
      () -> new CommandBlockShovelItem(WitherStormModItemTier.IRON_CMD, 2.5F, -3.0F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> IRON_COMMAND_BLOCK_HOE = ITEMS.register(
      "iron_command_block_hoe", () -> new CommandBlockHoeItem(WitherStormModItemTier.IRON_CMD, 9, -3.5F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> GOLD_COMMAND_BLOCK_SWORD = ITEMS.register(
      "gold_command_block_sword",
      () -> new CommandBlockSwordItem(WitherStormModItemTier.GOLD_CMD, -1, -1.2F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> GOLD_COMMAND_BLOCK_PICKAXE = ITEMS.register(
      "gold_command_block_pickaxe",
      () -> new CommandBlockPickaxeItem(WitherStormModItemTier.GOLD_CMD, 1, -2.8F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> GOLD_COMMAND_BLOCK_AXE = ITEMS.register(
      "gold_command_block_axe", () -> new CommandBlockAxeItem(WitherStormModItemTier.GOLD_CMD, 6, -3.0F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> GOLD_COMMAND_BLOCK_SHOVEL = ITEMS.register(
      "gold_command_block_shovel",
      () -> new CommandBlockShovelItem(WitherStormModItemTier.GOLD_CMD, 1.5F, -3.0F, new Properties().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> GOLD_COMMAND_BLOCK_HOE = ITEMS.register(
      "gold_command_block_hoe",
      () -> new CommandBlockHoeItem(WitherStormModItemTier.GOLD_CMD, 0, -3.0F, new Properties().fireResistant().fireResistant().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> EYE_OF_THE_STORM = ITEMS.register(
      "eye_of_the_storm", () -> new EyeOfTheStormItem(WitherStormModItemTier.EYE_OF_THE_STORM, 3, -2.4F, new Properties().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> FORMIDI_BLADE = ITEMS.register(
      "formidi_blade", () -> new FormidiBladeItem(WitherStormModItemTier.FORMIDI_BLADE, 3, -3.7F, new Properties().rarity(Rarity.EPIC))
   );
   public static final RegistryObject<Item> SUPER_TNT = ITEMS.register(
      "super_tnt", () -> new BlockItem((Block)WitherStormModBlocks.SUPER_TNT.get(), new Properties().rarity(Rarity.RARE))
   );
   public static final RegistryObject<Item> FORMIDIBOMB = ITEMS.register(
      "formidibomb", () -> new FormidibombItem((Block)WitherStormModBlocks.FORMIDIBOMB.get(), new Properties().rarity(Rarity.EPIC).stacksTo(1).fireResistant())
   );
   public static final RegistryObject<Item> SUPER_BEACON = ITEMS.register(
      "super_beacon", () -> new BlockItem((Block)WitherStormModBlocks.SUPER_BEACON.get(), new Properties().rarity(Rarity.EPIC).fireResistant())
   );
   public static final RegistryObject<Item> SUPER_SUPPORT_BEACON = ITEMS.register(
      "super_support_beacon", () -> new BlockItem((Block)WitherStormModBlocks.SUPER_SUPPORT_BEACON.get(), new Properties().rarity(Rarity.RARE).fireResistant())
   );
   public static final RegistryObject<Item> FIREWORK_BUNDLE = ITEMS.register(
      "firework_bundle", () -> new BlockItem((Block)WitherStormModBlocks.FIREWORK_BUNDLE.get(), new Properties())
   );
   public static final RegistryObject<Item> PHASOMETER = ITEMS.register(
      "phasometer", () -> new PhasometerItem(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON))
   );
   public static final RegistryObject<Item> TAINTED_ZOMBIE_SITTING = ITEMS.register(
      "tainted_zombie_sitting", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_ZOMBIE_SITTING.get(), new Properties().rarity(Rarity.UNCOMMON))
   );
   public static final RegistryObject<Item> TAINTED_ZOMBIE_WALL = ITEMS.register(
      "tainted_zombie_wall", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_ZOMBIE_WALL.get(), new Properties().rarity(Rarity.UNCOMMON))
   );
   public static final RegistryObject<Item> TAINTED_ZOMBIE_LYING = ITEMS.register(
      "tainted_zombie_lying", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_ZOMBIE_LYING.get(), new Properties().rarity(Rarity.UNCOMMON))
   );
   public static final RegistryObject<Item> TAINTED_BONE_PILE = ITEMS.register(
      "tainted_bone_pile", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_BONE_PILE.get(), new Properties().rarity(Rarity.UNCOMMON))
   );
   public static final RegistryObject<Item> TAINTED_SKELETON_WALL = ITEMS.register(
      "tainted_skeleton_wall", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SKELETON_WALL.get(), new Properties().rarity(Rarity.UNCOMMON))
   );
   public static final RegistryObject<Item> TAINTED_SKULL_CEILING = ITEMS.register(
      "tainted_skull_ceiling", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SKULL_CEILING.get(), new Properties().rarity(Rarity.UNCOMMON))
   );
   public static final RegistryObject<Item> TAINTED_FLESH_VEINS = ITEMS.register(
      "tainted_flesh_veins", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_FLESH_VEINS.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_FLESH_BLOCK = ITEMS.register(
      "tainted_flesh_block", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_FLESH_BLOCK.get(), new Properties())
   );
   public static final RegistryObject<Item> INFECTED_FLESH_BLOCK = ITEMS.register(
      "infected_flesh_block", () -> new BlockItem((Block)WitherStormModBlocks.INFECTED_FLESH_BLOCK.get(), new Properties())
   );
   public static final RegistryObject<Item> HARDENED_FLESH_BLOCK = ITEMS.register(
      "hardened_flesh_block", () -> new BlockItem((Block)WitherStormModBlocks.HARDENED_FLESH_BLOCK.get(), new Properties())
   );
   public static final RegistryObject<Item> WITHERED_PHLEGM_BLOCK = ITEMS.register(
      "withered_phlegm_block", () -> new BlockItem((Block)WitherStormModBlocks.WITHERED_PHLEGM_BLOCK.get(), new Properties().rarity(Rarity.UNCOMMON))
   );
   public static final RegistryObject<Item> TAINTED_STONE = ITEMS.register(
      "tainted_stone", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_STONE.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_STONE_STAIRS = ITEMS.register(
      "tainted_stone_stairs", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_STONE_STAIRS.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_STONE_SLAB = ITEMS.register(
      "tainted_stone_slab", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_STONE_SLAB.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_STONE_BUTTON = ITEMS.register(
      "tainted_stone_button", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_STONE_BUTTON.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_STONE_PRESSURE_PLATE = ITEMS.register(
      "tainted_stone_pressure_plate", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_STONE_PRESSURE_PLATE.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_COBBLESTONE = ITEMS.register(
      "tainted_cobblestone", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_COBBLESTONE.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_COBBLESTONE_STAIRS = ITEMS.register(
      "tainted_cobblestone_stairs", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_COBBLESTONE_STAIRS.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_COBBLESTONE_SLAB = ITEMS.register(
      "tainted_cobblestone_slab", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_COBBLESTONE_SLAB.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_COBBLESTONE_WALL = ITEMS.register(
      "tainted_cobblestone_wall", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_COBBLESTONE_WALL.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_DIRT = ITEMS.register(
      "tainted_dirt", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_DIRT.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_SAND = ITEMS.register(
      "tainted_sand", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SAND.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_SANDSTONE = ITEMS.register(
      "tainted_sandstone", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SANDSTONE.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_SANDSTONE_SLAB = ITEMS.register(
      "tainted_sandstone_slab", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SANDSTONE_SLAB.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_SANDSTONE_STAIRS = ITEMS.register(
      "tainted_sandstone_stairs", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SANDSTONE_STAIRS.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_SANDSTONE_WALL = ITEMS.register(
      "tainted_sandstone_wall", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SANDSTONE_WALL.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_CUT_SANDSTONE = ITEMS.register(
      "tainted_cut_sandstone", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_CUT_SANDSTONE.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_CUT_SANDSTONE_SLAB = ITEMS.register(
      "tainted_cut_sandstone_slab", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_CUT_SANDSTONE_SLAB.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_CHISELED_SANDSTONE = ITEMS.register(
      "tainted_chiseled_sandstone", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_CHISELED_SANDSTONE.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_SMOOTH_SANDSTONE = ITEMS.register(
      "tainted_smooth_sandstone", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_SMOOTH_SANDSTONE_STAIRS = ITEMS.register(
      "tainted_smooth_sandstone_stairs", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_STAIRS.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_SMOOTH_SANDSTONE_SLAB = ITEMS.register(
      "tainted_smooth_sandstone_slab", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_SLAB.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_SMOOTH_SANDSTONE_WALL = ITEMS.register(
      "tainted_smooth_sandstone_wall", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SMOOTH_SANDSTONE_WALL.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_GLASS = ITEMS.register(
      "tainted_glass", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_GLASS.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_GLASS_PANE = ITEMS.register(
      "tainted_glass_pane", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_GLASS_PANE.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_PLANKS = ITEMS.register(
      "tainted_planks", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_PLANKS.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_LOG = ITEMS.register(
      "tainted_log", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_LOG.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_WOOD = ITEMS.register(
      "tainted_wood", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_WOOD.get(), new Properties())
   );
   public static final RegistryObject<Item> STRIPPED_TAINTED_LOG = ITEMS.register(
      "stripped_tainted_log", () -> new BlockItem((Block)WitherStormModBlocks.STRIPPED_TAINTED_LOG.get(), new Properties())
   );
   public static final RegistryObject<Item> STRIPPED_TAINTED_WOOD = ITEMS.register(
      "stripped_tainted_wood", () -> new BlockItem((Block)WitherStormModBlocks.STRIPPED_TAINTED_WOOD.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_LEAVES = ITEMS.register(
      "tainted_leaves", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_LEAVES.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_DOOR = ITEMS.register(
      "tainted_door", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_DOOR.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_TRAPDOOR = ITEMS.register(
      "tainted_trapdoor", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_TRAPDOOR.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_BUTTON = ITEMS.register(
      "tainted_button", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_BUTTON.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_PRESSURE_PLATE = ITEMS.register(
      "tainted_pressure_plate", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_PRESSURE_PLATE.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_STAIRS = ITEMS.register(
      "tainted_stairs", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_STAIRS.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_SLAB = ITEMS.register(
      "tainted_slab", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_SLAB.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_FENCE = ITEMS.register(
      "tainted_fence", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_FENCE.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_FENCE_GATE = ITEMS.register(
      "tainted_fence_gate", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_FENCE_GATE.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_MUSHROOM = ITEMS.register(
      "tainted_mushroom", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_MUSHROOM.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_TORCH = ITEMS.register(
      "tainted_torch",
      () -> new StandingAndWallBlockItem(
         (Block)WitherStormModBlocks.TAINTED_TORCH.get(), (Block)WitherStormModBlocks.TAINTED_WALL_TORCH.get(), new Properties(), Direction.DOWN
      )
   );
   public static final RegistryObject<Item> TAINTED_SIGN = ITEMS.register(
      "tainted_sign", () -> new SignItem(new Properties(), (Block)WitherStormModBlocks.TAINTED_SIGN.get(), (Block)WitherStormModBlocks.TAINTED_WALL_SIGN.get())
   );
   public static final RegistryObject<Item> TAINTED_PUMPKIN = ITEMS.register(
      "tainted_pumpkin", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_PUMPKIN.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_CARVED_PUMPKIN = ITEMS.register(
      "tainted_carved_pumpkin", () -> new TaintedCarvedPumpkinItem((Block)WitherStormModBlocks.TAINTED_CARVED_PUMPKIN.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_JACK_O_LANTERN = ITEMS.register(
      "tainted_jack_o_lantern", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_JACK_O_LANTERN.get(), new Properties())
   );
   public static final RegistryObject<Item> TAINTED_DUST_BLOCK = ITEMS.register(
      "tainted_dust_block", () -> new BlockItem((Block)WitherStormModBlocks.TAINTED_DUST_BLOCK.get(), new Properties())
   );

   public static void registerBrewingRecipes() {
      BrewingRecipeRegistry.addRecipe(
         createBrewingRecipe(Items.POTION, Potions.POISON, (Item)WITHERED_SPIDER_EYE.get(), (Potion)WitherStormModPotions.WITHER.get())
      );
      BrewingRecipeRegistry.addRecipe(
         createBrewingRecipe(Items.POTION, Potions.LONG_POISON, (Item)WITHERED_SPIDER_EYE.get(), (Potion)WitherStormModPotions.LONG_WITHER.get())
      );
      BrewingRecipeRegistry.addRecipe(
         createBrewingRecipe(Items.POTION, Potions.STRONG_POISON, (Item)WITHERED_SPIDER_EYE.get(), (Potion)WitherStormModPotions.STRONG_WITHER.get())
      );
      BrewingRecipeRegistry.addRecipe(
         createBrewingRecipe(Items.SPLASH_POTION, Potions.POISON, (Item)WITHERED_SPIDER_EYE.get(), (Potion)WitherStormModPotions.WITHER.get())
      );
      BrewingRecipeRegistry.addRecipe(
         createBrewingRecipe(Items.SPLASH_POTION, Potions.LONG_POISON, (Item)WITHERED_SPIDER_EYE.get(), (Potion)WitherStormModPotions.LONG_WITHER.get())
      );
      BrewingRecipeRegistry.addRecipe(
         createBrewingRecipe(Items.SPLASH_POTION, Potions.STRONG_POISON, (Item)WITHERED_SPIDER_EYE.get(), (Potion)WitherStormModPotions.STRONG_WITHER.get())
      );
      BrewingRecipeRegistry.addRecipe(
         createBrewingRecipe(Items.LINGERING_POTION, Potions.POISON, (Item)WITHERED_SPIDER_EYE.get(), (Potion)WitherStormModPotions.WITHER.get())
      );
      BrewingRecipeRegistry.addRecipe(
         createBrewingRecipe(Items.LINGERING_POTION, Potions.LONG_POISON, (Item)WITHERED_SPIDER_EYE.get(), (Potion)WitherStormModPotions.LONG_WITHER.get())
      );
      BrewingRecipeRegistry.addRecipe(
         createBrewingRecipe(Items.LINGERING_POTION, Potions.STRONG_POISON, (Item)WITHERED_SPIDER_EYE.get(), (Potion)WitherStormModPotions.STRONG_WITHER.get())
      );
      BrewingRecipeRegistry.addRecipe(
         createBrewingRecipe(Items.POTION, (Potion)WitherStormModPotions.WITHER.get(), Items.REDSTONE, (Potion)WitherStormModPotions.LONG_WITHER.get())
      );
      BrewingRecipeRegistry.addRecipe(
         createBrewingRecipe(Items.POTION, (Potion)WitherStormModPotions.WITHER.get(), Items.GLOWSTONE_DUST, (Potion)WitherStormModPotions.STRONG_WITHER.get())
      );
      BrewingRecipeRegistry.addRecipe(
         createBrewingRecipe(Items.SPLASH_POTION, (Potion)WitherStormModPotions.WITHER.get(), Items.REDSTONE, (Potion)WitherStormModPotions.LONG_WITHER.get())
      );
      BrewingRecipeRegistry.addRecipe(
         createBrewingRecipe(
            Items.SPLASH_POTION, (Potion)WitherStormModPotions.WITHER.get(), Items.GLOWSTONE_DUST, (Potion)WitherStormModPotions.STRONG_WITHER.get()
         )
      );
      BrewingRecipeRegistry.addRecipe(
         createBrewingRecipe(
            Items.LINGERING_POTION, (Potion)WitherStormModPotions.WITHER.get(), Items.REDSTONE, (Potion)WitherStormModPotions.LONG_WITHER.get()
         )
      );
      BrewingRecipeRegistry.addRecipe(
         createBrewingRecipe(
            Items.LINGERING_POTION, (Potion)WitherStormModPotions.WITHER.get(), Items.GLOWSTONE_DUST, (Potion)WitherStormModPotions.STRONG_WITHER.get()
         )
      );
   }

   public static BrewingRecipe createBrewingRecipe(Item potionType, Potion potion, Item ingredient, Potion output) {
      return new BrewingRecipe(
         Ingredient.of(new ItemStack[]{PotionUtils.setPotion(new ItemStack(potionType), potion)}),
         Ingredient.of(new ItemLike[]{ingredient}),
         PotionUtils.setPotion(new ItemStack(potionType), output)
      );
   }
}
