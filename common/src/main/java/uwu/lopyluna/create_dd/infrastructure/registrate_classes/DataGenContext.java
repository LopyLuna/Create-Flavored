package uwu.lopyluna.create_dd.infrastructure.registrate_classes;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class DataGenContext<R, E extends R> implements NonNullSupplier<E> {
    private final NonNullSupplier<E> entry;
    private final String name;
    private final ResourceLocation id;

    public @NonnullType E getEntry() {
        return this.entry.get();
    }

    public static <R, E extends R> DataGenContext<R, E> from(Builder<R, E, ?, ?> builder, ResourceKey<? extends Registry<R>> type) {
        return new DataGenContext(NonNullSupplier.of(builder.getOwner().get(builder.getName(), type)), builder.getName(), new ResourceLocation(builder.getOwner().getModid(), builder.getName()));
    }

    public DataGenContext(NonNullSupplier<E> entry, String name, ResourceLocation id) {
        this.entry = entry;
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof DataGenContext)) {
            return false;
        } else {
            DataGenContext other;
            label44: {
                other = (DataGenContext)o;
                Object this$entry = this.getEntry();
                Object other$entry = other.getEntry();
                if (this$entry == null) {
                    if (other$entry == null) {
                        break label44;
                    }
                } else if (this$entry.equals(other$entry)) {
                    break label44;
                }

                return false;
            }

            Object this$name = this.getName();
            Object other$name = other.getName();
            if (this$name == null) {
                if (other$name != null) {
                    return false;
                }
            } else if (!this$name.equals(other$name)) {
                return false;
            }

            Object this$id = this.getId();
            Object other$id = other.getId();
            if (this$id == null) {
                if (other$id != null) {
                    return false;
                }
            } else if (!this$id.equals(other$id)) {
                return false;
            }

            return true;
        }
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $entry = this.getEntry();
        result = result * 59 + ($entry == null ? 43 : $entry.hashCode());
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        return result;
    }

    public String toString() {
        Object var10000 = this.getEntry();
        return "DataGenContext(entry=" + var10000 + ", name=" + this.getName() + ", id=" + this.getId() + ")";
    }

    public NonNullSupplier<E> lazy() {
        return this.entry.lazy();
    }

    public E get() {
        return this.entry.get();
    }
}
