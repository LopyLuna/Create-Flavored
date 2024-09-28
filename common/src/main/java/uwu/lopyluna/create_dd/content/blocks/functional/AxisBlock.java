package uwu.lopyluna.create_dd.content.blocks.functional;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AxisBlock extends RotatedPillarBlock {
    boolean isFlammable;
    int getFlammability;
    int getFireSpreadSpeed;

    public static AxisBlock create(Properties properties) {
        return new AxisBlock(properties, false, 0, 0);
    }
    public static AxisBlock create(Properties properties, int getFlammability, int getFireSpreadSpeed) {
        return new AxisBlock(properties, true, getFlammability, getFireSpreadSpeed);
    }

    public AxisBlock(Properties properties, boolean isFlammable, int getFlammability, int getFireSpreadSpeed) {
        super(properties);
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
