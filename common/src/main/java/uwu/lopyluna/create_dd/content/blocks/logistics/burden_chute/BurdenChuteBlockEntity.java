package uwu.lopyluna.create_dd.content.blocks.logistics.burden_chute;

import com.simibubi.create.content.logistics.chute.ChuteBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;

public class BurdenChuteBlockEntity extends ChuteBlockEntity {

    public BurdenChuteBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected int getExtractionAmount() {
        return DesiresConfigs.server().logistics.burdenChuteStackSize.get();
    }

    @Override
    public float getItemMotion() {
        return super.getItemMotion() * DesiresConfigs.server().logistics.burdenChuteSpeedMultiplier.getF();
    }
}
