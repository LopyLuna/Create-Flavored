package uwu.lopyluna.create_dd.infrastructure.world;

import com.simibubi.create.infrastructure.worldgen.LayeredOreFeature;
import com.simibubi.create.infrastructure.worldgen.StandardOreFeature;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;

import static uwu.lopyluna.create_dd.Desires.MOD_ID;

@SuppressWarnings("unused")
public class DesiresFeatures {
    private static final DeferredRegister<Feature<?>> REGISTER = DeferredRegister.create(MOD_ID, Registry.FEATURE_REGISTRY);

    public static final RegistrySupplier<StandardOreFeature> STANDARD_ORE = REGISTER.register("standard_ore", StandardOreFeature::new);
    public static final RegistrySupplier<LayeredOreFeature> LAYERED_ORE = REGISTER.register("layered_ore", LayeredOreFeature::new);

    public static void register() {
        REGISTER.register();
    }
}
