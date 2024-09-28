package uwu.lopyluna.create_dd.content.blocks.kinetics.furnace_engine;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ModelFile;
import uwu.lopyluna.create_dd.Desires;

public class FurnaceEngineGenerator extends SpecialBlockStateGen {

    private final String verticalModel;
    private final String wallModel;

    public FurnaceEngineGenerator() {
        this.verticalModel = "block/furnace_engine/block";
        this.wallModel = "block/furnace_engine/block_horizontal";
    }


    @Override
    protected int getXRotation(BlockState state) {
        return state.getValue(BlockStateProperties.ATTACH_FACE).ordinal() * 90;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return (((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) + (state.getValue(BlockStateProperties.ATTACH_FACE) == AttachFace.CEILING ? 180 : 0)) % 360;
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> c, RegistrateBlockstateProvider p, BlockState s) {
        AttachFace face = s.getValue(FurnaceEngineBlock.FACE);
        boolean isWall = face == AttachFace.WALL;

        return p.models().getExistingFile(new ResourceLocation(Desires.MOD_ID, isWall ? getWallModel() : getVerticalModel()));
    }

    public String getVerticalModel() {
        return verticalModel;
    }

    public String getWallModel() {
        return wallModel;
    }
}
