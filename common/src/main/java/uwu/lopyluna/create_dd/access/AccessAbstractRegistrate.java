package uwu.lopyluna.create_dd.access;

import com.google.common.collect.Multimap;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.apache.commons.lang3.tuple.Pair;
import uwu.lopyluna.create_dd.registry.DesiresRegistrate;

public interface AccessAbstractRegistrate {
    Multimap<Pair<String, ResourceKey<? extends Registry<?>>>, NonNullConsumer<?>> getRegisterCallbacks();
    Table<String, ResourceKey<? extends Registry<?>>, DesiresRegistrate.Registration<?, ?>> getRegistrations();
}
