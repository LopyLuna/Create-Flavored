package uwu.lopyluna.create_dd.registry;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import com.simibubi.create.foundation.utility.Couple;
import uwu.lopyluna.create_dd.DesireUtil;


@SuppressWarnings({"unused"})
public class DesiresSpriteShifts {

	public static final Couple<CTSpriteShiftEntry>
			SOCKPILE_SIDE = stockpile("side"),
			SOCKPILE_TOP = stockpile("top"),
			SOCKPILE_BOTTOM = stockpile("bottom");


	public static final Couple<CTSpriteShiftEntry>
			KEG_TOP = reservoir("top"),
			KEG_FRONT = reservoir("front"),
			KEG_SIDE = reservoir("side"),
			KEG_SIDE_WINDOW = reservoir("side_window"),
			KEG_BOTTOM = reservoir("bottom");

	//public static final CTSpriteShiftEntry*
	//	HYDRAULIC_SCAFFOLD = horizontal("scaffold/hydraulic_scaffold");

	//public static final CTSpriteShiftEntry*
	//	HYDRAULIC_SCAFFOLD_INSIDE = horizontal("scaffold/hydraulic_scaffold_inside");

	//public static final CTSpriteShiftEntry*
	//	ORNATE_IRON_GLASS = getCT(AllCTTypes.OMNIDIRECTIONAL, "palettes/ornate_iron_glass", "palettes/ornate_iron_glass"),
	//	ORNATE_IRON_GLASS_SIDE = getCT(AllCTTypes.OMNIDIRECTIONAL, "palettes/ornate_iron_glass_side", "palettes/ornate_iron_glass_side");

	public static final CTSpriteShiftEntry
			HAZARD_BLOCK = omni("hazard_block"),
			DARK_METAL_PLATING = omni("dark_metal_plating"),
			CREATIVE_CASING = omni("creative_casing"),
			OVERBURDEN_CASING = omni("overburden_casing"),
			INDUSTRIAL_CASING = omni("industrial_casing"),
			HYDRAULIC_CASING = omni("hydraulic_casing");

	public static final CTSpriteShiftEntry
			CREATIVE_CASING_COGWHEEL_SIDE = vertical("creative_encased_cogwheel_side"),
			OVERBURDEN_CASING_COGWHEEL_SIDE = vertical("overburden_encased_cogwheel_side"),
			INDUSTRIAL_CASING_COGWHEEL_SIDE = vertical("industrial_encased_cogwheel_side"),
			HYDRAULIC_CASING_COGWHEEL_SIDE = vertical("hydraulic_encased_cogwheel_side");

	public static final CTSpriteShiftEntry
			CREATIVE_CASING_COGWHEEL_OTHERSIDE = horizontal("creative_encased_cogwheel_side"),
			OVERBURDEN_CASING_COGWHEEL_OTHERSIDE = horizontal("overburden_encased_cogwheel_side"),
			INDUSTRIAL_CASING_COGWHEEL_OTHERSIDE = horizontal("industrial_encased_cogwheel_side"),
			HYDRAULIC_CASING_COGWHEEL_OTHERSIDE = horizontal("hydraulic_encased_cogwheel_side");

	public static final CTSpriteShiftEntry
			 BLACK_BLUEPRINT_BLOCK = omniBlueprint("black"),
			 WHITE_BLUEPRINT_BLOCK = omniBlueprint("white"),
			 BLUE_BLUEPRINT_BLOCK = omni("blueprint_block"),
			 LIGHT_BLUE_BLUEPRINT_BLOCK = omniBlueprint("light"),
			 RED_BLUEPRINT_BLOCK = omniBlueprint("red"),
			 GREEN_BLUEPRINT_BLOCK = omniBlueprint("green"),
			 LIME_BLUEPRINT_BLOCK = omniBlueprint("lime"),
			 PINK_BLUEPRINT_BLOCK = omniBlueprint("pink"),
			 MAGENTA_BLUEPRINT_BLOCK = omniBlueprint("magenta"),
			 YELLOW_BLUEPRINT_BLOCK = omniBlueprint("yellow"),
			 GRAY_BLUEPRINT_BLOCK = omniBlueprint("gray"),
			 LIGHT_GRAY_BLUEPRINT_BLOCK = omniBlueprint("light_gray"),
			 BROWN_BLUEPRINT_BLOCK = omniBlueprint("brown"),
			 CYAN_BLUEPRINT_BLOCK = omniBlueprint("cyan"),
			 PURPLE_BLUEPRINT_BLOCK = omniBlueprint("purple"),
			 ORANGE_BLUEPRINT_BLOCK = omniBlueprint("orange");

