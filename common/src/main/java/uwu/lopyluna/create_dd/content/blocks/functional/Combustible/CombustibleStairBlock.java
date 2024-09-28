package uwu.lopyluna.create_dd.content.blocks.functional.Combustible;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CombustibleStairBlock extends StairBlock {
    boolean isFlammable;
    int getFlammability;
    int getFireSpreadSpeed;

    public static CombustibleStairBlock create(BlockState pBaseState, Properties properties) {
        return new CombustibleStairBlock(pBaseState, properties, false, 0, 0);
    }
    public static CombustibleStairBlock create(BlockState pBaseState, Properties properties, int getFlammability, int getFireSpreadSpeed) {
        return new CombustibleStairBlock(pBaseState, properties, true, getFlammability, getFireSpreadSpeed);
    }

    public CombustibleStairBlock(BlockState pBaseState, Properties pProperties, boolean isFlammable, int getFlammability, int getFireSpreadSpeed) {
        super(() -> pBaseState, pProperties);
        this.isFlammable = isFlammable;
        this.getFlammability = getFlammability;
        this.getFireSpreadSpeed = getFireSpreadSpeed;
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return isFlammable;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return getFlammability;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return getFireSpreadSpeed;
    }
}
