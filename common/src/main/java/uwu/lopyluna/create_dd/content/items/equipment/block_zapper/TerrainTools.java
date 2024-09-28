package uwu.lopyluna.create_dd.content.items.equipment.block_zapper;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.zapper.ZapperItem;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import uwu.lopyluna.create_dd.registry.DesiresTags;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("unused")
public enum TerrainTools {

	Fill(AllIcons.I_FILL),
	Place(AllIcons.I_PLACE),
	Replace(AllIcons.I_REPLACE),
	Clear(AllIcons.I_CLEAR),
	Overlay(AllIcons.I_OVERLAY),

	;

	public final String translationKey;
	public final AllIcons icon;

	TerrainTools(AllIcons icon) {
		this.translationKey = Lang.asId(name());
		this.icon = icon;
	}

	public boolean requiresSelectedBlock() {
		return this != Clear;
	}

	public void clear(BlockState paintedState, Player player, int amount) {
		BlockHelper.findAndRemoveInInventory(paintedState, player, amount);
	}

	public void run(Level world, List<BlockPos> targetPositions, Direction facing, @Nullable BlockState paintedState, @Nullable CompoundTag data, Player player, BlockHitResult raytrace, ItemStack zapper, boolean toVoid) {
		assert paintedState != null;
		switch (this) {
		case Clear:
			targetPositions.forEach(p -> {
				BlockState toReplace = world.getBlockState(p);
				if (blacklist(toReplace, world, p)) {
					SoundType sound = toReplace.getSoundType();
					world.playSound(player, p.getX(), p.getY(), p.getZ(), sound.getBreakSound(), SoundSource.BLOCKS, sound.getVolume(), sound.getPitch());
					zapper.hurtAndBreak(1, player, b -> b.broadcastBreakEvent(player.getUsedItemHand()));
					return;
				}
				if (toReplace.getBlock().equals(Blocks.AIR) || toReplace.getBlock().equals(Blocks.CAVE_AIR) || toReplace.getBlock().equals(Blocks.VOID_AIR))
					return;
				if (!paintedState.canSurvive(world, p))
					return;
				if (!world.getWorldBorder().isWithinBounds(p))
					return;
				if (!player.isCreative()) {
					zapper.hurtAndBreak(1, player, b -> b.broadcastBreakEvent(player.getUsedItemHand()));
				}
				if (!toVoid) {
					world.destroyBlock(p, true, player);
				}
				BlockState clearState = Blocks.AIR.defaultBlockState();
				clearState.getBlock().setPlacedBy(world, p, clearState, player, zapper);
				world.gameEvent(GameEvent.BLOCK_PLACE, p, GameEvent.Context.of(player, clearState));
				world.setBlockAndUpdate(p, clearState);
			});
			break;
		case Fill:
			targetPositions.forEach(p -> {
				BlockState toReplace = world.getBlockState(p);
				if (blacklist(toReplace, world, p))
					return;
				if (!isReplaceable(toReplace))
					return;
				if (toReplace == paintedState)
					return;
				if (!paintedState.canSurvive(world, p))
					return;
				if (!world.getWorldBorder().isWithinBounds(p))
					return;
				if (!player.isCreative()) {
					zapper.hurtAndBreak(1, player, b -> b.broadcastBreakEvent(player.getUsedItemHand()));
					clear(paintedState, player, 1);
				}
				if (!toVoid) {
					world.destroyBlock(p, true, player);
				}
				paintedState.getBlock().setPlacedBy(world, p, paintedState, player, zapper);
				world.gameEvent(GameEvent.BLOCK_PLACE, p, GameEvent.Context.of(player, paintedState));
				world.setBlockAndUpdate(p, paintedState);
				ZapperItem.setBlockEntityData(world, p, paintedState, data, player);
				
			});
			break;
		case Overlay:
			targetPositions.forEach(p -> {
				BlockState toOverlay = world.getBlockState(p);
				if (blacklist(toOverlay, world, p))
					return;
				if (isReplaceable(toOverlay))
					return;
				if (toOverlay == paintedState)
					return;
				if (!paintedState.canSurvive(world, p))
					return;
				if (!world.getWorldBorder().isWithinBounds(p))
					return;
				p = p.above();
				BlockState toReplace = world.getBlockState(p);
				if (blacklist(toReplace, world, p))
					return;
				if (!isReplaceable(toReplace))
					return;
				if (toReplace == paintedState)
					return;
				if (!paintedState.canSurvive(world, p))
					return;
				if (!world.getWorldBorder().isWithinBounds(p))
					return;
				if (!player.isCreative()) {
					zapper.hurtAndBreak(1, player, b -> b.broadcastBreakEvent(player.getUsedItemHand()));
					clear(paintedState, player, 1);
				}
				if (!toVoid) {
					world.destroyBlock(p, true, player);
				}
				paintedState.getBlock().setPlacedBy(world, p, paintedState, player, zapper);
				world.gameEvent(GameEvent.BLOCK_PLACE, p, GameEvent.Context.of(player, paintedState));
				world.setBlockAndUpdate(p, paintedState);
				ZapperItem.setBlockEntityData(world, p, paintedState, data, player);
				
			});
			break;
		case Place:
			targetPositions.forEach(p -> {
				BlockState toReplace = world.getBlockState(p);
				if (blacklist(toReplace, world, p))
					return;
				if (toReplace == paintedState)
					return;
				if (!paintedState.canSurvive(world, p))
					return;
				if (!world.getWorldBorder().isWithinBounds(p))
					return;
				if (!player.isCreative()) {
					zapper.hurtAndBreak(1, player, b -> b.broadcastBreakEvent(player.getUsedItemHand()));
					clear(paintedState, player, 1);
				}
				if (!toVoid) {
					world.destroyBlock(p, true, player);
				}
				paintedState.getBlock().setPlacedBy(world, p, paintedState, player, zapper);
				world.gameEvent(GameEvent.BLOCK_PLACE, p, GameEvent.Context.of(player, paintedState));
				world.setBlockAndUpdate(p, paintedState);
				ZapperItem.setBlockEntityData(world, p, paintedState, data, player);
				
			});
			break;
		case Replace:
			targetPositions.forEach(p -> {
				BlockState toReplace = world.getBlockState(p);
				if (blacklist(toReplace, world, p))
					return;
				if (isReplaceable(toReplace))
					return;
				if (toReplace == paintedState)
					return;
				if (!paintedState.canSurvive(world, p))
					return;
				if (!world.getWorldBorder().isWithinBounds(p))
					return;
				if (!player.isCreative()) {
					zapper.hurtAndBreak(1, player, b -> b.broadcastBreakEvent(player.getUsedItemHand()));
					clear(paintedState, player, 1);
				}
				if (!toVoid) {
					world.destroyBlock(p, true, player);
				}
				paintedState.getBlock().setPlacedBy(world, p, paintedState, player, zapper);
				world.gameEvent(GameEvent.BLOCK_PLACE, p, GameEvent.Context.of(player, paintedState));
				world.setBlockAndUpdate(p, paintedState);
				ZapperItem.setBlockEntityData(world, p, paintedState, data, player);
				
			});
			break;
		}
	}

	public static boolean isReplaceable(BlockState toReplace) {
		return toReplace.getMaterial().isReplaceable() || toReplace.is(DesiresTags.AllBlockTags.BLOCK_ZAPPER_REPLACEABLE.tag);
	}

	public static boolean blacklist(BlockState toBlacklist, Level level, BlockPos toPos) {
		return toBlacklist.is(DesiresTags.AllBlockTags.BLOCK_ZAPPER_BLACKLIST.tag) ||
				toBlacklist.is(AllTags.AllBlockTags.NON_MOVABLE.tag);
	}
}
