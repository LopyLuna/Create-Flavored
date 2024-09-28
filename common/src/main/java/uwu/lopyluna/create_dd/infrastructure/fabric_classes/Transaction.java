package uwu.lopyluna.create_dd.infrastructure.fabric_classes;

import org.jetbrains.annotations.Nullable;

public interface Transaction extends AutoCloseable, TransactionContext {
    /**
     * Open a new outer transaction.
     *
     * @throws IllegalStateException If a transaction is already active on the current thread.
     */
    static Transaction openOuter() {
        return TransactionManagerImpl.MANAGERS.get().openOuter();
    }

    /**
     * @return True if a transaction is open or closing on the current thread, and false otherwise.
     */
    static boolean isOpen() {
        return getLifecycle() != Transaction.Lifecycle.NONE;
    }

    /**
     * @return The current lifecycle of the transaction stack on this thread.
     */
    static Transaction.Lifecycle getLifecycle() {
        return TransactionManagerImpl.MANAGERS.get().getLifecycle();
    }

    /**
     * Open a nested transaction if {@code maybeParent} is non-null, or an outer transaction if {@code maybeParent} is null.
     */
    static Transaction openNested(@Nullable TransactionContext maybeParent) {
        return maybeParent == null ? openOuter() : maybeParent.openNested();
    }

    /**
     * Retrieve the currently open transaction, or null if there is none.
     *
     * <p><b>Usage of this function is strongly discouraged</b>, this is why it is deprecated and contains {@code unsafe} in its name.
     * The transaction may be aborted unbeknownst to you and anything you think that you have committed might be undone.
     * Only use it if you have no way to pass the transaction down the stack, for example if you are implementing compat with a simulation-based API,
     * and you know what you are doing, for example because you opened the outer transaction.
     *
     * @throws IllegalStateException If called from a close or outer close callback.
     * @deprecated Only use if you absolutely need it, there is almost always a better way.
     */
    @Deprecated
    @Nullable
    static TransactionContext getCurrentUnsafe() {
        return TransactionManagerImpl.MANAGERS.get().getCurrentUnsafe();
    }

    /**
     * Close the current transaction, rolling back all the changes that happened during this transaction and
     * the transactions opened with {@link #openNested} from this transaction.
     *
     * @throws IllegalStateException If this function is not called on the thread this transaction was opened in.
     * @throws IllegalStateException If this transaction is not the current transaction.
     * @throws IllegalStateException If this transaction was closed.
     */
    void abort();

    /**
     * Close the current transaction, committing all the changes that happened during this transaction and
     * the <b>committed</b> transactions opened with {@link #openNested} from this transaction.
     * If this transaction was opened with {@link #openOuter}, all changes are applied.
     * If this transaction was opened with {@link #openNested}, all changes will be applied when and if the changes of
     * the parent transactions are applied.
     *
     * @throws IllegalStateException If this function is not called on the thread this transaction was opened in.
     * @throws IllegalStateException If this transaction is not the current transaction.
     * @throws IllegalStateException If this transaction was closed.
     */
    void commit();

    /**
     * Abort the current transaction if it was not closed already.
     */
    @Override
    void close();

    enum Lifecycle {
        /**
         * No transaction is currently open or closing.
         */
        NONE,
        /**
         * A transaction is currently open.
         */
        OPEN,
        /**
         * The current transaction is invoking its close callbacks.
         */
        CLOSING,
        /**
         * The current transaction is invoking its outer close callbacks.
         */
        OUTER_CLOSING
    }
}
