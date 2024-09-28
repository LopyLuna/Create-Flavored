package uwu.lopyluna.create_dd.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.kinetics.transmission.GearshiftBlockEntity;
import com.simibubi.create.content.kinetics.transmission.SplitShaftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GearshiftBlockEntity.class)
public abstract class MixinGearshiftBlockEntity extends SplitShaftBlockEntity {
    private MixinGearshiftBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @WrapOperation(method = "getRotationSpeedModifier", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"))
    private <T extends Comparable<T>> Comparable<?> create_dd$addCheckForInvertedProperty(BlockState instance, Property<T> property, Operation<T> original) {
        T powered = original.call(instance, property);
        boolean inverted = getBlockState().getValue(BlockStateProperties.INVERTED);
        return ((Boolean) powered && inverted) || !(Boolean) powered && !inverted;
    }
}
