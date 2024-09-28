package uwu.lopyluna.create_dd.content.items.equipment;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;

import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
public class NameableRecordItem extends RecordItem {

    private final String desc;

    public NameableRecordItem(int comparatorValue, Supplier<SoundEvent> soundSupplier, Properties builder, int lengthInTicks, String desc) {
        super(comparatorValue, soundSupplier, builder, lengthInTicks);
        this.desc = desc;
    }

    @Override
    public MutableComponent getDisplayName() {
        return Component.literal(desc);
    }
}
