package uwu.lopyluna.create_dd.infrastructure.tabs;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.registry.DesiresPaletteBlocks;

public class PalettesCreativeModeTab extends DesireCreativeModeTab {
	public PalettesCreativeModeTab() {
		super("palettes");
	}

	@Override
	public void addItems(NonNullList<ItemStack> items, boolean specialItems) {
	}

	@Override
	@NotNull
	public ItemStack makeIcon() {
		return DesiresPaletteBlocks.LIGHT_BLUE_BLUEPRINT_BLOCK.asStack();
	}
}
