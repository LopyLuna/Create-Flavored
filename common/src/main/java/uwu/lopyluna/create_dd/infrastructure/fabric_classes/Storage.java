package uwu.lopyluna.create_dd.infrastructure.fabric_classes;

import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public interface Storage<T> extends Iterable<StorageView<T>> {
    /**
     * Return an empty storage.
     */
    @SuppressWarnings("unchecked")
    static <T> Storage<T> empty() {
        return (Storage<T>) TransferApiImpl.EMPTY_STORAGE;
    }

    /**
     * Return false if calling {@link #insert} will absolutely always return 0, or true otherwise or in doubt.
     *
     * <p>Note: This function is meant to be used by pipes or other devices that can transfer resources to know if
     * they should interact with this storage at all.
     */
    default boolean supportsInsertion() {
        return true;
    }

    /**
     * Try to insert up to some amount of a resource into this storage.
     *
     * @param resource The resource to insert. May not be blank.
     * @param maxAmount The maximum amount of resource to insert. May not be negative.
     * @param transaction The transaction this operation is part of.
     * @return A non-negative integer not greater than maxAmount: the amount that was inserted.
     */
    long insert(T resource, long maxAmount, TransactionContext transaction);

    /**
     * Convenient helper to simulate an insertion, i.e. get the result of insert without modifying any state.
     * The passed transaction may be null if a new transaction should be opened for the simulation.
     * @see #insert
     */
    default long simulateInsert(T resource, long maxAmount, @Nullable TransactionContext transaction) {
        try (Transaction simulateTransaction = Transaction.openNested(transaction)) {
            return insert(resource, maxAmount, simulateTransaction);
        }
    }

    /**
     * Return false if calling {@link #extract} will absolutely always return 0, or true otherwise or in doubt.
     *
     * <p>Note: This function is meant to be used by pipes or other devices that can transfer resources to know if
     * they should interact with this storage at all.
     */
    default boolean supportsExtraction() {
        return true;
    }

    /**
     * Try to extract up to some amount of a resource from this storage.
     *
     * @param resource The resource to extract. May not be blank.
     * @param maxAmount The maximum amount of resource to extract. May not be negative.
     * @param transaction The transaction this operation is part of.
     * @return A non-negative integer not greater than maxAmount: the amount that was extracted.
     */
    long extract(T resource, long maxAmount, TransactionContext transaction);

    /**
     * Convenient helper to simulate an extraction, i.e. get the result of extract without modifying any state.
     * The passed transaction may be null if a new transaction should be opened for the simulation.
     * @see #extract
     */
    default long simulateExtract(T resource, long maxAmount, @Nullable TransactionContext transaction) {
        try (Transaction simulateTransaction = Transaction.openNested(transaction)) {
            return extract(resource, maxAmount, simulateTransaction);
        }
    }

    /**
     * Iterate through the contents of this storage.
     * Every visited {@link StorageView} represents a stored resource and an amount.
     * The iterator doesn't guarantee that a single resource only occurs once during an iteration.
     * Calling {@linkplain Iterator#remove remove} on the iterator is not allowed.
     *
     * <p>{@link #insert} and {@link #extract} may be called safely during iteration.
     * Extractions should be visible to an open iterator, but insertions are not required to.
     * In particular, inventories with a fixed amount of slots may wish to make insertions visible to iterators,
     * but inventories with a dynamic or very large amount of slots should not do that to ensure timely termination of
     * the iteration.
     *
     * @return An iterator over the contents of this storage. Calling remove on the iterator is not allowed.
     */
    @Override
    Iterator<StorageView<T>> iterator();

    /**
     * Return a view over this storage, for a specific resource, or {@code null} if none is quickly available.
     *
     * <p>This function should only return a non-null view if this storage can provide it quickly,
     * for example with a hashmap lookup.
     * If returning the requested view would require iteration through a potentially large number of views,
     * {@code null} should be returned instead.
     *
     * @param resource The resource for which a storage view is requested. May be blank, for example to estimate capacity.
     * @return A view over this storage for the passed resource, or {@code null} if none is quickly available.
     */
    @Nullable
    default StorageView<T> exactView(T resource) {
        return null;
    }

    /**
     * @deprecated Use and implement the overload without the transaction parameter.
     */
    @Deprecated(forRemoval = true)
    @Nullable
    default StorageView<T> exactView(TransactionContext transaction, T resource) {
        return exactView(resource);
    }

    /**
     * Return an integer representing the current version of this storage instance to allow for fast change detection:
     * if the version hasn't changed since the last time, <b>and the storage instance is the same</b>, the storage has the same contents.
     * This can be used to avoid re-scanning the contents of the storage, which could be an expensive operation.
     * It may be used like that:
     * <pre>{@code
     * // Store storage and version:
     * Storage<?> firstStorage = // ...
     * long firstVersion = firstStorage.getVersion();
     *
     * // Later, check if the secondStorage is the unchanged firstStorage:
     * Storage<?> secondStorage = // ...
     * long secondVersion = secondStorage.getVersion();
     * // We must check firstStorage == secondStorage first, otherwise versions may not be compared.
     * if (firstStorage == secondStorage && firstVersion == secondVersion) {
     *     // storage contents are the same.
     * } else {
     *     // storage contents might have changed.
     * }
     * }</pre>
     *
     * <p>The version <b>must</b> change if the state of the storage has changed,
     * generally after a direct modification, or at the end of a modifying transaction.
     * The version may also change even if the state of the storage hasn't changed.
     *
     * <p>It is not valid to call this during a transaction,
     * and implementations are encouraged to throw an exception if that happens.
     */
    default long getVersion() {
        if (Transaction.isOpen()) {
            throw new IllegalStateException("getVersion() may not be called during a transaction.");
        }

        return TransferApiImpl.version.getAndIncrement();
    }

    /**
     * Return a class instance of this interface with the desired generic type,
     * to be used for easier registration with API lookups.
     */
    @SuppressWarnings("unchecked")
    static <T> Class<Storage<T>> asClass() {
        return (Class<Storage<T>>) (Object) Storage.class;
    }
}
