package uwu.lopyluna.create_dd.content.blocks.logistics.fluid_reservoir;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.create_dd.registry.DesiresSpriteShifts;

@SuppressWarnings({"unused"})
public class FluidReservoirCTBehaviour extends ConnectedTextureBehaviour.Base {

    @Override
    public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
        return getShiftConnection(state, direction, sprite);
    }

    public CTSpriteShiftEntry getShiftConnection(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
        Axis vaultBlockAxis = FluidReservoirBlock.getKegAxis(state);
        boolean small = !FluidReservoirBlock.isLarge(state);
        if (vaultBlockAxis == null)
            return null;

        if (direction.getAxis() == vaultBlockAxis)
            return DesiresSpriteShifts.KEG_FRONT.get(small);
        if (direction == Direction.UP)
            return DesiresSpriteShifts.KEG_TOP.get(small);
        if (direction == Direction.DOWN)
            return DesiresSpriteShifts.KEG_BOTTOM.get(small);

        return state.getValue(FluidReservoirBlock.WINDOW) ? DesiresSpriteShifts.KEG_SIDE_WINDOW.get(small) : DesiresSpriteShifts.KEG_SIDE.get(small);
    }


    @Override
    protected Direction getUpDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
        Axis kegBlockAxis = FluidReservoirBlock.getKegAxis(state);
        boolean alongX = kegBlockAxis == Axis.X;
        if (face.getAxis()
                .isVertical() && alongX)
            return super.getUpDirection(reader, pos, state, face).getClockWise();
        if (face.getAxis() == kegBlockAxis || face.getAxis()
                .isVertical())
            return super.getUpDirection(reader, pos, state, face);
        assert kegBlockAxis != null;
        return Direction.fromAxisAndDirection(kegBlockAxis, alongX ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE);
    }

    @Override
    protected Direction getRightDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
        Axis vaultBlockAxis = FluidReservoirBlock.getKegAxis(state);
        if (face.getAxis().isVertical() && vaultBlockAxis == Axis.X)
            return super.getRightDirection(reader, pos, state, face).getClockWise();
        if (face.getAxis() == vaultBlockAxis || face.getAxis().isVertical())
            return super.getRightDirection(reader, pos, state, face);
        return Direction.fromAxisAndDirection(Axis.Y, face.getAxisDirection());
    }

    public boolean buildContextForOccludedDirections() {
        return true;
    }

    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos,
                              BlockPos otherPos, Direction face) {
        return state == other && ConnectivityHandler.isConnected(reader, pos, otherPos);
    }

}
