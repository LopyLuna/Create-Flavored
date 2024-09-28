package uwu.lopyluna.create_dd.content.blocks.logistics.fluid_reservoir;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class FluidReservoirGenerator extends SpecialBlockStateGen {

    public FluidReservoirGenerator() {
    }

    @Override
    protected int getXRotation(BlockState s) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState s) {
        return s.getValue(FluidReservoirBlock.HORIZONTAL_AXIS) == Direction.Axis.X ? 90 : 0;
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> c, RegistrateBlockstateProvider p, BlockState s) {
        String modelName = c.getName() + (s.getValue(FluidReservoirBlock.WINDOW) ? "_window" : "");
        return p.models().getExistingFile(p.modLoc("block/" + modelName));
    }
}
