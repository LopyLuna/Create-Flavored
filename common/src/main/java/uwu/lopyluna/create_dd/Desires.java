package uwu.lopyluna.create_dd;

import com.mojang.logging.LogUtils;
import com.simibubi.create.*;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import uwu.lopyluna.create_dd.compat.DesiresMods;
import uwu.lopyluna.create_dd.compat.registry.EncasedCompat;
import uwu.lopyluna.create_dd.content.blocks.kinetics.furnace_engine.FurnaceEngineBlock;
import uwu.lopyluna.create_dd.infrastructure.world.DesireBuiltinRegistration;
import uwu.lopyluna.create_dd.infrastructure.world.DesiresFeatures;
import uwu.lopyluna.create_dd.infrastructure.world.DesiresPlacementModifiers;
import uwu.lopyluna.create_dd.registry.*;
import uwu.lopyluna.create_dd.registry.addons.DreamsAddons;
import uwu.lopyluna.create_dd.registry.darkness.MagicItems;
import uwu.lopyluna.create_dd.registry.delighted.EndItems;
import uwu.lopyluna.create_dd.registry.destinies.ExtrasItems;

import java.util.Random;


@SuppressWarnings({"deprecation", "unused"})
public class Desires {
    public static final String NAME = "Create: Dreams n' Desires";
    public static final String MOD_ID = "create_dd";
    public static final String VERSION = "1.3a.Beta Mid-Dev";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Random RANDOM = Create.RANDOM;
    public static final DesiresRegistrate REGISTRATE = DesiresRegistrate.create(MOD_ID);

    @Nullable
    public static KineticStats create(Item item) {
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();

            if (block instanceof IRotate || block instanceof FurnaceEngineBlock) {
                return new KineticStats(block);
            }
        }

        return null;
    }

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(Desires.create(item))));
    }

    public static void init() {
        //DesiresAllSoundEvents.prepare();
        DesiresSoundEvents.register();
        DesiresTags.init();
        DesiresCreativeModeTabs.init();
        DesiresSpriteShifts.register();
        DesiresWoodType.register();
        DesiresBlocks.register();
        DesiresItems.register();
        DesiresFluids.register();
        DesiresPaletteBlocks.register();
        ClassicBlocks.register();
        ClassicItems.register();
        ClassicBlockEntityTypes.register();
        DesiresEntityTypes.register();
        DesiresBlockEntityTypes.register();
        DesireFanProcessingTypes.register();
        DesiresRecipeTypes.register();
        DesiresParticleTypes.register();
        // DesiresEntityDataSerializers.register(); // TODO - Is this even used?
        DesiresPackets.registerPackets();
        DesiresOreFeaturesEntries.init();
        DesiresPlacementModifiers.register();
        DesiresFeatures.register();
        DesireBuiltinRegistration.register();
        DesiresBlockMovementChecks.register();

        // ADDONS

        if (DreamsAddons.EXTRAS.isLoaded()) {
            ExtrasItems.register();
        }

        if (DreamsAddons.MAGIC.isLoaded()) {
            MagicItems.register();
        }
        
        if (DreamsAddons.ENDGAME.isLoaded()) {
            EndItems.register();
        }

        // COMPAT
        
        if (DesiresMods.CREATECASING.isLoaded()) {
            EncasedCompat.register();
        }
    }
}
