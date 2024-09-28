package uwu.lopyluna.create_dd.infrastructure.fabric_classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class SnapshotParticipant<T> implements Transaction.CloseCallback, Transaction.OuterCloseCallback {
    private final List<T> snapshots = new ArrayList<>();

    /**
     * Return a new <b>nonnull</b> object containing the current state of this participant.
     * <b>{@code null} may not be returned, or an exception will be thrown!</b>
     */
    protected abstract T createSnapshot();

    /**
     * Roll back to a state previously created by {@link #createSnapshot}.
     */
    protected abstract void readSnapshot(T snapshot);

    /**
     * Signals that the snapshot will not be used anymore, and is safe to cache for next calls to {@link #createSnapshot},
     * or discard entirely.
     */
    protected void releaseSnapshot(T snapshot) {
    }

    /**
     * Called after an outer transaction succeeded,
     * to perform irreversible actions such as {@code markDirty()} or neighbor updates.
     */
    protected void onFinalCommit() {
    }

    /**
     * Update the stored snapshots so that the changes happening as part of the passed transaction can be correctly
     * committed or rolled back.
     * This function should be called every time the participant is about to change its internal state as part of a transaction.
     */
    public void updateSnapshots(TransactionContext transaction) {
        // Make sure we have enough storage for snapshots
        while (snapshots.size() <= transaction.nestingDepth()) {
            snapshots.add(null);
        }

        // If the snapshot is null, we need to create it, and we need to register a callback.
        if (snapshots.get(transaction.nestingDepth()) == null) {
            T snapshot = createSnapshot();
            Objects.requireNonNull(snapshot, "Snapshot may not be null!");

            snapshots.set(transaction.nestingDepth(), snapshot);
            transaction.addCloseCallback(this);
        }
    }

    @Override
    public void onClose(TransactionContext transaction, Transaction.Result result) {
        // Get and remove the relevant snapshot.
        T snapshot = snapshots.set(transaction.nestingDepth(), null);

        if (result.wasAborted()) {
            // If the transaction was aborted, we just revert to the state of the snapshot.
            readSnapshot(snapshot);
            releaseSnapshot(snapshot);
        } else if (transaction.nestingDepth() > 0) {
            if (snapshots.get(transaction.nestingDepth() - 1) == null) {
                // No snapshot yet, so move the snapshot one nesting level up.
                snapshots.set(transaction.nestingDepth() - 1, snapshot);
                // This is the first snapshot at this level: we need to call addCloseCallback.
                transaction.getOpenTransaction(transaction.nestingDepth() - 1).addCloseCallback(this);
            } else {
                // There is already an older snapshot at the nesting level above, just release the newer one.
                releaseSnapshot(snapshot);
            }
        } else {
            releaseSnapshot(snapshot);
            transaction.addOuterCloseCallback(this);
        }
    }

    @Override
    public void afterOuterClose(Transaction.Result result) {
        // The result is guaranteed to be COMMITTED,
        // as this is only scheduled during onClose() when the outer transaction is successful.
        onFinalCommit();
    }
}
