package uwu.lopyluna.create_dd.content.blocks.kinetics.modular_drill;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import uwu.lopyluna.create_dd.registry.DesiresBlockEntityTypes;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class ModularDrillBlock extends DirectionalKineticBlock implements IBE<ModularDrillBlockEntity>, SimpleWaterloggedBlock {
    public static final EnumProperty<ModularDrillHeadItem.DrillType> DRILL_TYPE = EnumProperty.create("drill_type", ModularDrillHeadItem.DrillType.class);
    public static DamageSource damageSourceDrill = new DamageSource("create.mechanical_drill").bypassArmor();

    private static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());

    public ModularDrillBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(DRILL_TYPE, ModularDrillHeadItem.DrillType.VEIN));
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof ItemEntity)
            return;
        if (!new AABB(pos).deflate(.1f)
                .intersects(entityIn.getBoundingBox()))
            return;
        withBlockEntityDo(worldIn, pos, be -> {
            if (be.getSpeed() == 0)
                return;
            entityIn.hurt(damageSourceDrill, (float) getDamage(be.getSpeed()));
        });
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.CASING_12PX.get(state.getValue(FACING));
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        withBlockEntityDo(worldIn, pos, ModularDrillBlockEntity::destroyNextTick);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);

        if (!world.isClientSide && heldItem.getItem() instanceof ModularDrillHeadItem drillHeadItem) {
            ModularDrillHeadItem.DrillType newDrillType = drillHeadItem.getDrillType();

            if (state.getValue(DRILL_TYPE) == newDrillType && newDrillType != ModularDrillHeadItem.DrillType.ENCHANTABLE) {
                return InteractionResult.PASS;
            }

            withBlockEntityDo(world, pos, be -> {
                be.setDrillHeadType(newDrillType);
                be.updateDrillHeadEnchantmentsFromItem(heldItem);
                player.getCooldowns().addCooldown(heldItem.getItem(), 10);
            });

            if (!player.isCreative()) {
                heldItem.shrink(1);
            }
            world.setBlock(pos, state.setValue(DRILL_TYPE, newDrillType), 3);
            return InteractionResult.SUCCESS;
        } else if (heldItem.getItem() instanceof ModularDrillHeadItem) {
            player.playSound(SoundEvents.NETHERITE_BLOCK_PLACE, 1, 2);
        } else if (!player.isShiftKeyDown() && player.mayBuild()) {
            if (placementHelper.matchesItem(heldItem)) {
                placementHelper.getOffset(player, world, state, pos, hit)
                        .placeInWorld(world, (BlockItem) heldItem.getItem(), player, hand, hit);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }


    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING)
                .getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING)
                .getOpposite();
    }
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.NORMAL;
    }
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED, DRILL_TYPE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState,
                                  LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED))
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState FluidState = context.getLevel().getFluidState(context.getClickedPos());
        return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(BlockStateProperties.WATERLOGGED, FluidState.getType() == Fluids.WATER);
    }

    public static double getDamage(float speed) {
        float speedAbs = Math.abs(speed);
        double sub1 = Math.min(speedAbs / 16, 2);
        double sub2 = Math.min(speedAbs / 32, 4);
        double sub3 = Math.min(speedAbs / 64, 4);
        return Mth.clamp(sub1 + sub2 + sub3, 1, 10);
    }


    @Override
    public Class<ModularDrillBlockEntity> getBlockEntityClass() {
        return ModularDrillBlockEntity.class;
    }

    @Nullable
    @Override
    public BlockEntityType<? extends ModularDrillBlockEntity> getBlockEntityType() {
        return DesiresBlockEntityTypes.DRILL.get();
    }


    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return DesiresBlocks.MODULAR_DRILL::isIn;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return DesiresBlocks.MODULAR_DRILL::has;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos,
                                         BlockHitResult ray) {
            List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(),
                    state.getValue(FACING)
                            .getAxis(),
                    dir -> world.getBlockState(pos.relative(dir))
                            .getMaterial()
                            .isReplaceable());

            if (directions.isEmpty())
                return PlacementOffset.fail();
            else {
                return PlacementOffset.success(pos.relative(directions.get(0)),
                        s -> s.setValue(FACING, state.getValue(FACING)));
            }
        }
    }
}
