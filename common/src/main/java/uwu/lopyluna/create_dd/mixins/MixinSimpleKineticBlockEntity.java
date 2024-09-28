package uwu.lopyluna.create_dd.mixins;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uwu.lopyluna.create_dd.content.blocks.kinetics.giant_gear.GiantGearBlockEntity;

import java.util.List;

@Mixin(value = SimpleKineticBlockEntity.class, remap = false)
public class MixinSimpleKineticBlockEntity extends KineticBlockEntity {
    public MixinSimpleKineticBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }
    
    @Inject(method = "addPropagationLocations", at = @At("RETURN"))
    private void addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours, CallbackInfoReturnable<List<BlockPos>> cir) {
        Direction.Axis axis = getBlockState().getValue(RotatedPillarKineticBlock.AXIS);
        
        if (ICogWheel.isLargeCog(state))
            neighbours.addAll(
                GiantGearBlockEntity.collectConnectionPositions(
                    getBlockPos(), axis,
                    false, true
                )
            );
        
        if (ICogWheel.isSmallCog(state))
            neighbours.addAll(
                GiantGearBlockEntity.collectConnectionPositions(
                    getBlockPos(), axis,
                    true, false
                )
            );
    }
}
