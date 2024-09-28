package uwu.lopyluna.create_dd.infrastructure.porting_lib_classes;

import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.create_dd.infrastructure.fabric_classes.ResourceAmount;
import uwu.lopyluna.create_dd.infrastructure.fabric_classes.Storage;
import uwu.lopyluna.create_dd.infrastructure.fabric_classes.StorageView;
import uwu.lopyluna.create_dd.infrastructure.fabric_classes.TransactionContext;

import java.util.Iterator;
import java.util.function.Predicate;

public interface ExtendedStorage<T> extends Storage<T> {
	/**
	 * Extract the first thing from this storage that matches the given predicate, or null if none available.
	 */
	@Nullable
    ResourceAmount<T> extractMatching(Predicate<T> predicate, long maxAmount, TransactionContext transaction);

	/**
	 * Extract anything from this storage, or null if empty.
	 */
	@Nullable
	default ResourceAmount<T> extractAny(long maxAmount, TransactionContext transaction) {
		return extractMatching($ -> true, maxAmount, transaction);
	}

	/**
	 * @return an iterator of only StorageViews that are not empty.
	 */
	Iterator<? extends StorageView<T>> nonEmptyViews();

	@SuppressWarnings("unchecked, rawtypes")
	default Iterable<? extends StorageView<T>> nonEmptyIterable() {
		return () -> (Iterator) nonEmptyViews();
	}
}
