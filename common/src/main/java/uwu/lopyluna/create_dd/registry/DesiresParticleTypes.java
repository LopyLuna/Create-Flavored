package uwu.lopyluna.create_dd.registry;

import com.simibubi.create.foundation.particle.ICustomParticleData;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import uwu.lopyluna.create_dd.registry.helper.Lang;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.content.blocks.kinetics.furnace_engine.SmokeJetParticleData;

import java.util.function.Supplier;

public enum DesiresParticleTypes {
    SMOKE_JET(SmokeJetParticleData::new);

    private final ParticleEntry<?> entry;

    <D extends ParticleOptions> DesiresParticleTypes(Supplier<? extends ICustomParticleData<D>> typeFactory) {
        String name = Lang.asId(name());
        entry = new ParticleEntry<>(name, typeFactory);
    }

    public static void register() {
        ParticleEntry.REGISTER.register();
    }

    @Environment(EnvType.CLIENT)
    public static void registerFactories(RegisterParticleProvidersEvent event) {
        for (DesiresParticleTypes particle : values())
            particle.entry.registerFactory(event);
    }

    public ParticleType<?> get() {
        return entry.object.get();
    }

    public String parameter() {
        return entry.name;
    }

    private static class ParticleEntry<D extends ParticleOptions> {
        private static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister.create(Desires.MOD_ID, Registry.PARTICLE_TYPE_REGISTRY);

        private final String name;
        private final Supplier<? extends ICustomParticleData<D>> typeFactory;
        private final RegistrySupplier<ParticleType<D>> object;

        public ParticleEntry(String name, Supplier<? extends ICustomParticleData<D>> typeFactory) {
            this.name = name;
            this.typeFactory = typeFactory;

            object = REGISTER.register(name, () -> this.typeFactory.get().createType());
        }

        @Environment(EnvType.CLIENT)
        public void registerFactory(RegisterParticleProvidersEvent event) {
            typeFactory.get()
                    .register(object.get(), event);
        }
    }
}