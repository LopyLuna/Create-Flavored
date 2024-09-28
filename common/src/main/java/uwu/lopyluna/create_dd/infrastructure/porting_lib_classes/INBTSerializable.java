package uwu.lopyluna.create_dd.infrastructure.porting_lib_classes;

import net.minecraft.nbt.Tag;

@SuppressWarnings("removal")
public interface INBTSerializable<T extends Tag> extends INBTSerializableUtil<T> {
    default T serializeNBT() {
        throw new RuntimeException("override serializeNBT!");
    }

    default void deserializeNBT(T nbt) {
        throw new RuntimeException("override deserializeNBT!");
    }
}
