package uwu.lopyluna.create_dd.content.data_recipes;

import static com.simibubi.create.foundation.data.recipe.CompatMetals.ALUMINUM;
import static com.simibubi.create.foundation.data.recipe.CompatMetals.LEAD;
import static com.simibubi.create.foundation.data.recipe.CompatMetals.NICKEL;
import static com.simibubi.create.foundation.data.recipe.CompatMetals.OSMIUM;
import static com.simibubi.create.foundation.data.recipe.CompatMetals.PLATINUM;
import static com.simibubi.create.foundation.data.recipe.CompatMetals.QUICKSILVER;
import static com.simibubi.create.foundation.data.recipe.CompatMetals.SILVER;
import static com.simibubi.create.foundation.data.recipe.CompatMetals.TIN;
import static com.simibubi.create.foundation.data.recipe.CompatMetals.URANIUM;

import com.simibubi.create.AllItems;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.registry.DesiresItems;
import uwu.lopyluna.create_dd.registry.DesiresRecipeTypes;

@SuppressWarnings({"unused", "deprecation"})
public class SeethingRecipeGen extends DesireProcessingRecipeGen {

	GeneratedRecipe

		ENDER_EYE = convert(Items.ENDER_PEARL, Items.ENDER_EYE),
		MAGMA_BLOCK = convert(Blocks.NETHERRACK, Blocks.MAGMA_BLOCK),
		MAGMA_CREAM = convert(Items.SLIME_BALL, Items.MAGMA_CREAM),
		COBBLED_DEEPSLATE = convert(Blocks.COBBLESTONE, Blocks.COBBLED_DEEPSLATE),

		LAPIS_LAZULI_SHARD = convertChanceRecipe(Items.CALCITE, DesiresItems.LAPIS_LAZULI_SHARD.get(), .75f),
		DIAMOND_SHARD = convertChanceRecipe(Items.COAL_BLOCK, DesiresItems.DIAMOND_SHARD.get(), .25f),
		DIAMOND_SHARD_V2 = convertChanceRecipe(Items.DEEPSLATE_COAL_ORE, DesiresItems.DIAMOND_SHARD.get(), .75f),

		NETHERITE_SCRAP = secondaryRecipe(() -> Items.ANCIENT_DEBRIS, () -> Items.NETHERITE_SCRAP, () -> Items.NETHERITE_SCRAP, .15f),

		CRUSHED_COPPER = crushedOre(AllItems.CRUSHED_COPPER::get, Items.COPPER_INGOT, Items.COPPER_INGOT, .5f),
		CRUSHED_ZINC = crushedOre(AllItems.CRUSHED_ZINC::get, AllItems.ZINC_INGOT.get(), AllItems.ZINC_INGOT.get(), .25f),
		CRUSHED_GOLD = crushedOre(AllItems.CRUSHED_GOLD::get, Items.GOLD_INGOT, Items.GOLD_INGOT, .5f),
		CRUSHED_IRON = crushedOre(AllItems.CRUSHED_IRON::get, Items.IRON_INGOT, Items.IRON_INGOT, .75f),

		CRUSHED_OSMIUM = moddedCrushedOre(AllItems.CRUSHED_OSMIUM, OSMIUM),
		CRUSHED_PLATINUM = moddedCrushedOre(AllItems.CRUSHED_PLATINUM, PLATINUM),
		CRUSHED_SILVER = moddedCrushedOre(AllItems.CRUSHED_SILVER, SILVER),
		CRUSHED_TIN = moddedCrushedOre(AllItems.CRUSHED_TIN, TIN),
		CRUSHED_LEAD = moddedCrushedOre(AllItems.CRUSHED_LEAD, LEAD),
		CRUSHED_QUICKSILVER = moddedCrushedOre(AllItems.CRUSHED_QUICKSILVER, QUICKSILVER),
		CRUSHED_BAUXITE = moddedCrushedOre(AllItems.CRUSHED_BAUXITE, ALUMINUM),
		CRUSHED_URANIUM = moddedCrushedOre(AllItems.CRUSHED_URANIUM, URANIUM),
		CRUSHED_NICKEL = moddedCrushedOre(AllItems.CRUSHED_NICKEL, NICKEL)

	;

	protected static @NotNull String getItemName(ItemLike pItemLike) {
		return Registry.ITEM.getKey(pItemLike.asItem()).getPath();
	}

	public SeethingRecipeGen(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected @NotNull DesiresRecipeTypes getRecipeType() {
		return DesiresRecipeTypes.SEETHING;
	}

}
