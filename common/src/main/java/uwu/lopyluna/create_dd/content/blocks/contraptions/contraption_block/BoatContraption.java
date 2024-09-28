package uwu.lopyluna.create_dd.content.blocks.contraptions.contraption_block;

import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.TranslatingContraption;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;
import com.simibubi.create.content.contraptions.render.NonStationaryLighter;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import uwu.lopyluna.create_dd.registry.DesiresContraptionType;

public class BoatContraption extends TranslatingContraption {

    @Override
    public boolean assemble(Level world, BlockPos pos) throws AssemblyException {
        if (!searchMovedStructure(world, pos, null))
            return false;
        startMoving(world);
        return true;
    }

    @Override
    public ContraptionType getType() {
        return DesiresContraptionType.BOAT;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ContraptionLighter<?> makeLighter() {
        return new NonStationaryLighter<>(this);
    }
}
