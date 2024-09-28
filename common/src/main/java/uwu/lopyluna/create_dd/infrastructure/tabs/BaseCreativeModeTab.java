package uwu.lopyluna.create_dd.infrastructure.tabs;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;

public class BaseCreativeModeTab extends DesireCreativeModeTab {
	public BaseCreativeModeTab() {
		super("base");
	}

	@Override
	@NotNull
	public ItemStack makeIcon() {
		return DesiresBlocks.COG_CRANK.asStack();
	}
}
