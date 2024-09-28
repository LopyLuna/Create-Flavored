package uwu.lopyluna.create_dd.infrastructure.world;

import com.simibubi.create.infrastructure.worldgen.OreFeatureConfigEntry;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Map;

import static uwu.lopyluna.create_dd.Desires.MOD_ID;

public class DesireBuiltinRegistration {
    private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURE_REGISTER = DeferredRegister.create(MOD_ID, Registry.CONFIGURED_FEATURE_REGISTRY);
    private static final DeferredRegister<PlacedFeature> PLACED_FEATURE_REGISTER = DeferredRegister.create(MOD_ID, Registry.PLACED_FEATURE_REGISTRY);
    // private static final DeferredRegister<BiomeModifier> BIOME_MODIFIER_REGISTER = DeferredRegister.create(Registry.Keys.BIOME_MODIFIERS, MOD_ID);

    static {
        for (Map.Entry<ResourceLocation, OreFeatureConfigEntry> entry : OreFeatureConfigEntry.ALL.entrySet()) {
            ResourceLocation id = entry.getKey();
            if (id.getNamespace().equals(MOD_ID)) {
                OreFeatureConfigEntry.DatagenExtension datagenExt = entry.getValue().datagenExt();
                if (datagenExt != null) {
                    CONFIGURED_FEATURE_REGISTER.register(id.getPath(), () -> datagenExt.createConfiguredFeature(BuiltinRegistries.ACCESS));
                    PLACED_FEATURE_REGISTER.register(id.getPath(), () -> datagenExt.createPlacedFeature(BuiltinRegistries.ACCESS));
                    // BIOME_MODIFIER_REGISTER.register(id.getPath(), () -> datagenExt.createBiomeModifier(BuiltinRegistries.ACCESS)); // TODO
                }
            }
        }
    }

    public static void register() {
        CONFIGURED_FEATURE_REGISTER.register();
        PLACED_FEATURE_REGISTER.register();
        // BIOME_MODIFIER_REGISTER.register(modEventBus); // TODO
    }
}
