package uwu.lopyluna.create_dd.infrastructure.config;


import com.simibubi.create.foundation.config.ConfigBase;

@SuppressWarnings("unused")
public class DCommon extends ConfigBase {

    public final DWorldGen worldGen = nested(0, DWorldGen::new, Comments.worldGen);

    @Override
    public String getName() {
        return "common";
    }

    private static class Comments {
        static String worldGen = "Modify Create's impact on your terrain";
    }
}
