package uwu.lopyluna.create_dd.content.recipes;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import net.minecraft.world.level.Level;
import uwu.lopyluna.create_dd.infrastructure.porting_lib_classes.ItemStackHandler;
import uwu.lopyluna.create_dd.infrastructure.porting_lib_classes.RecipeWrapper;
import uwu.lopyluna.create_dd.registry.DesiresRecipeTypes;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FreezingRecipe extends ProcessingRecipe<FreezingRecipe.FreezingWrapper> {

	public FreezingRecipe(ProcessingRecipeParams params) {
		super(DesiresRecipeTypes.FREEZING, params);
	}

	@Override
	public boolean matches(FreezingWrapper inv, Level worldIn) {
		if (inv.isEmpty())
			return false;
		return ingredients.get(0)
			.test(inv.getItem(0));
	}

	@Override
	protected int getMaxInputCount() {
		return 1;
	}

	@Override
	protected int getMaxOutputCount() {
		return 12;
	}

	public static class FreezingWrapper extends RecipeWrapper {
		public FreezingWrapper() {
			super(new ItemStackHandler(1));
		}
	}

}
