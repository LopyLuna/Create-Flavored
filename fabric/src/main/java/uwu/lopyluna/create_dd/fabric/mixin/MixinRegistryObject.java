package uwu.lopyluna.create_dd.fabric.mixin;

import com.tterrag.registrate.fabric.RegistryObject;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import uwu.lopyluna.create_dd.access.AccessRegistryObject;

@Mixin(RegistryObject.class)
public class MixinRegistryObject<T> implements AccessRegistryObject<T> {
    @Shadow @Final private ResourceLocation id;
    @Shadow private @Nullable T object;

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public @Nullable T getObject() {
        return object;
    }
}
