package uwu.lopyluna.create_dd.content.blocks.logistics.item_stockpile;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.common.util.ForgeSoundType;
import uwu.lopyluna.create_dd.registry.DesiresBlockEntityTypes;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.Objects;

@SuppressWarnings({"removal", "deprecation"})
@ParametersAreNullableByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemStockpileBlock extends Block implements IWrenchable, IBE<ItemStockpileBlockEntity> {
	public static final BooleanProperty LARGE = BooleanProperty.create("large");

	public ItemStockpileBlock(Properties p_i48440_1_) {
		super(Objects.requireNonNull(p_i48440_1_));
		registerDefaultState(defaultBlockState().setValue(LARGE, false));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        assert pBuilder != null;
        pBuilder.add(LARGE);
		super.createBlockStateDefinition(pBuilder);
	}

	@Override
	public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        assert pOldState != null;
        assert pState != null;
        if (pOldState.getBlock() == pState.getBlock())
			return;
		if (pIsMoving)
			return;
		withBlockEntityDo(pLevel, pPos, ItemStockpileBlockEntity::updateConnectivity);
	}

	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        assert context != null;
        if (context.getClickedFace().getAxis().isVertical()) {
			BlockEntity be = context.getLevel()
				.getBlockEntity(context.getClickedPos());
			if (be instanceof ItemStockpileBlockEntity vault) {
                ConnectivityHandler.splitMulti(vault);
				vault.removeController(true);
			}
            assert state != null;
            state = state.setValue(LARGE, false);
		}
        return IWrenchable.super.onWrenched(state, context);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean pIsMoving) {
        assert state != null;
        if (state.hasBlockEntity()) {
            assert newState != null;
            if (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity()) {
                assert world != null;
                assert pos != null;
                BlockEntity be = world.getBlockEntity(pos);
                if (!(be instanceof ItemStockpileBlockEntity vaultBE))
                    return;
                ItemHelper.dropContents(world, pos, vaultBE.inventory);
                world.removeBlockEntity(pos);
                ConnectivityHandler.splitMulti(vaultBE);
            }
        }
	}

	public static boolean isVault(BlockState state) {
        assert state != null;
        return DesiresBlocks.ITEM_STOCKPILE.has(state);
	}

	@Nullable
	public static Axis getVaultBlockAxis(BlockState state) {
		if (!isVault(state))
			return null;
		return Axis.Y;
	}

	public static boolean isLarge(BlockState state) {
		if (!isVault(state))
			return false;
        return state.getValue(LARGE);
	}

	// Vaults are less noisy when placed in batch
	public static final SoundType SILENCED_METAL =
		new ForgeSoundType(0.1F, 1.5F, () -> SoundEvents.NETHERITE_BLOCK_BREAK, () -> SoundEvents.NETHERITE_BLOCK_STEP,
			() -> SoundEvents.NETHERITE_BLOCK_PLACE, () -> SoundEvents.NETHERITE_BLOCK_HIT,
			() -> SoundEvents.NETHERITE_BLOCK_FALL);

	@Override
	public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
		SoundType soundType = super.getSoundType(state, world, pos, entity);
		if (entity != null && entity.getPersistentData()
			.contains("SilenceVaultSound"))
			return SILENCED_METAL;
		return soundType;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState p_149740_1_) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
		return getBlockEntityOptional(pLevel, pPos)
			.map(vte -> vte.getCapability(net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
			.map(lo -> lo.map(ItemHelper::calcRedstoneFromInventory)
				.orElse(0))
			.orElse(0);
	}

	@Override
	public BlockEntityType<? extends ItemStockpileBlockEntity> getBlockEntityType() {
		return DesiresBlockEntityTypes.ITEM_STOCKPILE.get();
	}

	@Override
	public Class<ItemStockpileBlockEntity> getBlockEntityClass() {
		return ItemStockpileBlockEntity.class;
	}
}
