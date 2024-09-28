package uwu.lopyluna.create_dd.content.blocks.functional.Combustible;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CombustibleTrapdoorBlock extends TrapDoorBlock {
    boolean isFlammable;
    int getFlammability;
    int getFireSpreadSpeed;

    public static CombustibleTrapdoorBlock create(Properties properties) {
        return new CombustibleTrapdoorBlock(properties, false, 0, 0);
    }
    public static CombustibleTrapdoorBlock create(Properties properties, int getFlammability, int getFireSpreadSpeed) {
        return new CombustibleTrapdoorBlock(properties, true, getFlammability, getFireSpreadSpeed);
    }

    public CombustibleTrapdoorBlock(Properties pProperties, boolean isFlammable, int getFlammability, int getFireSpreadSpeed) {
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
