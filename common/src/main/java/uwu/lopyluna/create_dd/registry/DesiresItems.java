package uwu.lopyluna.create_dd.registry;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.item.CombustibleItem;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.DesireUtil;
import uwu.lopyluna.create_dd.content.blocks.kinetics.modular_drill.ModularDrillHeadItem;
import uwu.lopyluna.create_dd.content.items.equipment.NameableRecordItem;
import uwu.lopyluna.create_dd.content.items.equipment.block_zapper.BlockZapperItem;
import uwu.lopyluna.create_dd.content.items.equipment.clockwork_crossbow.ClockworkCrossbow;
import uwu.lopyluna.create_dd.content.items.equipment.deforester_saw.DeforesterSawItem;
import uwu.lopyluna.create_dd.content.items.equipment.excavation_drill.ExcavationDrillItem;
import uwu.lopyluna.create_dd.content.items.equipment.gilded_rose_tools.*;
import uwu.lopyluna.create_dd.content.items.equipment.handheld_nozzle.HandheldNozzleItem;
import uwu.lopyluna.create_dd.content.items.materials.ChromaticCompound;
import uwu.lopyluna.create_dd.content.items.materials.RefinedRadiance;
import uwu.lopyluna.create_dd.content.items.materials.ShadowSteel;
import uwu.lopyluna.create_dd.registry.addons.DreamsAddons;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;
import static uwu.lopyluna.create_dd.Desires.REGISTRATE;
import static uwu.lopyluna.create_dd.registry.DesiresTags.forgeItemTag;
import static uwu.lopyluna.create_dd.registry.DesiresTags.optionalTag;

@SuppressWarnings({"unused", "deprecation", "SameParameterValue"})
public class DesiresItems {

	public static final ItemEntry<SequencedAssemblyItem>
			INCOMPLETE_KINETIC_MECHANISM = sequencedItem("incomplete_kinetic_mechanism");

	public static final ItemEntry<Item>
			KINETIC_MECHANISM = item("kinetic_mechanism");

