package uwu.lopyluna.create_dd.fabric;

import net.fabricmc.api.ModInitializer;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.registry.DesiresFluids;
import uwu.lopyluna.create_dd.registry.DesiresWoodType;

public class DesiresFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Desires.init();
        Desires.REGISTRATE.register();
        DesiresFluids.registerFluidInteractions();
        DesiresWoodType.regStrippables();
    }
}
