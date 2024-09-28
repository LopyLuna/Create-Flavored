package uwu.lopyluna.create_dd.infrastructure.porting_lib_classes;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.infrastructure.fabric_classes.ItemVariant;
import uwu.lopyluna.create_dd.infrastructure.fabric_classes.Storage;
import uwu.lopyluna.create_dd.infrastructure.fabric_classes.TransactionContext;

public interface SlotExposedStorage extends Storage<ItemVariant> {
	ItemStack getStackInSlot(int slot);
	int getSlots();
	int getSlotLimit(int slot);
	default void setStackInSlot(int slot, @NotNull ItemStack stack) {

	}
	boolean isItemValid(int slot, ItemVariant resource, long amount);

	default long insertSlot(int slot, ItemVariant resource, long maxAmount, TransactionContext transaction) {
		Desires.LOGGER.warn("Tried to insert to a slotted storage without a implementation! Implementations will be forced to implement this in 1.19.3");
		if (isItemValid(slot, resource, maxAmount))
			return insert(resource, maxAmount, transaction);
		return 0;
	}

	default long extractSlot(int slot, ItemVariant resource, long maxAmount, TransactionContext transaction) {
        Desires.LOGGER.warn("Tried to extract from a slotted storage without a implementation! Implementations will be forced to implement this in 1.19.3");
		return extract(resource, maxAmount, transaction);
	}
}
