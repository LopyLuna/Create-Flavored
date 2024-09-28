package uwu.lopyluna.create_dd.content.items.equipment.block_zapper.additional;

import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SphereBrush extends ShapedBrush {

	public final int MAX_RADIUS = 8;
	private final Map<Integer, List<BlockPos>> cachedBrushes;

	public SphereBrush() {
		super(1);

		cachedBrushes = new HashMap<>();
		for (int i = 0; i <= (getBulk() ? MAX_RADIUS : 4); i++) {
			int radius = i;
			List<BlockPos> positions =
				BlockPos.betweenClosedStream(BlockPos.ZERO.offset(-i - 1, -i - 1, -i - 1), BlockPos.ZERO.offset(i + 1, i + 1, i + 1))
						.map(BlockPos::new).filter(p -> VecHelper.getCenterOf(p)
								.distanceTo(VecHelper.getCenterOf(BlockPos.ZERO)) < radius + .5f)
						.collect(Collectors.toList());
			cachedBrushes.put(i, positions);
		}
	}

	@Override
	public BlockPos getOffset(Vec3 ray, Direction face, PlacementOptions option) {
		if (option == PlacementOptions.Merged)
			return BlockPos.ZERO;

		int offset = option == PlacementOptions.Attached ? 0 : -1;
		int r = (param0 + 1 + offset);

		return BlockPos.ZERO.relative(face, r * (option == PlacementOptions.Attached ? 1 : -1));
	}

	@Override
    public int getMax(int paramIndex) {
		return getBulk() ? MAX_RADIUS : 4;
	}

	@Override
    public Component getParamLabel(int paramIndex) {
		return Lang.translateDirect("generic.radius");
	}

	@Override
	List<BlockPos> getIncludedPositions() {
		return cachedBrushes.get(param0);
	}

}
