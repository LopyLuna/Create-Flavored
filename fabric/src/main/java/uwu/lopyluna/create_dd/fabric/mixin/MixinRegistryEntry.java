package uwu.lopyluna.create_dd.fabric.mixin;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import uwu.lopyluna.create_dd.access.AccessRegistryEntry;
import uwu.lopyluna.create_dd.infrastructure.registrate_classes.RegistryObject;

@Mixin(RegistryEntry.class)
public class MixinRegistryEntry<T> implements AccessRegistryEntry<T> {
    @Shadow @Final private AbstractRegistrate<?> owner;

    @Shadow @Final private @Nullable com.tterrag.registrate.fabric.RegistryObject<T> delegate;

    @Override
    public AbstractRegistrate<?> getOwner() {
        return owner;
    }

    @Override
    public @Nullable RegistryObject<T> getDelegate() {
        return new RegistryObject<>(delegate);
    }
}
