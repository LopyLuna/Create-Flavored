package uwu.lopyluna.create_dd.infrastructure.config;

import com.simibubi.create.foundation.config.ConfigBase;

@SuppressWarnings({"unused"})
public class DKinetics extends ConfigBase {

	public final ConfigFloat cogCrankHungerMultiplier = f(.01f, 0, 1, "cogCrankHungerMultiplier", Comments.cogCrankHungerMultiplier);

	public final ConfigGroup fan = group(1, "industrialFan", "Industrial Fan");
	public final ConfigInt fanPushDistance = i(40, 5, "fanPushDistance", Comments.fanPushDistance);
	public final ConfigInt fanPullDistance = i(40, 5, "fanPullDistance", Comments.fanPullDistance);
	public final ConfigInt fanRotationArgmax = i(256, 64, "fanRotationArgmax", Comments.rpm, Comments.fanRotationArgmax);
	//public final ConfigInt fanBlockCheckRate = i(30, 10, "fanBlockCheckRate", Comments.fanBlockCheckRate);*
	//public final ConfigInt fanProcessingTime = i(75, 0, "fanProcessingTime", Comments.fanProcessingTime);*

	//public final ConfigInt maxRubberBeltLength = i(32, 2, "maxRubberBeltLength", Comments.maxRubberBeltLength);*

	//public final ConfigBool weakenedHarvestPartiallyGrown = b(true, "weakenedHarvestPartiallyGrown", Comments.weakenedHarvestPartiallyGrown);*
	//public final ConfigBool weakenedHarvesterReplants = b(false, "weakenedHarvesterReplants", Comments.weakenedHarvesterReplants);*

	public final ConfigGroup stats = group(1, "stats", Comments.stats);

	public final DStress stressValues = nested(1, DStress::new, Comments.stress);

	@Override
	public String getName() {
		return "kinetics";
	}

	private static class Comments {
		static String fanPushDistance = "Maximum distance in blocks Fans can push entities.";
		static String fanPullDistance = "Maximum distance in blocks from where Fans can pull entities.";
		static String fanBlockCheckRate = "Game ticks between Fans checking for anything blocking their air flow.";
		static String fanRotationArgmax = "Rotation speed at which the maximum stats of fans are reached.";
		static String fanProcessingTime = "Game ticks required for a Fan-based processing recipe to take effect.";

		static String maxRubberBeltLength = "Maximum length in blocks of Rubber Belts.";
		static String cogCrankHungerMultiplier = "multiplier used for calculating exhaustion from speed when a Cog Crank is turned.";
		static String weakenedHarvestPartiallyGrown = "Whether Weakened Harvesters should break crops that aren't fully grown.";
		static String weakenedHarvesterReplants = "Whether Weakened Harvesters should replant crops after harvesting.";
		static String stats = "Configure speed/capacity levels for requirements and indicators.";
		static String rpm = "[in Revolutions per Minute]";
		static String stress = "Fine tune the kinetic stats of individual components";
	}

}
