package uwu.lopyluna.create_dd.infrastructure.config;

import com.simibubi.create.foundation.config.ConfigBase;

@SuppressWarnings({"unused"})
public class DEquipment extends ConfigBase {
	//Plans to change this
	//public final ConfigInt maxSymmetryWandRange = i(50, 10, "maxSymmetryWandRange", Comments.symmetryRange);*

	public final ConfigInt excavationDrillMaximumBlocks = i(64, "excavationDrillMaximumBlocks", Comments.excavationDrillMaximumBlocks);
	public final ConfigFloat handheldNozzleRangePlayer = f(8, 1, 512, "handheldNozzleRangePlayer", Comments.handheldNozzleRangePlayer);
	public final ConfigFloat handheldNozzleRange = f(16, 1, 512, "handheldNozzleRange", Comments.handheldNozzleRange);
	public final ConfigFloat handheldNozzleSpeedLimit = f(2, 0, 512, "handheldNozzleSpeedLimit", Comments.handheldNozzleRange);

//	public final ConfigInt deforestingSawMaximumBlocks = i(128, "deforestingSawMaximumBlocks", Comments.deforestingSawMaximumBlocks);*
//	public final ConfigInt backpackRange = i(4, 1, "backpackRange", Comments.backpackRange);*
//	public final ConfigInt zapperUndoLogLength = i(5, 0, "zapperUndoLogLength", Comments.zapperUndoLogLength);

	@Override
	public String getName() {
		return "equipment";
	}

	private static class Comments {
		//static String symmetryRange = "The Maximum Distance to an active mirror for the symmetry wand to trigger.";
		static String handheldNozzleRange =
				"The Maximum Distance at which a Hand Held Nozzle can push/pull Entities.";
		static String handheldNozzleRangePlayer =
				"The Maximum Distance at which a Hand Held Nozzle can push/pull your Player.";
		static String excavationDrillMaximumBlocks =
				"The Maximum Amount of Blocks a Excavation Drill can Handle.";
		static String deforestingSawMaximumBlocks =
				"The Maximum Amount of Blocks a Deforesting Saw can Handle.";
		static String backpackRange =
				"The Maximum Distance at which a Backpack can interact with Players' Inventories.";
		static String zapperUndoLogLength =
				"The Maximum Amount of operations a Block Zapper can remember for undoing. (0 to disable undo)";
	}

}