	//public static final Map<DyeColor, SpriteShiftEntry> DYED_RUBBER_BELTS = new EnumMap<>(DyeColor.class),
	//		DYED_OFFSET_RUBBER_BELTS = new EnumMap<>(DyeColor.class), DYED_DIAGONAL_RUBBER_BELTS = new EnumMap<>(DyeColor.class);

	//public static final SpriteShiftEntry
	//	RUBBER_BELT = get("block/belt", "block/rubber_belt_scroll"),
	//	RUBBER_BELT_OFFSET = get("block/rubber_belt_offset", "block/rubber_belt_scroll"),
	//	RUBBER_BELT_DIAGONAL = get("block/rubber_belt_diagonal", "block/rubber_belt_diagonal_scroll"),
	//	ANDESITE_RUBBER_BELT_CASING = get("block/belt/andesite_rubber_belt_casing", "block/belt/andesite_rubber_belt_casing"),
	//	BRASS_RUBBER_BELT_CASING = get("block/belt/brass_rubber_belt_casing", "block/belt/brass_rubber_belt_casing"),
	//	INDUSTRIAL_RUBBER_BELT_CASING = get("block/belt/industrial_rubber_belt_casing", "block/belt/industrial_rubber_belt_casing");

	//static {
	//	populateMaps();
	//}
//
	//private static void populateMaps() {
	//	for (DyeColor color : DyeColor.values()) {
	//		String id = color.getSerializedName();
	//		DYED_RUBBER_BELTS.put(color, get("block/rubber_belt", "block/rubber_belt/" + id + "_scroll"));
	//		DYED_OFFSET_RUBBER_BELTS.put(color, get("block/rubber_belt_offset", "block/rubber_belt/" + id + "_scroll"));
	//		DYED_DIAGONAL_RUBBER_BELTS.put(color, get("block/rubber_belt_diagonal", "block/rubber_belt/" + id + "_diagonal_scroll"));
	//	}
	//}

	private static Couple<CTSpriteShiftEntry> stockpile(String name) {
		final String prefixed = "block/stockpile/stockpile_" + name;
		return Couple.createWithContext(
				medium -> CTSpriteShifter.getCT(AllCTTypes.RECTANGLE, DesireUtil.asResource(prefixed + "_small"),
						DesireUtil.asResource(medium ? prefixed + "_medium" : prefixed + "_large")));
	}

	private static Couple<CTSpriteShiftEntry> reservoir(String name) {
		final String prefixed = "block/reservoir/reservoir_" + name;
		return Couple.createWithContext(
				medium -> CTSpriteShifter.getCT(AllCTTypes.RECTANGLE, DesireUtil.asResource(prefixed + "_small"),
						DesireUtil.asResource(medium ? prefixed + "_medium" : prefixed + "_large")));
	}

	private static CTSpriteShiftEntry omniBlueprint(String name) {
		return getCT(AllCTTypes.OMNIDIRECTIONAL, name + "_blueprint_block");
	}

	private static CTSpriteShiftEntry omni(String name) {
		return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
	}

	private static CTSpriteShiftEntry horizontal(String name) {
		return getCT(AllCTTypes.HORIZONTAL, name);
	}

	private static CTSpriteShiftEntry vertical(String name) {
		return getCT(AllCTTypes.VERTICAL, name);
	}

	private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
		return CTSpriteShifter.getCT(type, DesireUtil.asResource("block/" + blockTextureName), DesireUtil.asResource("block/" + connectedTextureName + "_connected"));
	}

	private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
		return getCT(type, blockTextureName, blockTextureName);
	}

	public static void register(){}
}
