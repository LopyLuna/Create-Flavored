package uwu.lopyluna.create_dd.registry;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.foundation.utility.Couple;
import uwu.lopyluna.create_dd.DesireUtil;

import static com.simibubi.create.AllPartialModels.FOLDING_DOORS;

@SuppressWarnings({"unused"})
public class DesiresPartialModels {

	public static final PartialModel

		EMPTY = block("empty"),

		INDUSTRIAL_FAN_POWER = block("industrial_fan/cog"),
		INDUSTRIAL_FAN_INNER = block("industrial_fan/propeller"),

		COG_CRANK_HANDLE = block("cog_crank/handle"),

		HYDRAULIC_PRESS_HEAD = block("hydraulic_press/head"),

		TOP_BRASS_PANEL = block("brass_gearbox/panels/top"),
		BOTTOM_BRASS_PANEL = block("brass_gearbox/panels/bottom"),
		NORTH_BRASS_PANEL = block("brass_gearbox/panels/north"),
		EAST_BRASS_PANEL = block("brass_gearbox/panels/east"),
		SOUTH_BRASS_PANEL = block("brass_gearbox/panels/south"),
		WEST_BRASS_PANEL = block("brass_gearbox/panels/west"),

		//LUMBER_SAW_BLADE_HORIZONTAL_ACTIVE = block("lumber_saw/blade_horizontal_active"),
		//LUMBER_SAW_BLADE_HORIZONTAL_INACTIVE = block("lumber_saw/blade_horizontal_inactive"),
		//LUMBER_SAW_BLADE_HORIZONTAL_REVERSED = block("lumber_saw/blade_horizontal_reversed"),
		//LUMBER_SAW_BLADE_VERTICAL_ACTIVE = block("lumber_saw/blade_vertical_active"),
		//LUMBER_SAW_BLADE_VERTICAL_INACTIVE = block("lumber_saw/blade_vertical_inactive"),
		//LUMBER_SAW_BLADE_VERTICAL_REVERSED = block("lumber_saw/blade_vertical_reversed"),
		//WEAKENED_HARVESTER_BLADE = block("weakened_harvester/blade"),
	
		ENGINE_PISTON = block("furnace_engine/piston"),
		ENGINE_LINKAGE = block("furnace_engine/linkage"),
		ENGINE_CONNECTOR = block("furnace_engine/shaft_connector")
	;

	static {
		putFoldingDoor("rose");
		putFoldingDoor("smoked");
		putFoldingDoor("spirit");
	}

	public static void putFoldingDoor(String id) {
		FOLDING_DOORS.put(DesireUtil.asResource(id + "_door"),
				Couple.create(block(id + "_door/fold_left"), block(id + "_door/fold_right")));
	}

	private static PartialModel block(String path) {
		return new PartialModel(DesireUtil.asResource("block/" + path));
	}

	//private static PartialModel entity(String path) {
	//	return new PartialModel(DesireUtil.asResource("entity/" + path))
	//}

	public static void init() {
		// init static fields
	}

}
