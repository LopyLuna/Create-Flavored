package uwu.lopyluna.create_dd.content.blocks.kinetics.giant_gear;

import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import uwu.lopyluna.create_dd.registry.DesiresBlockEntityTypes;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"deprecation", "all"})
public class GiantGearBlock extends RotatedPillarKineticBlock implements IBE<GiantGearBlockEntity> {
    
    private static HashMap<Direction.Axis, HashMap<Vec3i, BlockState>> STRUCTURE_BY_AXIS;

    /**
     * Quick iteration of the structure, result is cached
     */
    public static HashMap<Direction.Axis, HashMap<Vec3i, BlockState>> getStructureByAxis() {
        if (STRUCTURE_BY_AXIS == null)
            STRUCTURE_BY_AXIS = generateStructuresByAxis();
        return STRUCTURE_BY_AXIS;
    }
    
    public static final BooleanProperty EXTENSION = BooleanProperty.create("extension");
    
    public GiantGearBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(EXTENSION, false));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(EXTENSION));
    }
    
    public Direction.Axis getAxisForPlacement(BlockPlaceContext context) {
        return Objects.requireNonNull(super.getStateForPlacement(context)).getValue(AXIS);
    }
    
    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.PASS;
    }
    
    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (!structureStillValid(pLevel, pPos, pState))
            pLevel.scheduleTick(pPos, this, 1);
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
    }
    
    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);
        if (!structureStillValid(pLevel, pPos, pState))
            destroyStructure(pPos, pState.getValue(AXIS), pLevel, true);
    }
    
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == getRotationAxis(state);
    }
    
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    }
    
    @Override
    public Class<GiantGearBlockEntity> getBlockEntityClass() {
        return GiantGearBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends GiantGearBlockEntity> getBlockEntityType() {
        return DesiresBlockEntityTypes.GIANT_GEAR.get();
    }
    
    @Override
    public float getParticleTargetRadius() {
        return 2.5f;
    }
    
    @Override
    public float getParticleInitialRadius() {
        return 2.25f;
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        BlockPos origin = context.getClickedPos();
        assert stateForPlacement != null;
        Direction.Axis axis = stateForPlacement.getValue(AXIS);
        
        if (!isClearForStructure(axis, origin, context.getLevel()))
            return null;
        
        return stateForPlacement;
    }
    
    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        Direction.Axis axis = state.getValue(AXIS);
        
        for (Map.Entry<Vec3i, BlockState> entry : getStructureByAxis().get(axis).entrySet()) {
            if (!entry.getKey().equals(new Vec3i(0, 0, 0)))
                worldIn.setBlock(pos.offset(entry.getKey()), entry.getValue(), 3);
        }
    }
    
    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        destroyStructure(pPos, pState.getValue(AXIS), pLevel, pPlayer.isCreative());
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }
    
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        destroyStructure(pPos, pState.getValue(AXIS), pLevel, true);
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
    
    public static boolean isClearForStructure(Direction.Axis axis, BlockPos origin, LevelReader level) {
        HashMap<Vec3i, BlockState> structure = getStructureByAxis().get(axis);
        for (Map.Entry<Vec3i, BlockState> entry : structure.entrySet()) {
            if (!level.getBlockState(origin.offset(entry.getKey())).is(Blocks.AIR))
                return false;
        }
        return true;
    }
    
    /**
     * @param pPos   the position of a block in the structure
     * @param pState the state of a block in the structure
     *
     * @return whether the structure is valid
     */
    public static boolean structureStillValid(LevelReader pLevel, BlockPos pPos, BlockState pState) {
        Block block = pState.getBlock();
        
        if (pState.is(DesiresBlocks.GIANT_GEAR_STRUCTURAL.get())
            && block instanceof GiantGearStructuralBlock) {
            
            //This is the parent, so check
            BlockPos ownerPos = GiantGearStructuralBlock.getOwner(pLevel, pPos, pState);
            BlockState ownerState = pLevel.getBlockState(ownerPos);
            return structureStillValid(pLevel, ownerPos, ownerState);
            
        } else if (pState.is(DesiresBlocks.GIANT_GEAR.get())
            && block instanceof GiantGearBlock) {
            
            //Look for a parent to check for
            Direction.Axis axis = pState.getValue(AXIS);
            HashMap<Vec3i, BlockState> structure = getStructureByAxis().get(axis);
            for (Map.Entry<Vec3i, BlockState> entry : structure.entrySet()) {
                BlockPos blockPos = pPos.offset(entry.getKey());
                BlockState blockState = pLevel.getBlockState(blockPos);
                if (blockState != entry.getValue())
                    return false;
            }
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Removes blocks in the associated axis' structure
     */
    public static void destroyStructure(BlockPos origin, Direction.Axis axis, LevelAccessor level, boolean drops) {
        HashMap<Vec3i, BlockState> structure = getStructureByAxis().get(axis);
        for (Vec3i pos : structure.keySet()) {
            if (level.getBlockState(origin.offset(pos)).equals(structure.get(pos)))
                level.destroyBlock(origin.offset(pos), drops);
        }
    }
    
    private static HashMap<Direction.Axis, HashMap<Vec3i, BlockState>> generateStructuresByAxis() {
        HashMap<Direction.Axis, HashMap<Vec3i, BlockState>> result = new HashMap<>();
        for (Direction.Axis axis : Iterate.axisSet) {
            result.put(axis, generateStructureForAxis(axis));
        }
        return result;
    }
    
    private static HashMap<Vec3i, BlockState> generateStructureForAxis(Direction.Axis axis) {
        HashMap<Vec3i, BlockState> result = new HashMap<>();
        
        List<Direction.Axis> parallelAxis = Arrays.stream(Direction.Axis.values())
            .filter(other -> other != axis)
            .toList();
        
        Direction directionA = Direction.fromAxisAndDirection(parallelAxis.get(0), Direction.AxisDirection.POSITIVE);
        Direction directionB = Direction.fromAxisAndDirection(parallelAxis.get(1), Direction.AxisDirection.POSITIVE);
        
        Vec3i origin = new Vec3i(0, 0, 0);
        
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                if (Math.pow(i, 2) + Math.pow(j, 2) > 12) continue;
                
                Vec3i pos = origin
                    .relative(directionA, i)
                    .relative(directionB, j);
                
                //If this is the center then just place the main block
                if (i == 0 && j == 0) {
                    result.put(
                        pos, DesiresBlocks.GIANT_GEAR.get()
                            .defaultBlockState()
                            .setValue(GiantGearBlock.AXIS, axis)
                    );
                    continue;
                }
                
                //Otherwise fill out the child blocks
                result.put(
                    pos, DesiresBlocks.GIANT_GEAR_STRUCTURAL.get()
                        .defaultBlockState()
                        .setValue(GiantGearStructuralBlock.FACING, Direction.getNearest(pos.getX(), pos.getY(), pos.getZ()).getOpposite())
                );
            }
        }
        
        return result;
    }
    
}
