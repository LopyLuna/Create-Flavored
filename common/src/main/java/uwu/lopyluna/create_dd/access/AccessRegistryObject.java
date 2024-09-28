package uwu.lopyluna.create_dd.access;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface AccessRegistryObject<T> {
    ResourceLocation getId();
    @Nullable T getObject();
}
