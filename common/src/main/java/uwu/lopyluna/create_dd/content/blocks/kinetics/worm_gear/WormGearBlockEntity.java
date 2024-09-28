package uwu.lopyluna.create_dd.content.blocks.kinetics.worm_gear;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;

@SuppressWarnings({"all"})
public class WormGearBlockEntity extends KineticBlockEntity {
    public WormGearBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    protected boolean syncSequenceContext() {
        return true;
    }

    @Override
    public float propagateRotationTo(KineticBlockEntity from, BlockState stateFrom, BlockState stateTo, BlockPos diff,
                                     boolean connectedViaAxes, boolean connectedViaCogs) {
        //CogWheelBlock & AllBlocks.LARGE_COGWHEEL.has(stateTo)
        if (!connectedViaAxes && from instanceof WormGearBlockEntity eWormGear && stateFrom.getBlock() instanceof WormGearBlock pWormGear && stateTo.getBlock() instanceof WormGearBlock pLargeGear && DesiresBlocks.WORM_GEAR.has(stateTo)) {
            Direction.Axis fromAxis = stateFrom.getValue(pWormGear.FACING).getAxis();
            Direction.Axis toAxis = stateTo.getValue(pLargeGear.FACING).getAxis();
            BlockPos pos = eWormGear.getBlockPos();
            Level level = eWormGear.getLevel();
            boolean isSameAxis = toAxis == fromAxis;
            float convert = 0.25F;
            Direction.Axis x = Direction.Axis.X;
            Direction.Axis y = Direction.Axis.Y;
            Direction.Axis z = Direction.Axis.Z;
            boolean stateX = (toAxis == y || toAxis == z) && (level.getBlockState(pos.east()) == stateFrom || level.getBlockState(pos.west()) == stateFrom);
            boolean stateY = (toAxis == x || toAxis == z) && (level.getBlockState(pos.above()) == stateFrom || level.getBlockState(pos.below()) == stateFrom);
            boolean stateZ = (toAxis == x || toAxis == y) && (level.getBlockState(pos.north()) == stateFrom || level.getBlockState(pos.south()) == stateFrom);
            boolean aX = fromAxis == x && (stateY || stateZ);
            boolean aY = fromAxis == y && (stateX || stateZ);
            boolean aZ = fromAxis == z && (stateY || stateX);

            if (aX || aY || aZ) {
                return -convert;}
            if (aX && aY || aZ) {
                return -convert;}
            if (aX || aY && aZ) {
                return -convert;}
            if (aX && aZ || aY) {
                return -convert;}
            if (aX && aY && aZ) {
                return -convert;}
        }
        return super.propagateRotationTo(from, stateFrom, stateTo, diff, connectedViaAxes, connectedViaCogs);
    }

    @Override
    public boolean isCustomConnection(KineticBlockEntity other, BlockState fromState, BlockState otherState) {
        //!AllBlocks.LARGE_COGWHEEL.has(otherState)
        if (!DesiresBlocks.WORM_GEAR.has(otherState))
            return false;
        final BlockPos diff = other.getBlockPos()
                .subtract(getBlockPos());
        Direction direction = Direction.getNearest(diff.getX(), diff.getY(), diff.getZ());
        return otherState.getValue(WormGearBlock.FACING).getAxis() == direction.getAxis();
    }
}
