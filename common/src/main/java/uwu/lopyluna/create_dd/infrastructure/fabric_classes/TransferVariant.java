package uwu.lopyluna.create_dd.infrastructure.fabric_classes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface TransferVariant<O> {
    /**
     * Return true if this variant is blank, and false otherwise.
     */
    boolean isBlank();

    /**
     * Return the immutable object instance of this variant.
     */
    O getObject();

    /**
     * Return the underlying tag.
     *
     * <p><b>NEVER MUTATE THIS NBT TAG</b>, if you need to mutate it you can use {@link #copyNbt()} to retrieve a copy instead.
     */
    @Nullable
    CompoundTag getNbt();

    /**
     * Return true if this variant has a tag, false otherwise.
     */
    default boolean hasNbt() {
        return getNbt() != null;
    }

    /**
     * Return true if the tag of this variant matches the passed tag, and false otherwise.
     *
     * <p>Note: True is returned if both tags are {@code null}.
     */
    default boolean nbtMatches(@Nullable CompoundTag other) {
        return Objects.equals(getNbt(), other);
    }

    /**
     * Return {@code true} if the object of this variant matches the passed fluid.
     */
    default boolean isOf(O object) {
        return getObject() == object;
    }

    /**
     * Return a copy of the tag of this variant, or {@code null} if this variant doesn't have a tag.
     *
     * <p>Note: Use {@link #nbtMatches} if you only need to check for custom tag equality, or {@link #getNbt()} if you don't need to mutate the tag.
     */
    @Nullable
    default CompoundTag copyNbt() {
        CompoundTag nbt = getNbt();
        return nbt == null ? null : nbt.copy();
    }

    /**
     * Return a copy of the tag of this variant, or a new compound if this variant doesn't have a tag.
     */
    default CompoundTag copyOrCreateNbt() {
        CompoundTag nbt = getNbt();
        return nbt == null ? new CompoundTag() : nbt.copy();
    }

    /**
     * Save this variant into an NBT compound tag. Subinterfaces should have a matching static {@code fromNbt}.
     *
     * <p>Note: This is safe to use for persisting data as objects are saved using their full Identifier.
     */
    CompoundTag toNbt();

    /**
     * Write this variant into a packet byte buffer. Subinterfaces should have a matching static {@code fromPacket}.
     *
     * <p>Implementation note: Objects are saved using their raw registry integer id.
     */
    void toPacket(FriendlyByteBuf buf);
}
