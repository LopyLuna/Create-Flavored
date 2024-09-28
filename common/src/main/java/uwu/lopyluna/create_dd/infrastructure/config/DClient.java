package uwu.lopyluna.create_dd.infrastructure.config;

import com.simibubi.create.foundation.config.ConfigBase;

import static uwu.lopyluna.create_dd.Desires.NAME;

@SuppressWarnings({"unused"})
public class DClient extends ConfigBase {

	public final ConfigGroup client = group(0, "client",
			Comments.client);
	
	public final ConfigGroup configMenu = group(1, "configMenu", Comments.configMenu);
	public final ConfigInt mainMenuConfigButtonRow = i(2, 0, 4, "mainMenuConfigButtonRow",
			Comments.mainMenuConfigButtonRow);
	public final ConfigInt mainMenuConfigButtonOffsetX = i(4, Integer.MIN_VALUE, Integer.MAX_VALUE, "mainMenuConfigButtonOffsetX",
			Comments.mainMenuConfigButtonOffsetX);
	public final ConfigInt ingameMenuConfigButtonRow = i(3, 0, 5, "ingameMenuConfigButtonRow",
			Comments.ingameMenuConfigButtonRow);
	public final ConfigInt ingameMenuConfigButtonOffsetX = i(4, Integer.MIN_VALUE, Integer.MAX_VALUE, "ingameMenuConfigButtonOffsetX",
			Comments.ingameMenuConfigButtonOffsetX);

	public final ConfigGroup equipments = group(1, "equipments", Comments.equipments);
	public final ConfigBool invertDeforesterSawFunction = b(false, "invertDeforesterSawFunction",
			Comments.invertDeforesterSawFunction);
	public final ConfigBool invertExcavationDrillFunction = b(false, "invertExcavationDrillFunction",
			Comments.invertExcavationDrillFunction);
	public final ConfigBool disableBlocksVoidZapperMessage = b(false, "disableBlocksVoidZapperMessage",
			Comments.disableBlocksVoidZapperMessage);


	public final ConfigBool equipmentsDebug = b(false, "equipmentsDebug",
			Comments.equipmentsDebug);

	// custom fluid fog
	public final ConfigGroup fluidFogSettings = group(1, "fluidFogSettings", Comments.fluidFogSettings);
	public final ConfigFloat sapTransparencyMultiplier =
			f(.125f, .125f, 128, "sap", Comments.sapTransparencyMultiplier);
	public final ConfigFloat chocolateTransparencyMultiplier =
			f(.125f, .125f, 128, "chocolateTransparencyMultiplier", Comments.chocolateTransparencyMultiplier);
	public final ConfigFloat vanillaTransparencyMultiplier =
			f(.125f, .125f, 128, "vanillaTransparencyMultiplier", Comments.vanillaTransparencyMultiplier);
	public final ConfigFloat glowberryTransparencyMultiplier =
			f(.125f, .125f, 128, "glowberryTransparencyMultiplier", Comments.glowberryTransparencyMultiplier);
	public final ConfigFloat strawberryTransparencyMultiplier =
			f(.125f, .125f, 128, "strawberryTransparencyMultiplier", Comments.strawberryTransparencyMultiplier);
	public final ConfigFloat pumpkinTransparencyMultiplier =
			f(.125f, .125f, 128, "pumpkinTransparencyMultiplier", Comments.pumpkinTransparencyMultiplier);

	//ponder group
	public final ConfigGroup ponder = group(1, "ponder",
			Comments.ponder);

	@Override
	public String getName() {
		return "client";
	}

	private static class Comments {
		static String client = "Client-only settings - If you're looking for general settings, look inside your worlds serverconfig folder!";

		static String configMenu = "Configure Config Menu";
		static String[] mainMenuConfigButtonRow = new String[]{
				"Choose the menu row that the " + NAME + " config button appears on in the main menu",
				"Set to 0 to disable the button altogether"
		};
		static String[] mainMenuConfigButtonOffsetX = new String[]{
				"Offset the " + NAME + " config button in the main menu by this many pixels on the X axis",
				"The sign (-/+) of this value determines what side of the row the button appears on (left/right)"
		};
		static String[] ingameMenuConfigButtonRow = new String[]{
				"Choose the menu row that the " + NAME + " config button appears on in the in-game menu",
				"Set to 0 to disable the button altogether"
		};
		static String[] ingameMenuConfigButtonOffsetX = new String[]{
				"Offset the " + NAME + " config button in the in-game menu by this many pixels on the X axis",
				"The sign (-/+) of this value determines what side of the row the button appears on (left/right)"
		};


		static String equipments = "Configure Equipment settings";
		static String invertDeforesterSawFunction = "Invert Deforester Saw activation function";
		static String invertExcavationDrillFunction = "Invert Excavation Drill activation function";
		static String disableBlocksVoidZapperMessage = "Disables the blocks will be voided warning for the Block Zapper";

		static String equipmentsDebug = "Debug Equipments (only works in Creative mode)";

		static String fluidFogSettings = "Configure your vision range when submerged in Create Dream n' Desire's custom fluids";
		static String sapTransparencyMultiplier = "The vision range through Sap will be multiplied by this factor";
		static String chocolateTransparencyMultiplier = "The vision range through Chocolate Milkshake will be multiplied by this factor";
		static String vanillaTransparencyMultiplier = "The vision range through Vanilla Milkshake will be multiplied by this factor";
		static String glowberryTransparencyMultiplier = "The vision range through Glowberry Milkshake will be multiplied by this factor";
		static String strawberryTransparencyMultiplier = "The vision range through Strawberry Milkshake will be multiplied by this factor";
		static String pumpkinTransparencyMultiplier = "The vision range through Pumpkin Milkshake will be multiplied by this factor";


		static String ponder = "Ponder settings";
	}

}
