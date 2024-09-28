package uwu.lopyluna.create_dd.content.blocks.logistics.item_stockpile;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import uwu.lopyluna.create_dd.registry.DesiresSpriteShifts;

public class ItemStockpileCTBehaviour extends ConnectedTextureBehaviour.Base {

	@Override
	public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
		Axis vaultBlockAxis = ItemStockpileBlock.getVaultBlockAxis(state);
		boolean small = !ItemStockpileBlock.isLarge(state);
		if (vaultBlockAxis == null)
			return null;

		if (direction == Direction.UP)
			return DesiresSpriteShifts.SOCKPILE_TOP.get(small);
		if (direction == Direction.DOWN)
			return DesiresSpriteShifts.SOCKPILE_BOTTOM.get(small);

		return DesiresSpriteShifts.SOCKPILE_SIDE.get(small);
	}

	public boolean buildContextForOccludedDirections() {
		return super.buildContextForOccludedDirections();
	}

	@Override
	public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos,
		BlockPos otherPos, Direction face) {
		return state == other && ConnectivityHandler.isConnected(reader, pos, otherPos);
	}

}
