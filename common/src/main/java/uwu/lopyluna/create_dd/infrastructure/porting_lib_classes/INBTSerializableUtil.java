package uwu.lopyluna.create_dd.infrastructure.porting_lib_classes;

import net.minecraft.nbt.Tag;

@Deprecated(forRemoval = true)
public interface INBTSerializableUtil<T extends Tag> {
    T serializeNBT();

    void deserializeNBT(T nbt);
}
