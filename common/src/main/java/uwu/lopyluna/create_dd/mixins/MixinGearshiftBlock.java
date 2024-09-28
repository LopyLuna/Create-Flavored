package uwu.lopyluna.create_dd.mixins;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.base.AbstractEncasedShaftBlock;
import com.simibubi.create.content.kinetics.transmission.GearshiftBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uwu.lopyluna.create_dd.access.AccessGearshiftBlock;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings({"deprecation"})
@Mixin(value = GearshiftBlock.class)
public class MixinGearshiftBlock extends AbstractEncasedShaftBlock implements AccessGearshiftBlock {

    @Shadow(remap = false) public void detachKinetics(Level worldIn, BlockPos pos, boolean reAttachNextTick) {}

    @Unique
    private static final BooleanProperty create_DnD$INVERTED = BlockStateProperties.INVERTED;

    private MixinGearshiftBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer,
                                          InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.mayBuild() && pPlayer.isShiftKeyDown()) {
            if (pLevel.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                BlockState blockstate = pState.cycle(create_DnD$INVERTED);
                detachKinetics(pLevel, pPos, true);
                pLevel.setBlock(pPos, blockstate, 4);
                pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, blockstate));
                AllSoundEvents.SCROLL_VALUE.playOnServer(pLevel, pPos, 1, Create.RANDOM.nextFloat() + .8f);
                return InteractionResult.CONSUME;
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public boolean create_DnD$getInverted() {
        return defaultBlockState().getValue(create_DnD$INVERTED);
    }

    @Inject(method = "createBlockStateDefinition(Lnet/minecraft/world/level/block/state/StateDefinition$Builder;)V",at=@At("HEAD"))
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(create_DnD$INVERTED);
    }
}
