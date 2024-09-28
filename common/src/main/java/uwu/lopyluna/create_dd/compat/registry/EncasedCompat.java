package uwu.lopyluna.create_dd.compat.registry;


import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;
import uwu.lopyluna.create_dd.registry.DesiresCreativeModeTabs;
import uwu.lopyluna.create_dd.registry.DesiresSpriteShifts;

import static uwu.lopyluna.create_dd.Desires.REGISTRATE;

@SuppressWarnings({"unused"})
public class EncasedCompat {
    //ON HOLD FOR NOW DUE TO ISSUES

    static {
        REGISTRATE.creativeModeTab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB);
    }
    public static final BlockEntry<? extends Block> CREATIVE = createCompatForMaterial(REGISTRATE, "creative", DesiresBlocks.CREATIVE_CASING, true,
            DesiresSpriteShifts.CREATIVE_CASING, DesiresSpriteShifts.CREATIVE_CASING_COGWHEEL_SIDE, DesiresSpriteShifts.CREATIVE_CASING_COGWHEEL_OTHERSIDE);

    public static BlockEntry<? extends Block> createCompatForMaterial(CreateRegistrate registrate, String name, BlockEntry<? extends Block> casing, boolean shouldGenerateVerticalItem,
                CTSpriteShiftEntry connectedTexturesSprite, CTSpriteShiftEntry verticalCogwheelSide, CTSpriteShiftEntry horizontalCogwheelSide) {
    //    fr.iglee42.createcasing.api.CreateCasingApi.createEncasedShaft(
    //            registrate, name, casing::get, connectedTexturesSprite);
    //    fr.iglee42.createcasing.api.CreateCasingApi.createEncasedCogwheel(
    //            registrate, name, casing::get, connectedTexturesSprite, verticalCogwheelSide, horizontalCogwheelSide);
    //    fr.iglee42.createcasing.api.CreateCasingApi.createEncasedLargeCogwheel(
    //            registrate, name, casing::get, connectedTexturesSprite);
    //    fr.iglee42.createcasing.api.CreateCasingApi.createEncasedPipe(
    //            registrate, name, casing::get, connectedTexturesSprite);
    //    fr.iglee42.createcasing.api.CreateCasingApi.createGearbox(
    //            registrate, name, connectedTexturesSprite, shouldGenerateVerticalItem);
    //    fr.iglee42.createcasing.api.CreateCasingApi.createDepot(
    //            registrate, name);
    //    fr.iglee42.createcasing.api.CreateCasingApi.createMixer(
    //            registrate, name);
    //    fr.iglee42.createcasing.api.CreateCasingApi.createPress(
    //            registrate, name);
        return null;
    }

    public static void register() {}
}
