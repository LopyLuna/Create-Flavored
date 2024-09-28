package uwu.lopyluna.create_dd.content.blocks.kinetics.multimeter;

import com.mojang.math.Vector3f;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.worldWrappers.WrappedWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.registry.DesiresBlockEntityTypes;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings({"deprecation"})
public class MultiMeterBlock extends DirectionalAxisKineticBlock implements IBE<MultiMeterBlockEntity> {
    public static final GaugeShaper GAUGE = GaugeShaper.make();

    public MultiMeterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        Direction face = context.getClickedFace();
        BlockPos placedOnPos = context.getClickedPos()
                .relative(context.getClickedFace()
                        .getOpposite());
        BlockState placedOnState = world.getBlockState(placedOnPos);
        Block block = placedOnState.getBlock();

        if (block instanceof IRotate && ((IRotate) block).hasShaftTowards(world, placedOnPos, placedOnState, face)) {
            BlockState toPlace = defaultBlockState();
            Direction horizontalFacing = context.getHorizontalDirection();
            Direction nearestLookingDirection = context.getNearestLookingDirection();
            boolean lookPositive = nearestLookingDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE;
            if (face.getAxis() == Direction.Axis.X) {
                toPlace = toPlace.setValue(FACING, lookPositive ? Direction.NORTH : Direction.SOUTH)
                        .setValue(AXIS_ALONG_FIRST_COORDINATE, true);
            } else if (face.getAxis() == Direction.Axis.Y) {
                toPlace = toPlace.setValue(FACING, horizontalFacing.getOpposite())
                        .setValue(AXIS_ALONG_FIRST_COORDINATE, horizontalFacing.getAxis() == Direction.Axis.X);
            } else {
                toPlace = toPlace.setValue(FACING, lookPositive ? Direction.WEST : Direction.EAST)
                        .setValue(AXIS_ALONG_FIRST_COORDINATE, false);
            }

            return toPlace;
        }

        return super.getStateForPlacement(context);
    }

    @Override
    protected Direction getFacingForPlacement(BlockPlaceContext context) {
        return context.getClickedFace();
    }

    @Override
    protected boolean getAxisAlignmentForPlacement(BlockPlaceContext context) {
        return context.getHorizontalDirection()
                .getAxis() != Direction.Axis.X;
    }

    public boolean shouldRenderHeadOnFace(Level world, BlockPos pos, BlockState state, Direction face) {
        if (face.getAxis()
                .isVertical())
            return false;
        if (face == state.getValue(FACING)
                .getOpposite())
            return false;
        if (face.getAxis() == getRotationAxis(state))
            return false;
        if (getRotationAxis(state) == Direction.Axis.Y && face != state.getValue(FACING))
            return false;
        return Block.shouldRenderFace(state, world, pos, face, pos.relative(face)) || world instanceof WrappedWorld;
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        BlockEntity be = worldIn.getBlockEntity(pos);
        if (!(be instanceof MultiMeterBlockEntity gaugeBE))
            return;
        if (gaugeBE.dialTarget == 0)
            return;
        int color = gaugeBE.color;

        for (Direction face : Iterate.directions) {
            if (!shouldRenderHeadOnFace(worldIn, pos, stateIn, face))
                continue;

            Vector3f rgb = new Color(color).asVectorF();
            Vec3 faceVec = Vec3.atLowerCornerOf(face.getNormal());
            Direction positiveFacing = Direction.get(Direction.AxisDirection.POSITIVE, face.getAxis());
            Vec3 positiveFaceVec = Vec3.atLowerCornerOf(positiveFacing.getNormal());
            int particleCount = gaugeBE.dialTarget > 1 ? 4 : 1;

            if (particleCount == 1 && rand.nextFloat() > 1 / 4f)
                continue;

            for (int i = 0; i < particleCount; i++) {
                Vec3 mul = VecHelper.offsetRandomly(Vec3.ZERO, rand, .25f)
                        .multiply(new Vec3(1, 1, 1).subtract(positiveFaceVec))
                        .normalize()
                        .scale(.3f);
                Vec3 offset = VecHelper.getCenterOf(pos)
                        .add(faceVec.scale(.55))
                        .add(mul);
                worldIn.addParticle(new DustParticleOptions(rgb, 1), offset.x, offset.y, offset.z, mul.x, mul.y, mul.z);
            }

        }

    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return GAUGE.get(state.getValue(FACING), state.getValue(AXIS_ALONG_FIRST_COORDINATE));
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<MultiMeterBlockEntity> getBlockEntityClass() {
        return MultiMeterBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MultiMeterBlockEntity> getBlockEntityType() {
        return DesiresBlockEntityTypes.MULTIMETER.get();
    }
}
