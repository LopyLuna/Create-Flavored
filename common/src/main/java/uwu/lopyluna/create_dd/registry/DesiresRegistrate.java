package uwu.lopyluna.create_dd.registry;

import com.google.common.base.Preconditions;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.DebugMarkers;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.access.AccessAbstractRegistrate;
import uwu.lopyluna.create_dd.infrastructure.registrate_classes.*;

import java.util.ArrayList;
import java.util.List;

public class DesiresRegistrate extends CreateRegistrate {
    protected DesiresRegistrate(String modid) {
        super(modid);
    }

    public <T extends SimpleFlowableFluid> FluidBuilder<SimpleFlowableFluid, DesiresRegistrate> standardFluid(String name,
                                                                                                             NonNullFunction<SimpleFlowableFluid.Properties, T> typeFactory) {
        return customFluid(name, new ResourceLocation(getModid(), "fluid/" + name + "_still"), new ResourceLocation(getModid(), "fluid/" + name + "_flow"),
                typeFactory);
    }

    public <T extends SimpleFlowableFluid> FluidBuilder<T, DesiresRegistrate> customFluid(String name, ResourceLocation stillTexture, ResourceLocation flowingTexture,
                                                                                                                                  NonNullFunction<SimpleFlowableFluid.Properties, T> fluidFactory) {
        return customFluid(self(), name, stillTexture, flowingTexture, fluidFactory);
    }

    public <T extends SimpleFlowableFluid, P> FluidBuilder<T, P> customFluid(P parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture,
                                                                                                                                     NonNullFunction<SimpleFlowableFluid.Properties, T> fluidFactory) {
        return customEntry(name, callback -> FluidBuilder.create(this, parent, name, callback, stillTexture, flowingTexture, fluidFactory));
    }

    public <R, T extends R, P, S2 extends Builder<R, T, P, S2>> S2 customEntry(String name, NonNullFunction<BuilderCallback, S2> factory) {
        return factory.apply(this::customAccept);
    }

    protected <R , T extends R> RegistryEntry<T> customAccept(String name, ResourceKey<? extends Registry<R>> type, Builder<R, T, ?, ?> builder, NonNullSupplier<? extends T> creator, NonNullFunction<RegistryObject<T>, ? extends RegistryEntry<T>> entryFactory) {
        RegistryEntry<T> entry = customAccept(name, type, builder, creator, entryFactory);
        if (type.equals(Registry.ITEM_REGISTRY)) {
            if (currentTooltipModifierFactory != null) {
                TooltipModifier.REGISTRY.registerDeferred(entry.getId(), currentTooltipModifierFactory);
            }
        }
        return entry;
    }

    protected <R, T extends R> RegistryEntry<T> customAccept(String name, ResourceKey<? extends Registry<R>> type, com.tterrag.registrate.builders.Builder<R, T, ?, ?> builder, NonNullSupplier<? extends T> creator, NonNullFunction<RegistryObject<T>, ? extends RegistryEntry<T>> entryFactory) {
        Registration<R, T> reg = new Registration<>(new ResourceLocation(modid, name), type, creator, entryFactory);
        Desires.LOGGER.debug(DebugMarkers.REGISTER, "Captured registration for entry {} of type {}", name, type.location());
        ((AccessAbstractRegistrate) this).getRegisterCallbacks().removeAll(Pair.of(name, type)).forEach(callback -> {
            @SuppressWarnings({ "unchecked", "null" })
            NonNullConsumer<? super T> unsafeCallback = (NonNullConsumer<? super T>) callback;
            reg.addRegisterCallback(unsafeCallback);
        });
        registrations.put(name, type, reg);
        return reg.getDelegate();
    }

    public static DesiresRegistrate create(String modid) {
        return new DesiresRegistrate(modid);
    }
}
