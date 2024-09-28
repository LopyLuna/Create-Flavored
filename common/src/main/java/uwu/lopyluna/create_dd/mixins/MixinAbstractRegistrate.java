package uwu.lopyluna.create_dd.mixins;

import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import uwu.lopyluna.create_dd.access.AccessAbstractRegistrate;
import uwu.lopyluna.create_dd.registry.DesiresRegistrate;

@Mixin(AbstractRegistrate.class)
public class MixinAbstractRegistrate implements AccessAbstractRegistrate {
    @Shadow @Final private Multimap<Pair<String, ResourceKey<? extends Registry<?>>>, NonNullConsumer<?>> registerCallbacks;

    @Shadow @Final private Table<String, ResourceKey<? extends Registry<?>>, AbstractRegistrate<S>.Registration<?, ?>> registrations;

    @Override
    public Multimap<Pair<String, ResourceKey<? extends Registry<?>>>, NonNullConsumer<?>> getRegisterCallbacks() {
        return registerCallbacks;
    }

    @Override
    public Table<String, ResourceKey<? extends Registry<?>>, DesiresRegistrate.Registration<?, ?>> getRegistrations() {
        return registrations;
    }
}
