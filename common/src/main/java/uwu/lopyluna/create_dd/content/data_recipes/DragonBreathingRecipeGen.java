package uwu.lopyluna.create_dd.content.data_recipes;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import uwu.lopyluna.create_dd.registry.DesiresRecipeTypes;

@MethodsReturnNonnullByDefault
@SuppressWarnings({"unused"})
public class DragonBreathingRecipeGen extends DesireProcessingRecipeGen {

	GeneratedRecipe

			BLAZE_CAKE = convert(Items.APPLE, Items.CHORUS_FRUIT, .50f),
			DRAGON_BREATH = convert(Items.POTION, Items.DRAGON_BREATH),
			END_STONE = convert(Items.DEEPSLATE, Items.END_STONE),
			ENDER_PEARL = convert(Items.MAGMA_CREAM, Items.ENDER_PEARL, .15f),
			GHAST_TEAR = convert(Items.BLUE_ICE, Items.GHAST_TEAR, .10f, Items.GHAST_TEAR, .05f),
			CRYING_OBSIDIAN = convert(Items.OBSIDIAN, Items.CRYING_OBSIDIAN);


	public DragonBreathingRecipeGen(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected DesiresRecipeTypes getRecipeType() {
		return DesiresRecipeTypes.DRAGON_BREATHING;
	}

}
