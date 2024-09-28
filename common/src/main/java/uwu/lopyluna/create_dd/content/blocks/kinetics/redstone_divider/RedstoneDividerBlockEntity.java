package uwu.lopyluna.create_dd.content.blocks.kinetics.redstone_divider;

import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RedstoneDividerBlockEntity extends SplitShaftBlockEntity {

    public RedstoneDividerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        //have to do this due to a crash on the same value or half of said value
    }

    @Override
    public float getRotationSpeedModifier(Direction face) {
        int power = getBlockState().getValue(BlockStateProperties.POWER);
        if (hasSource()) {
            if (face != getSourceFacing())
                return power == 0 ? 1f :
                       power == 1 ? 1f :
                       power == 2 ? 1f :
                       power == 3 ? 0.75f :
                       power == 4 ? 0.75f :
                       power == 5 ? 0.75f :
                       power == 6 ? 0.5f :
                       power == 7 ? 0.5f :
                       power == 8 ? 0.5f :
                       power == 9 ? 0.25f :
                       power == 10 ? 0.25f :
                       power == 11 ? 0.25f : 0.0f;
        }
        return 1;
    }


}
