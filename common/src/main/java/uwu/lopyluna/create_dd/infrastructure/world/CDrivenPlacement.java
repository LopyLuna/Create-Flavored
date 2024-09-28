package uwu.lopyluna.create_dd.infrastructure.world;

import com.simibubi.create.infrastructure.worldgen.ConfigDrivenPlacement;
import com.simibubi.create.infrastructure.worldgen.OreFeatureConfigEntry;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;

public class CDrivenPlacement extends ConfigDrivenPlacement {
    public CDrivenPlacement(OreFeatureConfigEntry entry) {
        super(entry);
    }

    @Override
    public PlacementModifierType<?> type() {
        return DesiresPlacementModifiers.CONFIG_DRIVEN.get();
    }

    @Override
    public float getFrequency() {
        if (DesiresConfigs.common().worldGen.disable.get())
            return 0;
        return getEntry().frequency.getF();
    }
}
