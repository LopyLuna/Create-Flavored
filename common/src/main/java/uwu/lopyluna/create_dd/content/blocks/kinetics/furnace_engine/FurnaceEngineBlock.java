package uwu.lopyluna.create_dd.content.blocks.kinetics.furnace_engine;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.flywheel.FlywheelBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.registry.DesiresBlockEntityTypes;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings({"deprecation"})
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Mod.EventBusSubscriber
public class FurnaceEngineBlock extends FaceAttachedHorizontalDirectionalBlock implements SimpleWaterloggedBlock, IWrenchable, IBE<FurnaceEngineBlockEntity> {

    private static final int placementHelperId = PlacementHelpers.register(new FurnaceEngineBlock.PlacementHelper());

    public FurnaceEngineBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACE, AttachFace.FLOOR).setValue(FACING, Direction.NORTH).setValue(BlockStateProperties.WATERLOGGED, false));

    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return canAttach(pLevel, pPos, getConnectedDirection(pState).getOpposite()) && isValidPosition(pLevel,pPos.relative(getConnectedDirection(pState).getOpposite()),getConnectedDirection(pState));
    }

    public static boolean canAttach(LevelReader pReader, BlockPos pPos, Direction pDirection) {
        BlockPos blockpos = pPos.relative(pDirection);
        return pReader.getBlockState(blockpos).getBlock() instanceof AbstractFurnaceBlock;
    }

    public static boolean isValidPosition(LevelReader world, BlockPos pos, Direction facing) {
        for (Direction otherFacing : Iterate.directions) {
            if (otherFacing != facing) {
                BlockPos otherPos = pos.relative(otherFacing);
                BlockState otherState = world.getBlockState(otherPos);
                if (otherState.getBlock() instanceof FurnaceEngineBlock)
                    return false;
            }
        }
        return true;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACE, FACING, BlockStateProperties.WATERLOGGED));
    }
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                          BlockHitResult ray) {
        ItemStack heldItem = player.getItemInHand(hand);

        IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
        if (placementHelper.matchesItem(heldItem))
            return placementHelper.getOffset(player, world, state, pos, ray)
                    .placeInWorld(world, (BlockItem) heldItem.getItem(), player, hand, ray);
        return InteractionResult.PASS;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidState ifluidstate = level.getFluidState(pos);
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : state.setValue(BlockStateProperties.WATERLOGGED, ifluidstate.getType() == Fluids.WATER);
    }

    @SubscribeEvent
    public static void usingFurnaceEngineOnFurnacePreventsGUI(PlayerInteractEvent.RightClickBlock event) {
        BlockItem blockItem;
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            blockItem = (BlockItem) item;
        } else {
            return;
        }
        if (blockItem.getBlock() != DesiresBlocks.FURNACE_ENGINE.get())
            return;
        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (state.getBlock() instanceof AbstractFurnaceBlock)
            event.setUseBlock(Event.Result.DENY);
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    public static Direction getFacing(BlockState sideState) {
        return getConnectedDirection(sideState);
    }

    public static BlockPos getFlywheelPos(BlockState sideState, BlockPos pos) {
        return pos.relative(getConnectedDirection(sideState), 2);
    }

    public static boolean isFlywheelValid(BlockState state, BlockState shaft) {
        return (AllBlocks.FLYWHEEL.has(shaft) || DesiresBlocks.POWERED_FLYWHEEL.has(shaft)) && shaft.getValue(RotatedPillarKineticBlock.AXIS) != getFacing(state).getAxis();
    }

    @Override
    public Class<FurnaceEngineBlockEntity> getBlockEntityClass() {
        return FurnaceEngineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FurnaceEngineBlockEntity> getBlockEntityType() {
        return DesiresBlockEntityTypes.FURNACE_ENGINE.get();
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape up_z = Stream.of(Block.box(1.5, 4, 2, 14.5, 14, 14), Block.box(0, 3, 3.5, 1.5, 13, 12.5), Block.box(-1, -3, -1, 17, 4, 17), Block.box(14.5, 3, 3.5, 16, 13, 12.5), Block.box(3.5, 4, 0, 12.5, 11, 2), Block.box(3.5, 4, 14, 12.5, 11, 16)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
        VoxelShape up_x = Stream.of(Block.box(2, 4, 1.5, 14, 14, 14.5), Block.box(3.5, 3, 14.5, 12.5, 13, 16), Block.box(-1, -3, -1, 17, 4, 17), Block.box(3.5, 3, 0, 12.5, 13, 1.5), Block.box(0, 4, 3.5, 2, 11, 12.5), Block.box(14, 4, 3.5, 16, 11, 12.5)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

        VoxelShape down_z = Stream.of(Block.box(1.5, 2, 2, 14.5, 12, 14), Block.box(14.5, 3, 3.5, 16, 13, 12.5), Block.box(-1, 12, -1, 17, 19, 17), Block.box(0, 3, 3.5, 1.5, 13, 12.5), Block.box(3.5, 5, 0, 12.5, 12, 2), Block.box(3.5, 5, 14, 12.5, 12, 16)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
        VoxelShape down_x = Stream.of(Block.box(2, 2, 1.5, 14, 12, 14.5), Block.box(3.5, 3, 0, 12.5, 13, 1.5), Block.box(-1, 12, -1, 17, 19, 17), Block.box(3.5, 3, 14.5, 12.5, 13, 16), Block.box(0, 5, 3.5, 2, 12, 12.5), Block.box(14, 5, 3.5, 16, 12, 12.5)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

        VoxelShape wall_north = Stream.of(Block.box(1.5, 2, 2, 14.5, 14, 12), Block.box(0, 3.5, 3, 1.5, 12.5, 13), Block.box(-1, -1, 12, 17, 17, 19), Block.box(14.5, 3.5, 3, 16, 12.5, 13), Block.box(3.5, 0, 5, 12.5, 2, 12), Block.box(3.5, 14, 5, 12.5, 16, 12)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
        VoxelShape wall_south = Stream.of(Block.box(1.5, 2, 4, 14.5, 14, 14), Block.box(14.5, 3.5, 3, 16, 12.5, 13), Block.box(-1, -1, -3, 17, 17, 4), Block.box(0, 3.5, 3, 1.5, 12.5, 13), Block.box(3.5, 0, 4, 12.5, 2, 11), Block.box(3.5, 14, 4, 12.5, 16, 11)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
        VoxelShape wall_east = Stream.of(Block.box(4, 2, 1.5, 14, 14, 14.5), Block.box(3, 3.5, 0, 13, 12.5, 1.5), Block.box(-3, -1, -1, 4, 17, 17), Block.box(3, 3.5, 14.5, 13, 12.5, 16), Block.box(4, 0, 3.5, 11, 2, 12.5), Block.box(4, 14, 3.5, 11, 16, 12.5)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
        VoxelShape wall_west = Stream.of(Block.box(2, 2, 1.5, 12, 14, 14.5), Block.box(3, 3.5, 14.5, 13, 12.5, 16), Block.box(12, -1, -1, 19, 17, 17), Block.box(3, 3.5, 0, 13, 12.5, 1.5), Block.box(5, 0, 3.5, 12, 2, 12.5), Block.box(5, 14, 3.5, 12, 16, 12.5)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

        AttachFace face = pState.getValue(FACE);
        Direction direction = pState.getValue(FACING);
        return face == AttachFace.CEILING ? direction.getAxis() == Direction.Axis.X ? down_x : down_z :
               face == AttachFace.FLOOR ? direction.getAxis() == Direction.Axis.X ? up_x : up_z :
               direction == Direction.NORTH ? wall_north :
               direction == Direction.SOUTH ? wall_south :
               direction == Direction.EAST ? wall_east : wall_west;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        BlockPos shaftPos = getFlywheelPos(pState, pPos);
        BlockState shaftState = pLevel.getBlockState(shaftPos);
        if (isFlywheelValid(pState, shaftState))
            pLevel.setBlock(shaftPos, PoweredFlywheelBlock.getEquivalent(shaftState,shaftState.getValue(RotatedPillarKineticBlock.AXIS)), 3);
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {
        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return AllBlocks.FLYWHEEL::isIn;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return s -> s.getBlock() instanceof FurnaceEngineBlock;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos,
                                         BlockHitResult ray) {
            BlockPos shaftPos = FurnaceEngineBlock.getFlywheelPos(state, pos);
            BlockState shaft = AllBlocks.FLYWHEEL.getDefaultState();
            for (Direction direction : Direction.orderedByNearest(player)) {
                shaft = shaft.setValue(FlywheelBlock.AXIS, direction.getAxis());
                if (isFlywheelValid(state, shaft))
                    break;
            }

            BlockState newState = world.getBlockState(shaftPos);
            if (!newState.getMaterial().isReplaceable())
                return PlacementOffset.fail();

            Direction.Axis axis = shaft.getValue(FlywheelBlock.AXIS);
            return PlacementOffset.success(shaftPos,
                    s -> BlockHelper.copyProperties(s, DesiresBlocks.POWERED_FLYWHEEL.getDefaultState())
                            .setValue(PoweredFlywheelBlock.AXIS, axis));
        }
    }

    public static Couple<Integer> getSpeedRange() {
        return Couple.create(0, 32);
    }
}
