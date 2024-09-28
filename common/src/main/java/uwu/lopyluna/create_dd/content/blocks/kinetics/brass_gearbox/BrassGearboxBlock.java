package uwu.lopyluna.create_dd.content.blocks.kinetics.brass_gearbox;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.ticks.TickPriority;
import uwu.lopyluna.create_dd.registry.DesiresBlockEntityTypes;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings({"deprecation"})
public class BrassGearboxBlock extends DirectionalKineticBlock implements IBE<BrassGearboxBlockEntity> {

    public static final BooleanProperty UP_SHAFT = BooleanProperty.create("top_shaft");
    public static final BooleanProperty DOWN_SHAFT = BooleanProperty.create("bottom_shaft");
    public static final BooleanProperty NORTH_SHAFT = BooleanProperty.create("north_shaft");
    public static final BooleanProperty EAST_SHAFT = BooleanProperty.create("east_shaft");
    public static final BooleanProperty SOUTH_SHAFT = BooleanProperty.create("south_shaft");
    public static final BooleanProperty WEST_SHAFT = BooleanProperty.create("west_shaft");

    public BrassGearboxBlock(Properties properties) {
        super(properties);

        registerDefaultState(defaultBlockState()
                .setValue(UP_SHAFT, true)
                .setValue(DOWN_SHAFT, true)
                .setValue(NORTH_SHAFT, true)
                .setValue(EAST_SHAFT, true)
                .setValue(SOUTH_SHAFT, true)
                .setValue(WEST_SHAFT, true)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(UP_SHAFT, DOWN_SHAFT, NORTH_SHAFT, EAST_SHAFT, SOUTH_SHAFT, WEST_SHAFT));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState();
    }

    @Override
    public InteractionResult onWrenched(BlockState pState, UseOnContext pContext) {
        Level pLevel = pContext.getLevel();
        Direction clickedFace = pContext.getClickedFace();

        BlockState pStateNew = pState;

        pStateNew = clickedFace == Direction.UP ? pStateNew.cycle(UP_SHAFT) : pStateNew;
        pStateNew = clickedFace == Direction.DOWN ? pStateNew.cycle(DOWN_SHAFT) : pStateNew;
        pStateNew = clickedFace == Direction.NORTH ? pStateNew.cycle(NORTH_SHAFT) : pStateNew;
        pStateNew = clickedFace == Direction.EAST ? pStateNew.cycle(EAST_SHAFT) : pStateNew;
        pStateNew = clickedFace == Direction.SOUTH ? pStateNew.cycle(SOUTH_SHAFT) : pStateNew;
        pStateNew = clickedFace == Direction.WEST ? pStateNew.cycle(WEST_SHAFT) : pStateNew;

        KineticBlockEntity.switchToBlockState(pLevel, pContext.getClickedPos(), updateAfterWrenched(pStateNew, pContext));
        detachKinetics(pLevel, pContext.getClickedPos(), true);
        withBlockEntityDo(pLevel, pContext.getClickedPos(), KineticBlockEntity::clearKineticInformation);

        BlockEntity be = pContext.getLevel()
                .getBlockEntity(pContext.getClickedPos());
        if (be instanceof BrassGearboxBlockEntity) {
            be.setChanged();
            be.setBlockState(pStateNew);
        }

        if (pLevel.getBlockState(pContext.getClickedPos()) != pState)
            playRotateSound(pLevel, pContext.getClickedPos());


        return InteractionResult.SUCCESS;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.PUSH_ONLY;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState pState, Direction pFace) {
        return (pFace == Direction.UP && pState.getValue(UP_SHAFT)) ||
                (pFace == Direction.DOWN && pState.getValue(DOWN_SHAFT)) ||
                (pFace == Direction.NORTH && pState.getValue(NORTH_SHAFT)) ||
                (pFace == Direction.EAST && pState.getValue(EAST_SHAFT)) ||
                (pFace == Direction.SOUTH && pState.getValue(SOUTH_SHAFT)) ||
                (pFace == Direction.WEST && pState.getValue(WEST_SHAFT));
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return originalState;
    }

    public void detachKinetics(Level worldIn, BlockPos pos, boolean reAttachNextTick) {
        BlockEntity be = worldIn.getBlockEntity(pos);
        if (!(be instanceof KineticBlockEntity))
            return;
        RotationPropagator.handleRemoved(worldIn, pos, (KineticBlockEntity) be);

        // Re-attach next tick
        if (reAttachNextTick)
            worldIn.scheduleTick(pos, this, 0, TickPriority.EXTREMELY_HIGH);
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        BlockEntity be = worldIn.getBlockEntity(pos);
        if (!(be instanceof KineticBlockEntity kbe))
            return;
        RotationPropagator.handleAdded(worldIn, pos, kbe);
    }

    @Override
    public Class<BrassGearboxBlockEntity> getBlockEntityClass() {
        return BrassGearboxBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BrassGearboxBlockEntity> getBlockEntityType() {
        return DesiresBlockEntityTypes.BRASS_GEARBOX.get();
    }
}
