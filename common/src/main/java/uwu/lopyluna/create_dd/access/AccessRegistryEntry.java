package uwu.lopyluna.create_dd.access;

import com.tterrag.registrate.AbstractRegistrate;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.create_dd.infrastructure.registrate_classes.RegistryObject;

public interface AccessRegistryEntry<T> {
    AbstractRegistrate<?> getOwner();
    @Nullable RegistryObject<T> getDelegate();
}
