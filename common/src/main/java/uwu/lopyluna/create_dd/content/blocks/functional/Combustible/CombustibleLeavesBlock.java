package uwu.lopyluna.create_dd.content.blocks.functional.Combustible;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CombustibleLeavesBlock extends LeavesBlock {

    boolean isFlammable;
    int getFlammability;
    int getFireSpreadSpeed;

    public static CombustibleLeavesBlock create(Properties properties) {
        return new CombustibleLeavesBlock(properties, false, 0, 0);
    }
    public static CombustibleLeavesBlock create(Properties properties, boolean isFlammable) {
        return new CombustibleLeavesBlock(properties, true, 30, 60);
    }
    public static CombustibleLeavesBlock create(Properties properties, int getFlammability, int getFireSpreadSpeed) {
        return new CombustibleLeavesBlock(properties, true, getFlammability, getFireSpreadSpeed);
    }

    public CombustibleLeavesBlock(Properties pProperties, boolean isFlammable, int getFlammability, int getFireSpreadSpeed) {
        super(pProperties);
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