	public static final ItemEntry<Item> RAW_RUBBER = REGISTRATE.item("raw_rubber", Item::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/generated")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/" + c.getId().getPath())))
			.tag(forgeItemTag("raw_rubbers"))
			.recipe((c, p) -> {
				Item output = DesiresBlocks.RAW_RUBBER_BLOCK.get().asItem();
				ShapedRecipeBuilder.shaped(output, 1)
						.pattern("CCC")
						.pattern("CCC")
						.pattern("CCC")
						.define('C', c.get())
						.unlockedBy("has_" + getItemName(output), has(output))
						.save(p, DesireUtil.asResource("crafting/" + getItemName(output) + "_from_" + c.getName()));
				ShapelessRecipeBuilder.shapeless(c.get(), 9)
						.requires(output)
						.unlockedBy("has_" + c.getName(), has(c.get()))
						.save(p, DesireUtil.asResource("crafting/" + c.getName() + "_from_" + getItemName(output)));
			})
			.lang("Raw Rubber")
			.register();

	public static final ItemEntry<Item> RUBBER = REGISTRATE.item("rubber", Item::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/generated")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/" + c.getId().getPath())))
			.tag(forgeItemTag("rubbers"), forgeItemTag("crude_rubbers"))
			.recipe((c, p) -> {
				Item output = DesiresBlocks.RUBBER_BLOCK.get().asItem();
				ShapedRecipeBuilder.shaped(output, 1)
						.pattern("CCC")
						.pattern("CCC")
						.pattern("CCC")
						.define('C', c.get())
						.unlockedBy("has_" + getItemName(output), has(output))
						.save(p, DesireUtil.asResource("crafting/" + getItemName(output) + "_from_" + c.getName()));
				ShapelessRecipeBuilder.shapeless(c.get(), 9)
						.requires(output)
						.unlockedBy("has_" + c.getName(), has(c.get()))
						.save(p, DesireUtil.asResource("crafting/" + c.getName() + "_from_" + getItemName(output)));

				SimpleCookingRecipeBuilder.cooking(Ingredient.of(RAW_RUBBER.get()), c.get(), 2, 600, RecipeSerializer.SMOKING_RECIPE)
						.unlockedBy("has_" + getItemName(RAW_RUBBER.get()), has(RAW_RUBBER.get()))
						.save(p, DesireUtil.asResource("smoking/" + c.getId().getPath()));
			})
			.lang("Rubber")
			.register();

	public static final ItemEntry<ChromaticCompound> CHROMATIC_COMPOUND = REGISTRATE.item("chromatic_compound", ChromaticCompound::new)
					.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
					.properties(p -> p.stacksTo(16)
							.rarity(Rarity.UNCOMMON)
							.fireResistant())
					.register();

	public static final ItemEntry<ShadowSteel> SHADOW_STEEL = REGISTRATE.item("shadow_steel", ShadowSteel::new)
					.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
					.properties(p -> p.stacksTo(16)
							.rarity(Rarity.UNCOMMON)
							.fireResistant())
					.register();

	public static final ItemEntry<ShadowSteel> SHADOW_STEEL_SHEET = !DreamsAddons.EXTRAS.isLoaded() ? null : REGISTRATE.item("shadow_steel_sheet", ShadowSteel::new)
					.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
					.properties(p -> p.stacksTo(16)
							.rarity(Rarity.UNCOMMON)
							.fireResistant())
					.register();

	public static final ItemEntry<RefinedRadiance> REFINED_RADIANCE =
			REGISTRATE.item("refined_radiance", RefinedRadiance::new)
					.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
					.properties(p -> p.stacksTo(16)
							.rarity(Rarity.UNCOMMON)
							.fireResistant())
					.register();

	public static final ItemEntry<RefinedRadiance> REFINED_RADIANCE_SHEET = !DreamsAddons.EXTRAS.isLoaded() ? null : REGISTRATE.item("refined_radiance_sheet", RefinedRadiance::new)
					.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
					.properties(p -> p.stacksTo(16)
							.rarity(Rarity.UNCOMMON)
							.fireResistant())
					.register();

	public static final ItemEntry<Item> BURY_BLEND = REGISTRATE.item("bury_blend", Item::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/generated")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/" + c.getId().getPath())))
			.tag(forgeItemTag("ingots/bury_blend"), forgeItemTag("ingots"), forgeItemTag("bury_blends"))
			.recipe((c, p) -> ShapelessRecipeBuilder.shapeless(c.get(), 2)
					.requires(Items.LAPIS_LAZULI)
					.requires(AllItems.CRUSHED_IRON.get())
					.requires(AllItems.CRUSHED_IRON.get())
					.requires(Items.LAPIS_LAZULI)
					.unlockedBy("has_" + getItemName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI))
					.save(p, DesireUtil.asResource("crafting/bury_blend")))
			.lang("Bury Blend")
			.register();

	public static final ItemEntry<Item> LAPIS_LAZULI_SHARD = REGISTRATE.item("lapis_lazuli_shard", Item::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/generated")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/" + c.getId().getPath())))
			.tag(forgeItemTag("nuggets/lapis"), forgeItemTag("nuggets"))
			.recipe((c, p) -> {
				Item output = Items.LAPIS_LAZULI;
				ShapedRecipeBuilder.shaped(output, 1)
						.pattern("CC")
						.pattern("CC")
						.define('C', c.get())
						.unlockedBy("has_" + getItemName(output), has(output))
						.save(p, DesireUtil.asResource("crafting/" + getItemName(output) + "_from_" + c.getName()));
				ShapelessRecipeBuilder.shapeless(c.get(), 4)
						.requires(output)
						.unlockedBy("has_" + c.getName(), has(c.get()))
						.save(p, DesireUtil.asResource("crafting/" + c.getName() + "_from_" + getItemName(output)));
			})
			.register();

	public static final ItemEntry<Item> DIAMOND_SHARD = REGISTRATE.item("diamond_shard", Item::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/generated")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/" + c.getId().getPath())))
			.tag(forgeItemTag("nuggets/diamond"), forgeItemTag("nuggets"))
			.recipe((c, p) -> {
				Item output = Items.DIAMOND;
				ShapedRecipeBuilder.shaped(output, 1)
					.pattern("CC")
					.pattern("CC")
						.define('C', c.get())
						.unlockedBy("has_" + getItemName(output), has(output))
						.save(p, DesireUtil.asResource("crafting/" + getItemName(output) + "_from_" + c.getName()));
				ShapelessRecipeBuilder.shapeless(c.get(), 4)
						.requires(output)
						.unlockedBy("has_" + c.getName(), has(c.get()))
						.save(p, DesireUtil.asResource("crafting/" + c.getName() + "_from_" + getItemName(output)));
			})
			.register();

	public static final ItemEntry<CombustibleItem> COAL_PIECE = REGISTRATE.item("coal_piece", CombustibleItem::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.onRegister(i -> i.setBurnTime(200))
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/generated")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/" + c.getId().getPath())))
			.tag(forgeItemTag("nuggets/coal"), forgeItemTag("nuggets"))
			.recipe((c, p) -> {
				Item output = Items.COAL;
				ShapelessRecipeBuilder.shapeless(output, 1)
						.requires(c.get()).requires(c.get())
						.requires(c.get()).requires(c.get())
						.requires(c.get()).requires(c.get())
						.requires(c.get()).requires(c.get())
						.unlockedBy("has_" + getItemName(output), has(output))
						.save(p, DesireUtil.asResource("crafting/" + getItemName(output) + "_from_" + c.getName()));
				ShapelessRecipeBuilder.shapeless(c.get(), 8)
						.requires(output)
						.unlockedBy("has_" + c.getName(), has(c.get()))
						.save(p, DesireUtil.asResource("crafting/" + c.getName() + "_from_" + getItemName(output)));
			})
			.register();

	public static final ItemEntry<GRSwordItem> GILDED_ROSE_SWORD = REGISTRATE.item("gilded_rose_sword",
					p -> new GRSwordItem(DesireTiers.GILDED_ROSE, 3, -2.4F, p))
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/handheld")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/" + c.getId().getPath())))
			.properties(p -> p.rarity(Rarity.UNCOMMON))
			.tag(DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.tag)
			.tag(DesiresTags.AllItemTags.SWORD.tag)
			.register();

	public static final ItemEntry<GRPickaxeItem> GILDED_ROSE_PICKAXE = REGISTRATE.item("gilded_rose_pickaxe",
					p -> new GRPickaxeItem(DesireTiers.GILDED_ROSE, 1, -2.8F, p))
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/handheld")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/" + c.getId().getPath())))
			.properties(p -> p.rarity(Rarity.UNCOMMON))
			.tag(DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.tag)
			.tag(DesiresTags.AllItemTags.PICKAXE.tag)
			.register();

	public static final ItemEntry<GRAxeItem> GILDED_ROSE_AXE = REGISTRATE.item("gilded_rose_axe",
					p -> new GRAxeItem(DesireTiers.GILDED_ROSE, 6.0F, -3.0F, p))
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/handheld")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/" + c.getId().getPath())))
			.properties(p -> p.rarity(Rarity.UNCOMMON))
			.tag(DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.tag)
			.tag(DesiresTags.AllItemTags.AXE.tag)
			.register();

	public static final ItemEntry<GRShovelItem> GILDED_ROSE_SHOVEL = REGISTRATE.item("gilded_rose_shovel",
					p -> new GRShovelItem(DesireTiers.GILDED_ROSE, 1.5F, -3.0F, p))
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/handheld")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/" + c.getId().getPath())))
			.properties(p -> p.rarity(Rarity.UNCOMMON))
			.tag(DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.tag)
			.tag(DesiresTags.AllItemTags.SHOVEL.tag)
			.register();

	public static final ItemEntry<GRHoeItem> GILDED_ROSE_HOE = REGISTRATE.item("gilded_rose_hoe",
					p -> new GRHoeItem(DesireTiers.GILDED_ROSE, -2, -3.0F, p))
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/handheld")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/" + c.getId().getPath())))
			.properties(p -> p.rarity(Rarity.UNCOMMON))
			.tag(DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.tag)
			.tag(DesiresTags.AllItemTags.HOE.tag)
			.register();

	public static final ItemEntry<DeforesterSawItem> DEFORESTER_SAW = REGISTRATE.item("deforester_saw", DeforesterSawItem::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.model(AssetLookup.itemModelWithPartials())
			.properties(p -> p.rarity(Rarity.UNCOMMON))
			.recipe((c, p) -> {
				ShapedRecipeBuilder.shaped(c.get(), 1)
						.pattern(" II")
						.pattern("AKI")
						.pattern(" S ")
						.define('K', KINETIC_MECHANISM.get())
						.define('A', AllItems.ANDESITE_ALLOY.get())
						.define('I', AllTags.forgeItemTag("plates/iron"))
						.define('S', Items.STICK)
						.unlockedBy("has_" + getItemName(KINETIC_MECHANISM.get().asItem()), has(KINETIC_MECHANISM.get()))
						.save(p, DesireUtil.asResource("crafting/equipment/flipped_" + c.getName()));
				ShapedRecipeBuilder.shaped(c.get(), 1)
						.pattern("II ")
						.pattern("IKA")
						.pattern(" S ")
						.define('K', KINETIC_MECHANISM.get())
						.define('A', AllItems.ANDESITE_ALLOY.get())
						.define('I', AllTags.forgeItemTag("plates/iron"))
						.define('S', Items.STICK)
						.unlockedBy("has_" + getItemName(KINETIC_MECHANISM.get().asItem()), has(KINETIC_MECHANISM.get()))
						.save(p, DesireUtil.asResource("crafting/equipment/" + c.getName()));
			})
			.tag(DesiresTags.AllItemTags.AXE.tag)
			.register();

	public static final ItemEntry<ExcavationDrillItem> EXCAVATION_DRILL = REGISTRATE.item("excavation_drill", ExcavationDrillItem::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.model(AssetLookup.itemModelWithPartials())
			.properties(p -> p.rarity(Rarity.UNCOMMON))
			.tag(DesiresTags.AllItemTags.PICKAXE.tag)
			.register();

	public static final ItemEntry<ClockworkCrossbow> CLOCKWORK_CROSSBOW = REGISTRATE.item("clockwork_crossbow", ClockworkCrossbow::new)
			.tab(() -> DesiresCreativeModeTabs.BETA_CREATIVE_TAB)
			.model(AssetLookup.itemModelWithPartials())
			.properties(p -> p.stacksTo(1).defaultDurability(500).durability(500).rarity(Rarity.UNCOMMON))
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/crossbow")).texture("layer0",
					new ResourceLocation("minecraft","item/crossbow_standby")))
			.tag(DesiresTags.AllItemTags.CROSSBOW.tag)
			.register();

	public static final ItemEntry<HandheldNozzleItem> HANDHELD_NOZZLE = REGISTRATE.item("handheld_nozzle", HandheldNozzleItem::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.model(AssetLookup.itemModelWithPartials())
			.properties(p -> p.rarity(Rarity.UNCOMMON))
			.register();

	public static final ItemEntry<BlockZapperItem> BLOCK_ZAPPER = REGISTRATE.item("handheld_block_zapper", BlockZapperItem::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.lang("Handheld Block Zapper")
			.model(AssetLookup.itemModelWithPartials())
			.register();

	public static final ItemEntry<ModularDrillHeadItem> DRILL_VEIN_HEAD = REGISTRATE.item("drill_vein_head",
					p -> new ModularDrillHeadItem(p, ModularDrillHeadItem.DrillType.VEIN))
			.tab(() -> DesiresCreativeModeTabs.BETA_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/handheld")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/bury_blend")))
			.register();
	public static final ItemEntry<ModularDrillHeadItem> DRILL_CRUSHER_HEAD = REGISTRATE.item("drill_crusher_head",
					p -> new ModularDrillHeadItem(p, ModularDrillHeadItem.DrillType.CRUSHER))
			.tab(() -> DesiresCreativeModeTabs.BETA_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/handheld")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/bury_blend")))
			.register();
	public static final ItemEntry<ModularDrillHeadItem> DRILL_ENCHANTABLE_HEAD = REGISTRATE.item("drill_enchantable_head",
					p -> new ModularDrillHeadItem(p, ModularDrillHeadItem.DrillType.ENCHANTABLE))
			.tab(() -> DesiresCreativeModeTabs.BETA_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/handheld")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/bury_blend")))
			.register();
	public static final ItemEntry<ModularDrillHeadItem> DRILL_SMELTING_HEAD = REGISTRATE.item("drill_smelting_head",
					p -> new ModularDrillHeadItem(p, ModularDrillHeadItem.DrillType.SMELTING))
			.tab(() -> DesiresCreativeModeTabs.BETA_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/handheld")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/bury_blend")))
			.register();

	public static final ItemEntry<NameableRecordItem> MUSIC_DISC_WALTZ_OF_THE_FLOWERS = REGISTRATE.item("music_disc_waltz_of_the_flowers",
					p -> new NameableRecordItem(10, DesiresSoundEvents.MUSIC_DISC_WALTZ_OF_THE_FLOWERS, p, 16600, "Tchaikovsky - Waltz of the Flowers"))
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.properties(p -> p.rarity(Rarity.RARE)
					.stacksTo(1))
			.recipe((c, p) -> p.stonecutting(DataIngredient.items(AllItems.ROSE_QUARTZ), c, 1))
			.tag(optionalTag(Registry.ITEM_REGISTRY, new ResourceLocation("minecraft", "music_discs")))
			.tag(optionalTag(Registry.ITEM_REGISTRY, new ResourceLocation("minecraft", "creeper_drop_music_discs")))
			.lang("Music Disc")
			.register();

	public static final ItemEntry<SpawnEggItem> INERT_BLAZELING_SPAWN_EGG = REGISTRATE.item("inert_blazeling_spawn_egg",
					p -> new SpawnEggItem(DesiresEntityTypes.INERT_BLAZELING.get(), 5451574, 13661252, p))
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/template_spawn_egg")))
			.register();

	public static final ItemEntry<SpawnEggItem> SEETHING_ABLAZE_SPAWN_EGG = REGISTRATE.item("seething_ablaze_spawn_egg",
					p -> new SpawnEggItem(DesiresEntityTypes.SEETHING_ABLAZE.get(), 44543, 56063, p))
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/template_spawn_egg")))
			.register();

	public static final ItemEntry<CombustibleItem> SEETHING_ABLAZE_ROD = REGISTRATE.item("seething_ablaze_rod", CombustibleItem::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.tag(AllTags.AllItemTags.BLAZE_BURNER_FUEL_SPECIAL.tag)
			.onRegister(i -> i.setBurnTime(9600))
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/handheld")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/" + c.getId().getPath())))
			.recipe((c, p) -> {
				Item output = DesiresItems.SEETHING_ABLAZE_POWDER.get().asItem();
				ShapelessRecipeBuilder.shapeless(output, 2)
						.requires(c.get())
						.unlockedBy("has_" + c.getName(), has(c.get()))
						.save(p, DesireUtil.asResource("crafting/" + getItemName(output) + "_from_" + c.getName()));
			})
			.register();

	public static final ItemEntry<CombustibleItem> SEETHING_ABLAZE_POWDER = REGISTRATE.item("seething_ablaze_powder", CombustibleItem::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.onRegister(i -> i.setBurnTime(4800))
			.model((c, p) -> p.withExistingParent(c.getId().getPath(),
					new ResourceLocation("item/generated")).texture("layer0",
					new ResourceLocation(Desires.MOD_ID,"item/" + c.getId().getPath())))
			.register();


	private static ItemEntry<Item> item(String name) {
		return REGISTRATE.item(name, Item::new)
				.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
				.register();
	}

	private static ItemEntry<SequencedAssemblyItem> sequencedItem(String name) {
		return REGISTRATE.item(name, SequencedAssemblyItem::new)
				.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
				.register();
	}


	protected static String getItemName(ItemLike pItemLike) {
		return Registry.ITEM.getKey(pItemLike.asItem()).getPath();
	}

	// Load this class

	public static void register() {}

}
