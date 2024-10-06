package uwu.lopyluna.create_dd.content.items.equipment.jetpack;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.registry.DesiresBlockEntityTypes;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JetpackBlock extends HorizontalKineticBlock implements IBE<JetpackBlockEntity>, SimpleWaterloggedBlock, ISpecialBlockItemRequirement {

    public JetpackBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false)
                : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void fillItemCategory(CreativeModeTab pTab, NonNullList<ItemStack> pItems) {}

    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    @Override
    public boolean hasAnalogOutputSignal(BlockState p_149740_1_) {
        return true;
    }

    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return getBlockEntityOptional(world, pos).map(JetpackBlockEntity::getComparatorOutput)
                .orElse(0);
    }

    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world,
                                           BlockPos pos, BlockPos neighbourPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED))
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel()
                .getFluidState(context.getClickedPos());
        return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(BlockStateProperties.WATERLOGGED,
                fluidState.getType() == Fluids.WATER);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.UP;
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (worldIn.isClientSide)
            return;
        withBlockEntityDo(worldIn, pos, be -> {
            be.setAirLevel(stack.getOrCreateTag()
                    .getInt("Air"));
            CompoundTag vanillaTag = stack.getOrCreateTag();
            if (stack.hasCustomHoverName())
                be.setCustomName(stack.getHoverName());

            CompoundTag nbt = stack.serializeNBT();
            CompoundTag forgeCapsTag = nbt.contains("ForgeCaps") ? nbt.getCompound("ForgeCaps") : null;
            be.setTags(vanillaTag, forgeCapsTag);
        });
    }

    @Override
    @SuppressWarnings("deprecation")
    // Re-adding ForgeCaps to item here as there is no loot function that can modify
    // outside of the vanilla tag
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState pState, LootContext.@NotNull Builder pBuilder) {
        List<ItemStack> lootDrops = super.getDrops(pState, pBuilder);

        BlockEntity blockEntity = pBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (!(blockEntity instanceof JetpackBlockEntity bbe))
            return lootDrops;

        CompoundTag forgeCapsTag = bbe.getForgeCapsTag();
        if (forgeCapsTag == null)
            return lootDrops;

        return lootDrops.stream()
                .map(stack -> {
                    if (!(stack.getItem() instanceof JetpackItem))
                        return stack;

                    ItemStack modifiedStack = new ItemStack(stack.getItem(), stack.getCount(), forgeCapsTag.copy());
                    modifiedStack.setTag(stack.getTag());
                    return modifiedStack;
                })
                .toList();
    }

    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                          BlockHitResult hit) {
        if (player instanceof FakePlayer)
            return InteractionResult.PASS;
        if (player.isShiftKeyDown())
            return InteractionResult.PASS;
        if (player.getMainHandItem()
                .getItem() instanceof BlockItem)
            return InteractionResult.PASS;
        if (!player.getItemBySlot(EquipmentSlot.CHEST)
                .isEmpty())
            return InteractionResult.PASS;
        if (!world.isClientSide) {
            world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .75f, 1);
            player.setItemSlot(EquipmentSlot.CHEST, getCloneItemStack(world, pos, state));
            world.destroyBlock(pos, false);
        }
        return InteractionResult.SUCCESS;
    }

    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos pos, BlockState state) {
        Item item = asItem();
        if (item instanceof JetpackItem.JetpackBlockItem placeable)
            item = placeable.asItem();

        Optional<JetpackBlockEntity> blockEntityOptional = getBlockEntityOptional(blockGetter, pos);

        CompoundTag forgeCapsTag = blockEntityOptional.map(JetpackBlockEntity::getForgeCapsTag)
                .orElse(null);
        CompoundTag vanillaTag = blockEntityOptional.map(JetpackBlockEntity::getVanillaTag)
                .orElse(new CompoundTag());
        int air = blockEntityOptional.map(JetpackBlockEntity::getAirLevel)
                .orElse(0);

        ItemStack stack = new ItemStack(item, 1, forgeCapsTag);
        vanillaTag.putInt("Air", air);
        stack.setTag(vanillaTag);
        return stack;
    }

    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                                        CollisionContext p_220053_4_) {
        return AllShapes.BACKTANK;
    }

    @Override
    public Class<JetpackBlockEntity> getBlockEntityClass() {
        return JetpackBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends JetpackBlockEntity> getBlockEntityType() {
        return DesiresBlockEntityTypes.JETPACK.get();
    }

    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        Item item = asItem();
        if (item instanceof JetpackItem.JetpackBlockItem placeable)
            item = placeable.asItem();
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, item);
    }

}