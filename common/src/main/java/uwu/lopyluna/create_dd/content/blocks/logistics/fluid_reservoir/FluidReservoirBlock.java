package uwu.lopyluna.create_dd.content.blocks.logistics.fluid_reservoir;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.registry.DesiresBlockEntityTypes;

import javax.annotation.Nullable;

@MethodsReturnNonnullByDefault
@SuppressWarnings({"deprecation", "removal", "all"})
public class FluidReservoirBlock extends Block implements IWrenchable, IBE<FluidReservoirBlockEntity> {

    public static final Property<Axis> HORIZONTAL_AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    
    public static final BooleanProperty LARGE = BooleanProperty.create("large");
    
    public static final BooleanProperty WINDOW = BooleanProperty.create("window");

    public FluidReservoirBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
        registerDefaultState(defaultBlockState().setValue(TOP, true)
                .setValue(BOTTOM, true).setValue(LARGE, false)
                .setValue(WINDOW, true));
    }

    public static boolean isTank(BlockState state) {
        return state.getBlock() instanceof FluidReservoirBlock;
    }

    @Override
    public void onPlace(BlockState state, @NotNull Level world, @NotNull BlockPos pos, BlockState oldState, boolean moved) {
        if (oldState.getBlock() == state.getBlock())
            return;
        if (moved)
            return;
        withBlockEntityDo(world, pos, FluidReservoirBlockEntity::updateConnectivity);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(TOP, BOTTOM, HORIZONTAL_AXIS, LARGE, WINDOW);
        super.createBlockStateDefinition(p_206840_1_);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        if (pContext.getPlayer() == null || !pContext.getPlayer()
                .isSteppingCarefully()) {
            BlockState placedOn = pContext.getLevel()
                    .getBlockState(pContext.getClickedPos()
                            .relative(pContext.getClickedFace()
                                    .getOpposite()));
            Axis preferredAxis = getKegAxis(placedOn);
            if (preferredAxis != null)
                return this.defaultBlockState()
                        .setValue(HORIZONTAL_AXIS, preferredAxis);
        }
        return this.defaultBlockState()
                .setValue(HORIZONTAL_AXIS, pContext.getHorizontalDirection()
                        .getAxis());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        boolean skip = super.skipRendering(state, adjacentBlockState, side);
        return adjacentBlockState.getBlock() instanceof FluidReservoirBlock && state.getValue(WINDOW) ? true : skip;
    }

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return pState.getValue(WINDOW) ? Shapes.empty() : super.getVisualShape(pState, pReader, pPos, pContext);
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.getValue(WINDOW) ? 1.0F : super.getShadeBrightness(pState, pLevel, pPos);
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
        return state.getValue(WINDOW) ? true : super.shouldDisplayFluidOverlay(state, world, pos, fluidState);
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return state.getValue(WINDOW) ? false : super.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    @Override
    public boolean supportsExternalFaceHiding(BlockState state) {
        return state.getValue(WINDOW) ? false : super.supportsExternalFaceHiding(state);
    }

    @Override
    public boolean isOcclusionShapeFullBlock(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return pState.getValue(WINDOW) ? false : super.isOcclusionShapeFullBlock(pState, pReader, pPos);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return pState.getValue(WINDOW) ? RenderShape.MODEL : super.getRenderShape(pState);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        FluidReservoirBlockEntity tankAt = ConnectivityHandler.partAt(getBlockEntityType(), world, pos);
        if (tankAt == null)
            return 0;
        FluidReservoirBlockEntity controllerBE = tankAt.getControllerBE();
        if (controllerBE == null || !controllerBE.window)
            return 0;
        return tankAt.luminosity;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (context.getClickedFace()
                .getAxis()
                .isVertical()) {
            BlockEntity be = context.getLevel()
                    .getBlockEntity(context.getClickedPos());
            if (be instanceof FluidReservoirBlockEntity keg) {
                ConnectivityHandler.splitMulti(keg);
                keg.removeController(true);
            }
            state = state.setValue(LARGE, false);
        }
        if (context.getClickedFace().getAxis().isHorizontal()) {
            withBlockEntityDo(context.getLevel(), context.getClickedPos(), FluidReservoirBlockEntity::toggleWindows);
        }
        return IWrenchable.super.onWrenched(state, context);
    }

    static final VoxelShape CAMPFIRE_SMOKE_CLIP = Block.box(0, 4, 0, 16, 16, 16);

    @Override
    public VoxelShape getCollisionShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos,
                                        @NotNull CollisionContext pContext) {
        if (pContext == CollisionContext.empty())
            return CAMPFIRE_SMOKE_CLIP;
        return pState.getShape(pLevel, pPos);
    }

    @Override
    public VoxelShape getBlockSupportShape(@NotNull BlockState pState, @NotNull BlockGetter pReader, @NotNull BlockPos pPos) {
        return Shapes.block();
    }

    //@Override
    //public BlockState updateShape(@NotNull BlockState pState, @NotNull Direction pDirection, @NotNull BlockState pNeighborState,
    //                              @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pNeighborPos) {
    //    if (pDirection == Direction.DOWN && pNeighborState.getBlock() != this)
    //        withBlockEntityDo(pLevel, pCurrentPos, FluidReservoirBlockEntity::updateConnectivity);
    //    return pState;
    //}

    @Override
    public InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand,
                                 @NotNull BlockHitResult ray) {
        ItemStack heldItem = player.getItemInHand(hand);
        boolean onClient = world.isClientSide;

        if (heldItem.isEmpty())
            return InteractionResult.PASS;
        if (!player.isCreative())
            return InteractionResult.PASS;

        FluidHelper.FluidExchange exchange = null;
        FluidReservoirBlockEntity be = ConnectivityHandler.partAt(getBlockEntityType(), world, pos);
        if (be == null)
            return InteractionResult.FAIL;

        LazyOptional<IFluidHandler> tankCapability = be.getCapability(net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        if (!tankCapability.isPresent())
            return InteractionResult.PASS;
        IFluidHandler fluidTank = tankCapability.orElse(null);
        FluidStack prevFluidInTank = fluidTank.getFluidInTank(0).copy();

        if (FluidHelper.tryEmptyItemIntoBE(world, player, hand, heldItem, be))
            exchange = FluidHelper.FluidExchange.ITEM_TO_TANK;
        else if (FluidHelper.tryFillItemFromBE(world, player, hand, heldItem, be))
            exchange = FluidHelper.FluidExchange.TANK_TO_ITEM;

        if (exchange == null) {
            if (GenericItemEmptying.canItemBeEmptied(world, heldItem)
                    || GenericItemFilling.canItemBeFilled(world, heldItem))
                return InteractionResult.SUCCESS;
            return InteractionResult.PASS;
        }

        SoundEvent soundevent = null;
        BlockState fluidState = null;
        FluidStack fluidInTank = tankCapability.map(fh -> fh.getFluidInTank(0))
                .orElse(FluidStack.EMPTY);

        if (exchange == FluidHelper.FluidExchange.ITEM_TO_TANK) {
            Fluid fluid = fluidInTank.getFluid();
            fluidState = fluid.defaultFluidState()
                    .createLegacyBlock();
            soundevent = FluidHelper.getEmptySound(fluidInTank);
        }

        if (exchange == FluidHelper.FluidExchange.TANK_TO_ITEM) {
            Fluid fluid = prevFluidInTank.getFluid();
            fluidState = fluid.defaultFluidState()
                    .createLegacyBlock();
            soundevent = FluidHelper.getFillSound(prevFluidInTank);
        }

        if (soundevent != null && !onClient) {
            float pitch = Mth
                    .clamp(1 - (1f * fluidInTank.getAmount() / (FluidReservoirBlockEntity.getCapacityMultiplier() * 16)), 0, 1);
            pitch /= 1.5f;
            pitch += .5f;
            pitch += (world.random.nextFloat() - .5f) / 4f;
            world.playSound(null, pos, soundevent, SoundSource.BLOCKS, .5f, pitch);
        }

        if (!fluidInTank.isFluidStackIdentical(prevFluidInTank)) {
            if (be instanceof FluidReservoirBlockEntity) {
                FluidReservoirBlockEntity controllerBE = ((FluidReservoirBlockEntity) be).getControllerBE();
                if (controllerBE != null) {
                    if (onClient) {
                        BlockParticleOption blockParticleData =
                                new BlockParticleOption(ParticleTypes.BLOCK, fluidState);
                        float level = (float) fluidInTank.getAmount() / fluidTank.getTankCapacity(0);

                        boolean reversed = fluidInTank.getFluid()
                                .getFluidType()
                                .isLighterThanAir();
                        if (reversed)
                            level = 1 - level;

                        Vec3 vec = ray.getLocation();
                        vec = new Vec3(vec.x, controllerBE.getBlockPos()
                                .getY() + level * (controllerBE.height - .5f) + .25f, vec.z);
                        Vec3 motion = player.position()
                                .subtract(vec)
                                .scale(1 / 20f);
                        vec = vec.add(motion);
                        world.addParticle(blockParticleData, vec.x, vec.y, vec.z, motion.x, motion.y, motion.z);
                        return InteractionResult.SUCCESS;
                    }

                    controllerBE.sendDataImmediately();
                    controllerBE.setChanged();
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean pIsMoving) {
        if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
            BlockEntity be = world.getBlockEntity(pos);
            if (!(be instanceof FluidReservoirBlockEntity kegBE))
                return;
            world.removeBlockEntity(pos);
            ConnectivityHandler.splitMulti(kegBE);
        }
    }

    @Override
    public Class<FluidReservoirBlockEntity> getBlockEntityClass() {
        return FluidReservoirBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FluidReservoirBlockEntity> getBlockEntityType() {
        return DesiresBlockEntityTypes.FLUID_RESERVOIR.get();
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirrorIn) {
        return state;
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        Axis axis = state.getValue(HORIZONTAL_AXIS);
        return state.setValue(HORIZONTAL_AXIS, rot.rotate(Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE))
                .getAxis());
    }

    @Nullable
    public static Axis getKegAxis(BlockState state) {
        if (!isTank(state))
            return null;
        return state.getValue(HORIZONTAL_AXIS);
    }

    public static boolean isLarge(BlockState state) {
        if (!isTank(state))
            return false;
        return state.getValue(LARGE);
    }

    // Tanks are less noisy when placed in batch
    public static final SoundType SILENCED_METAL =
            new ForgeSoundType(0.1F, 1.5F, () -> SoundEvents.COPPER_BREAK, () -> SoundEvents.COPPER_STEP,
                    () -> SoundEvents.COPPER_PLACE, () -> SoundEvents.COPPER_HIT, () -> SoundEvents.COPPER_FALL);

    @Override
    public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
        SoundType soundType = super.getSoundType(state, world, pos, entity);
        if (entity != null && entity.getPersistentData()
                .contains("SilenceTankSound"))
            return SILENCED_METAL;
        return soundType;
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState p_149740_1_) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos) {
        return getBlockEntityOptional(pLevel, pPos).map(FluidReservoirBlockEntity::getControllerBE)
                .map(be -> ComparatorUtil.fractionToRedstoneLevel(be.getFillState()))
                .orElse(0);
    }
}