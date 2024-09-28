package uwu.lopyluna.create_dd.infrastructure.config;

import com.simibubi.create.foundation.config.ConfigBase;
import uwu.lopyluna.create_dd.registry.addons.DreamsAddons;

@SuppressWarnings({"unused"})
public class DRecipes extends ConfigBase {

	public final ConfigBool hydraulicBulkPressing = b(true, "hydraulicBulkPressing", Comments.hydraulicBulkPressing);
	public final ConfigInt hydraulicLavaDrainPressing = i(250, 1, 1000, "hydraulicLavaDrainPressing", Comments.hydraulicLavaDrainPressing);
	public final ConfigInt hydraulicFluidDrainPressing = i(1000, 1, 1000, "hydraulicFluidDrainPressing", Comments.hydraulicFluidDrainPressing);
	//public final ConfigBool lumberBulkCutting = b(true, "lumberBulkCutting", Comments.lumberBulkCutting);*
	//public final ConfigBool allowStonecuttingOnLumberSaw = b(false, "allowStonecuttingOnLumberSaw", Comments.allowStonecuttingOnLumberSaw);*
	//public final ConfigBool allowWoodcuttingOnLumberSaw = b(true, "allowWoodcuttingOnLumberSaw", Comments.allowWoodcuttingOnLumberSaw);*
	//public final ConfigBool displayLogStrippingRecipes = b(true, "displayLogStrippingRecipes", Comments.displayLogStrippingRecipes);*

	public final ConfigBool blaze_gold_recipe = !DreamsAddons.EXTRAS.isLoaded() ? null : b(true, "blaze_gold_recipe", Comments.blaze_gold_recipe);

	public final ConfigBool refined_radiance_recipe = b(true, "refined_radiance_recipe", Comments.refined_radiance_recipe);
	public final ConfigInt refined_radiance_max_height = i(319, -2048, 2048, "refined_radiance_max_height", Comments.refined_radiance_max);
	public final ConfigInt refined_radiance_min_height = i(-32, -2048, 2048, "refined_radiance_min_height", Comments.refined_radiance_min);
	public final ConfigInt refined_radiance_light_level = i(15, 0, 15, "refined_radiance_light_level", Comments.refined_radiance_light_level);

	public final ConfigBool shadow_steel_recipe = b(true, "shadow_steel_recipe", Comments.shadow_steel_recipe);
	public final ConfigInt shadow_steel_min_height = i(-10, -2048, 2048, "shadow_steel_min_height", Comments.shadow_steel_min);


	@Override
	public String getName() {
		return "recipes";
	}

	private static class Comments {
		static String hydraulicBulkPressing = "Allow the Hydraulic Press to process entire stacks at a time.";
		static String hydraulicLavaDrainPressing = "Value Hydraulic Press to drain amount of lava of each bonk.";
		static String hydraulicFluidDrainPressing = "Value Hydraulic Press to drain amount of fluid of each bonk.";

		static String lumberBulkCutting = "Allow the Lumber Saw to process entire stacks at a time.";
		static String allowStonecuttingOnLumberSaw = "Allow any stonecutting recipes to be processed by a Lumber Saw.";
		static String allowWoodcuttingOnLumberSaw = "Allow any Druidcraft woodcutter recipes to be processed by a Lumber Saw.";
		static String displayLogStrippingRecipes = "Display vanilla Log-stripping interactions in JEI.";

		static String compound_recipe = "Compound Recipes";
		static String blaze_gold_recipe = "Blaze Brass Recipe";
		static String refined_radiance_recipe = "Refined Radiance Recipe";
		static String refined_radiance_max = "Shadow Steel Recipe Require Max Height";
		static String refined_radiance_min = "Shadow Steel Recipe Require Min Height";
		static String refined_radiance_light_level = "Shadow Steel Recipe Require Light Level";
		static String shadow_steel_recipe = "Shadow Steel Recipe";
		static String shadow_steel_min = "Shadow Steel Recipe Require Min Height";
	}

}
