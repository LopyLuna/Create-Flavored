package uwu.lopyluna.create_dd.content.data_recipes;

import com.simibubi.create.AllItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import uwu.lopyluna.create_dd.registry.DesiresRecipeTypes;

@MethodsReturnNonnullByDefault
@SuppressWarnings({"unused"})
public class FreezingRecipeGen extends DesireProcessingRecipeGen {

	GeneratedRecipe

			BLAZE_CAKE = secondaryRecipe(AllItems.BLAZE_CAKE::get, AllItems.POWDERED_OBSIDIAN::get, AllItems.CINDER_FLOUR::get, .90f),
			PACKED_ICE = convert(Items.ICE, Items.PACKED_ICE),
			BLUE_ICE = convert(Items.PACKED_ICE, Items.BLUE_ICE),
			POWDER_SNOW_BUCKET = convert(Items.WATER_BUCKET, Items.POWDER_SNOW_BUCKET),
			SLIME_BALL = convert(Items.MAGMA_CREAM, Items.SLIME_BALL),
			SNOW = convert(Items.SNOWBALL, Items.SNOW),
			SNOW_BLOCK = convert(Items.SNOW, Items.SNOW_BLOCK),
			OBSIDIAN = convert(Items.CRYING_OBSIDIAN, Items.OBSIDIAN);


	public FreezingRecipeGen(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected DesiresRecipeTypes getRecipeType() {
		return DesiresRecipeTypes.FREEZING;
	}

}
