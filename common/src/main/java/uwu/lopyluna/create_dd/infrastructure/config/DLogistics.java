package uwu.lopyluna.create_dd.infrastructure.config;

import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class DLogistics extends ConfigBase {

//	public final ConfigInt mechanicalArmRange = i(5, 1, "mechanicalArmRange", Comments.mechanicalArmRange);*
//	public final ConfigInt calculationLinkRange = i(512, 1, "calculationLinkRange", Comments.calculationLinkRange);*
	public final ConfigInt verticalVaultCapacity = i(20, 1, "verticalVaultCapacity", Comments.verticalVaultCapacity);
	public final ConfigInt burdenChuteStackSize = i(32, 1, Item.MAX_STACK_SIZE, "burdenChuteStackSize", Comments.burdenChuteStackSize);
	public final ConfigFloat burdenChuteSpeedMultiplier = f(1.5f,0.0f, 20.0f, "burdenChuteSpeedMultiplier", Comments.burdenChuteSpeedMultiplier);

	@Override
	public String getName() {
		return "logistics";
	}

	private static class Comments {
		static String defaultExtractionTimer = "The amount of ticks a funnel waits between item transferrals, when it is not re-activated by redstone.";
		static String calculationLinkRange = "Maximum possible range in blocks of Calculation Link connections.";
		static String mechanicalArmRange = "Maximum distance in blocks a Mechanical Arm can reach across.";
		static String verticalVaultCapacity = "The total amount of stacks a vault can hold per block in size.";
		static String burdenChuteSpeedMultiplier = "The total speed can the item travel through the Burden Chute.";
		static String burdenChuteStackSize = "The total amount of items that can go through the Burden Chute.";
	}

}
