package uwu.lopyluna.create_dd.infrastructure.fabric_classes;

public final class StoragePreconditions {
    /**
     * Ensure that the passed transfer variant is not blank.
     *
     * @throws IllegalArgumentException If the variant is blank.
     */
    public static void notBlank(TransferVariant<?> variant) {
        if (variant.isBlank()) {
            throw new IllegalArgumentException("Transfer variant may not be blank.");
        }
    }

    /**
     * Ensure that the passed amount is not negative. That is, it must be {@code >= 0}.
     *
     * @throws IllegalArgumentException If the amount is negative.
     */
    public static void notNegative(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount may not be negative, but it is: " + amount);
        }
    }

    /**
     * Check both for a not blank transfer variant and a not negative amount.
     */
    public static void notBlankNotNegative(TransferVariant<?> variant, long amount) {
        notBlank(variant);
        notNegative(amount);
    }

    private StoragePreconditions() {
    }
}
