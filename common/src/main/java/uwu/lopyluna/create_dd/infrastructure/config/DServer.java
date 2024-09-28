package uwu.lopyluna.create_dd.infrastructure.config;

import com.simibubi.create.foundation.config.ConfigBase;

@SuppressWarnings({"unused"})
public class DServer extends ConfigBase {

	public final ConfigGroup World = group(0, "World", "World");
	public final ConfigInt chanceForOreStone = i(25, 1, 100, "chanceForOreStone", Comments.chanceForOreStone);
	public final ConfigInt chanceForArtificialOreStone = i(5, 0, 100, "chanceForArtificialOreStone", Comments.chanceForArtificialOreStone);

	public final ConfigGroup infrastructure = group(0, "infrastructure", Comments.infrastructure);
	public final DRecipes recipes = nested(0, DRecipes::new, Comments.recipes);
	public final DKinetics kinetics = nested(0, DKinetics::new, Comments.kinetics);
	public final DLogistics logistics = nested(0, DLogistics::new, Comments.logistics);
	public final DEquipment equipment = nested(0, DEquipment::new, Comments.equipment);

	@Override
	public String getName() {
		return "server";
	}

	private static class Comments {
		static String recipes = "Packmakers' control panel for internal recipe compat";
		static String kinetics = "Parameters and abilities of Create: Dream n' Desire's kinetic mechanisms";
		static String logistics = "Tweaks for logistical components";
		static String equipment = "Equipment and gadgets added by Create: Dream n' Desire";
		static String infrastructure = "The Backbone of Create: Dream n' Desire";

		static String chanceForOreStone = "Chance for and Ore Stone to spawn when on top of Bedrock while Milkshake Stone Generating";
		static String chanceForArtificialOreStone = "Chance for and Ore Stone to spawn when on top of Artificial Bedrock while Milkshake Stone Generating";
	}

}
