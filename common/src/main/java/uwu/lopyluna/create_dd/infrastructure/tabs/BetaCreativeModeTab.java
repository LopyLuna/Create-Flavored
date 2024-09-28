package uwu.lopyluna.create_dd.infrastructure.tabs;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;

public class BetaCreativeModeTab extends DesireCreativeModeTab {
	public BetaCreativeModeTab() {
		super("beta");
	}

	@Override
	@NotNull
	public ItemStack makeIcon() {
		return DesiresBlocks.CREATIVE_CASING.asStack();
	}
}
