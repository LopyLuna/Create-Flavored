package uwu.lopyluna.create_dd.infrastructure.world;

import com.simibubi.create.infrastructure.worldgen.ConfigDrivenPlacement;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import static uwu.lopyluna.create_dd.Desires.MOD_ID;

public class DesiresPlacementModifiers {
    private static final DeferredRegister<PlacementModifierType<?>> REGISTER = DeferredRegister.create(MOD_ID, Registry.PLACEMENT_MODIFIER_REGISTRY);

    public static final RegistrySupplier<PlacementModifierType<? extends ConfigDrivenPlacement>> CONFIG_DRIVEN = REGISTER.register("config_driven", () -> () -> CDrivenPlacement.CODEC);

    public static void register() {
        REGISTER.register();
    }
}
