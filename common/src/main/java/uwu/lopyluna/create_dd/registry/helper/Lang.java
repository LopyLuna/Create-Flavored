package uwu.lopyluna.create_dd.registry.helper;

import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.network.chat.MutableComponent;
import uwu.lopyluna.create_dd.Desires;

public class Lang extends com.simibubi.create.foundation.utility.Lang {

    public static MutableComponent translateDirect(String key, Object... args) {
        return Components.translatable(Desires.MOD_ID + "." + key, resolveBuilders(args));
    }

    public static LangBuilder builder() {
        return new LangBuilder(Desires.MOD_ID);
    }
}
