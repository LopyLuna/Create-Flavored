package uwu.lopyluna.create_dd.infrastructure.config;

import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;
import uwu.lopyluna.create_dd.registry.DesiresOreFeaturesEntries;

import static uwu.lopyluna.create_dd.Desires.NAME;

@SuppressWarnings("unused")
public class DWorldGen extends ConfigBase {

    public static final int FORCED_UPDATE_VERSION = 2;

    public final ConfigBase.ConfigBool disable = b(false, "disableWorldGen", Comments.disable);

    @Override
    public void registerAll(ForgeConfigSpec.Builder builder) {
        super.registerAll(builder);
        DesiresOreFeaturesEntries.fillConfig(builder);
    }

    @Override
    public String getName() {
        return "worldgen.v" + FORCED_UPDATE_VERSION;
    }

    private static class Comments {
        static String disable = "Prevents all worldgen added by " + NAME + " from taking effect";
    }
}
