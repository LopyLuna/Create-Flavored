package uwu.lopyluna.create_dd.infrastructure.ponder;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;
import uwu.lopyluna.create_dd.DesireUtil;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;

@SuppressWarnings({"unused", "SameParameterValue"})
public class DesirePonderTags {

	public static final PonderTag
		ALL_DESIRES_CONTENT = create("create_dd").item(AllItems.PRECISION_MECHANISM.get())
			.defaultLang("Create: Dreams n' Desires", "Welcome to the mod!")
			.addToIndex();

	private static PonderTag create(String id) {
		return new PonderTag(DesireUtil.asResource(id));
	}

	public static void register() {
		// Add items to tags here
		PonderRegistry.TAGS.forTag(ALL_DESIRES_CONTENT).add(DesiresBlocks.COG_CRANK.get());
	}

}
