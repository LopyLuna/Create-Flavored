package uwu.lopyluna.create_dd.infrastructure.registrate_classes;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.create_dd.access.AccessRegistryEntry;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RegistryEntry<T> implements NonNullSupplier<T> {
    private static RegistryEntry<?> EMPTY = new RegistryEntry(null, RegistryObject.empty());
    private final AbstractRegistrate<?> owner;
    private final @Nullable RegistryObject<T> delegate;

    @SuppressWarnings("unchecked")
    public static <T> RegistryEntry<T> empty() {
        return (RegistryEntry<T>) EMPTY;
    }

    public RegistryEntry(com.tterrag.registrate.util.entry.RegistryEntry<T> entry) {
        AccessRegistryEntry<T> access = (AccessRegistryEntry<T>) entry;

        owner = access.getOwner();
        delegate = access.getDelegate();
    }

    public RegistryEntry(AbstractRegistrate<?> owner, RegistryObject<T> delegate) {
        if (EMPTY != null && owner == null) {
            throw new NullPointerException("Owner must not be null");
        } else if (EMPTY != null && delegate == null) {
            throw new NullPointerException("Delegate must not be null");
        } else {
            this.owner = owner;
            this.delegate = delegate;
        }
    }

    public void updateReference(Registry<? super T> registry) {
        RegistryObject<T> delegate = this.delegate;
        Objects.requireNonNull(delegate, "Registry entry is empty").updateReference(registry);
    }

    public @NonnullType T get() {
        RegistryObject<T> delegate = this.delegate;
        return Objects.requireNonNull(this.getUnchecked(), () -> {
            return delegate == null ? "Registry entry is empty" : "Registry entry not present: " + delegate.getId();
        });
    }

    public @Nullable T getUnchecked() {
        RegistryObject<T> delegate = this.delegate;
        return delegate == null ? null : delegate.orElse(null);
    }

    public <R, E extends R> RegistryEntry<E> getSibling(ResourceKey<? extends Registry<R>> registryType) {
        return this == EMPTY ? empty() : new RegistryEntry<>(this.owner.get(this.getId().getPath(), registryType));
    }

    public <R, E extends R> RegistryEntry<E> getSibling(Registry<R> registry) {
        return this.getSibling(registry.key());
    }

    public RegistryEntry<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        return this.isPresent() && !predicate.test(this.get()) ? empty() : this;
    }

    public <R> boolean is(R entry) {
        return this.get() == entry;
    }

    @SuppressWarnings("unchecked")
    protected static <E extends RegistryEntry<?>> E cast(Class<? super E> clazz, RegistryEntry<?> entry) {
        if (clazz.isInstance(entry)) {
            return (E) entry;
        } else {
            throw new IllegalArgumentException("Could not convert RegistryEntry: expecting " + clazz + ", found " + entry.getClass());
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RegistryEntry<?> other)) {
            return false;
        } else {
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$delegate = this.delegate;
                Object other$delegate = other.delegate;
                if (this$delegate == null) {
                    if (other$delegate != null) {
                        return false;
                    }
                } else if (!this$delegate.equals(other$delegate)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof RegistryEntry;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $delegate = this.delegate;
        result = result * 59 + ($delegate == null ? 43 : $delegate.hashCode());
        return result;
    }

    public ResourceLocation getId() {
        return this.delegate.getId();
    }

    public boolean isPresent() {
        return this.delegate.isPresent();
    }

    public void ifPresent(Consumer<? super T> consumer) {
        this.delegate.ifPresent(consumer);
    }

    public Stream<T> stream() {
        return this.delegate.stream();
    }

    public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        return this.delegate.map(mapper);
    }

    public <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
        return this.delegate.flatMap(mapper);
    }

    public <U> Supplier<U> lazyMap(Function<? super T, ? extends U> mapper) {
        return this.delegate.lazyMap(mapper);
    }

    public T orElse(T other) {
        return this.delegate.orElse(other);
    }

    public T orElseGet(Supplier<? extends T> other) {
        return this.delegate.orElseGet(other);
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return this.delegate.orElseThrow(exceptionSupplier);
    }

    private interface Exclusions<T> {
        T get();

        RegistryObject<T> filter(Predicate<? super T> var1);

        void updateReference(Registry<? extends T> var1);
    }
}
