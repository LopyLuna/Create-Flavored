package uwu.lopyluna.create_dd.content.blocks.kinetics.brass_gearbox;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BrassGearboxBlockEntity extends KineticBlockEntity {
    public BrassGearboxBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected boolean isNoisy() {
        return false;
    }
}
