package uwu.lopyluna.create_dd.content.data_recipes;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Blocks;
import uwu.lopyluna.create_dd.registry.DesiresPaletteStoneTypes;

@MethodsReturnNonnullByDefault
@SuppressWarnings({"unused"})
public class WashingRecipeGen extends DesireProcessingRecipeGen {

	GeneratedRecipe

			WEATHER_LIMESTONE = convert(AllPaletteStoneTypes.LIMESTONE.getBaseBlock().get(), DesiresPaletteStoneTypes.WEATHERED_LIMESTONE.getBaseBlock().get()),

			EXPOSED_COPPER = convert(Blocks.COPPER_BLOCK, Blocks.EXPOSED_COPPER),
			WEATHERED_COPPER = convert(Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER),
			OXIDIZED_COPPER = convert(Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER),

			EXPOSED_CUT_COPPER = convert(Blocks.CUT_COPPER, Blocks.EXPOSED_CUT_COPPER),
			WEATHERED_CUT_COPPER = convert(Blocks.EXPOSED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER),
			OXIDIZED_CUT_COPPER = convert(Blocks.WEATHERED_CUT_COPPER, Blocks.OXIDIZED_CUT_COPPER),

			EXPOSED_CUT_COPPER_SLAB = convert(Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB),
			WEATHERED_CUT_COPPER_SLAB = convert(Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB),
			OXIDIZED_CUT_COPPER_SLAB = convert(Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER_SLAB),

			EXPOSED_CUT_COPPER_STAIRS = convert(Blocks.CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER_STAIRS),
			WEATHERED_CUT_COPPER_STAIRS = convert(Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER_STAIRS),
			OXIDIZED_CUT_COPPER_STAIRS = convert(Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.OXIDIZED_CUT_COPPER_STAIRS);

	public WashingRecipeGen(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected AllRecipeTypes getRecipeType() {
		return AllRecipeTypes.SPLASHING;
	}

}
