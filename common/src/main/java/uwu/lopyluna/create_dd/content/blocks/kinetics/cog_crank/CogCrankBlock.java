package uwu.lopyluna.create_dd.content.blocks.kinetics.cog_crank;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogwheelBlockItem;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;
import uwu.lopyluna.create_dd.registry.DesiresBlockEntityTypes;
import uwu.lopyluna.create_dd.registry.DesiresShapes;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class CogCrankBlock extends RotatedPillarKineticBlock implements IBE<CogCrankBlockEntity>, ICogWheel, ProperWaterloggedBlock {
    
    boolean isLarge;
    
    public CogCrankBlock(boolean large, Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
        isLarge = large;
    }

    @Override
    public boolean isDedicatedCogWheel() {
        return true;
    }
    
    public static CogCrankBlock small(Properties properties) {
        return new CogCrankBlock(false, properties);
    }
    
    public static CogCrankBlock large(Properties properties) {
        return new CogCrankBlock(true, properties);
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(WATERLOGGED));
    }
    
    public int getRotationSpeed() {
        return 32;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return (isLarge ? DesiresShapes.SMALL_COG_CRANK : DesiresShapes.LARGE_COG_CRANK).get(state.getValue(AXIS));
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
                                 BlockHitResult hit) {
        if (player.isSpectator())
            return InteractionResult.PASS;
        if (player.getItemInHand(handIn).getItem() instanceof CogwheelBlockItem)
            return InteractionResult.PASS;
        if (player.getItemInHand(handIn).getItem() instanceof CogCrankBlockItem)
            return InteractionResult.PASS;
        withBlockEntityDo(worldIn, pos, be -> be.turn(player.isShiftKeyDown()));
        if (!player.getItemInHand(handIn)
            .is(AllItems.EXTENDO_GRIP.get()))
            player.causeFoodExhaustion(getRotationSpeed() * DesiresConfigs.server().kinetics.cogCrankHungerMultiplier.getF());
        
        if (player.getFoodData()
            .getFoodLevel() == 0)
            AllAdvancements.HAND_CRANK.awardTo(player);
        
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return isValidCogwheelPosition(ICogWheel.isLargeCog(state), worldIn, pos, state.getValue(AXIS));
    }
    
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return false;
    }
    
    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    }
    
    @Override
    public Class<CogCrankBlockEntity> getBlockEntityClass() {
        return CogCrankBlockEntity.class;
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    
    @Override
    public BlockEntityType<? extends CogCrankBlockEntity> getBlockEntityType() {
        return DesiresBlockEntityTypes.COG_CRANK.get();
    }
    
    protected Axis getAxisForPlacement(BlockPlaceContext context) {
        if (context.getPlayer() != null && context.getPlayer()
            .isShiftKeyDown())
            return context.getClickedFace().getAxis();
        
        Level world = context.getLevel();
        BlockState stateBelow = world.getBlockState(context.getClickedPos()
            .below());
        
        if (AllBlocks.ROTATION_SPEED_CONTROLLER.has(stateBelow) && isLargeCog())
            return stateBelow.getValue(SpeedControllerBlock.HORIZONTAL_AXIS) == Axis.X ? Axis.X : Axis.Z;
        
        BlockPos placedOnPos = context.getClickedPos()
            .relative(context.getClickedFace()
                .getOpposite());
        BlockState placedAgainst = world.getBlockState(placedOnPos);
        
        Block block = placedAgainst.getBlock();
        if (ICogWheel.isSmallCog(placedAgainst))
            return ((IRotate) block).getRotationAxis(placedAgainst);
        
        Axis preferredAxis = getPreferredAxis(context);
        return preferredAxis != null ? preferredAxis
            : context.getClickedFace().getAxis();
    }
    
    public static boolean isValidCogwheelPosition(boolean large, LevelReader worldIn, BlockPos pos, Axis cogAxis) {
        for (Direction facing : Iterate.directions) {
            if (facing.getAxis() == cogAxis)
                continue;
            
            BlockPos offsetPos = pos.relative(facing);
            BlockState blockState = worldIn.getBlockState(offsetPos);
            if (blockState.hasProperty(AXIS) && facing.getAxis() == blockState.getValue(AXIS))
                continue;
            
            if (ICogWheel.isLargeCog(blockState) || large && ICogWheel.isSmallCog(blockState))
                return false;
        }
        return true;
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean shouldWaterlog = context.getLevel()
                .getFluidState(context.getClickedPos())
                .getType() == Fluids.WATER;
        return this.defaultBlockState()
                .setValue(AXIS, getAxisForPlacement(context))
                .setValue(BlockStateProperties.WATERLOGGED, shouldWaterlog);
    }
    
    public static Couple<Integer> getSpeedRange() {
        return Couple.create(32, 32);
    }
    
    @Override
    public float getParticleTargetRadius() {
        return isLargeCog() ? 1.125f : .65f;
    }
    
    @Override
    public float getParticleInitialRadius() {
        return isLargeCog() ? 1f : .75f;
    }
    
    @Override
    public boolean isLargeCog() {
        return isLarge;
    }
    
    @Override
    public boolean isSmallCog() {
        return !isLarge;
    }
    
}