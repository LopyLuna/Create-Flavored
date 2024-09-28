package uwu.lopyluna.create_dd.content.blocks.kinetics.modular_drill;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ModularDrillInstance extends SingleRotatingInstance<ModularDrillBlockEntity> {

    public ModularDrillInstance(MaterialManager materialManager, ModularDrillBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        BlockState referenceState = blockEntity.getBlockState();
        Direction facing = referenceState.getValue(BlockStateProperties.FACING);

        ModularDrillHeadItem.DrillType drillType = blockEntity.getCurrentDrillHeadType();
        return switch (drillType) {
            case VEIN -> getRotatingMaterial().getModel(AllPartialModels.DRILL_HEAD, referenceState, facing);//vein
            case ENCHANTABLE -> getRotatingMaterial().getModel(AllPartialModels.DRILL_HEAD, referenceState, facing);//enchantable
            case CRUSHER -> getRotatingMaterial().getModel(AllPartialModels.DRILL_HEAD, referenceState, facing);//crusher
            case SMELTING -> getRotatingMaterial().getModel(AllPartialModels.DRILL_HEAD, referenceState, facing);//smelting
        };
    }
}
