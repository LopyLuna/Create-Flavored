package uwu.lopyluna.create_dd.infrastructure.tabs;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.registry.ClassicItems;

public class ClassicCreativeModeTab extends DesireCreativeModeTab {
	public ClassicCreativeModeTab() {
		super("classic");
	}

	@Override
	@NotNull
	public ItemStack makeIcon() {
		return ClassicItems.integrated_circuit.asStack();
	}
}
