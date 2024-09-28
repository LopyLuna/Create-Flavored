package uwu.lopyluna.create_dd.mixins;

import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.TickPriority;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uwu.lopyluna.create_dd.access.AccessFluidPipeBlock;

@Mixin(value = FluidPipeBlock.class, remap = false)
public class MixinFluidPipeBlock extends PipeBlock implements AccessFluidPipeBlock {
    @Unique
    private static final BooleanProperty create_dd$$UNLOCKED = BooleanProperty.create("unlocked");

    public MixinFluidPipeBlock(float pApothem, Properties pProperties) {
        super(pApothem, pProperties);
    }

    @Unique
    public boolean create_dd$getUNLOCKED() {
        return defaultBlockState().getValue(create_dd$$UNLOCKED);
    }

    @Inject(at = @At("HEAD"), method = "use(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;", cancellable = true)
    public void create_dd$use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pRaytrace, CallbackInfoReturnable<InteractionResult> cir) {
        Item handItem = pPlayer.getItemInHand(pHand).getItem();
        boolean isHoneyComb = handItem instanceof HoneycombItem;
        boolean isAxe = handItem instanceof AxeItem;
        boolean isWaxed = !pState.getValue(create_dd$$UNLOCKED);
        if (isWaxed && isAxe) {
            if (pLevel.isClientSide) {
                pLevel.playSound(pPlayer, pPos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                pLevel.levelEvent(pPlayer, 3004, pPos, 0);

                cir.setReturnValue(InteractionResult.SUCCESS);
            } else {
                BlockState blockstate = pState.setValue(create_dd$$UNLOCKED, true);
                pLevel.setBlock(pPos, blockstate, Block.UPDATE_ALL);
                pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, blockstate));
                pLevel.scheduleTick(pPos, this, 1, TickPriority.HIGH);

                if (pPlayer instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pPos, pPlayer.getItemInHand(pHand));
                }
                cir.setReturnValue(InteractionResult.CONSUME);
            }
        }
        if (!isWaxed && isHoneyComb) {
            if (pLevel.isClientSide) {
                pLevel.levelEvent(pPlayer, 3003, pPos, 0);
                cir.setReturnValue(InteractionResult.SUCCESS);
            } else {
                BlockState blockstate = pState.setValue(create_dd$$UNLOCKED, false);
                pLevel.setBlock(pPos, blockstate, Block.UPDATE_ALL);
                pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, blockstate));
                pLevel.scheduleTick(pPos, this, 1, TickPriority.HIGH);

                cir.setReturnValue(InteractionResult.CONSUME);
            }
        }

    }

    @Inject(method = "createBlockStateDefinition(Lnet/minecraft/world/level/block/state/StateDefinition$Builder;)V",at=@At("HEAD"))
    protected void create_dd$createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(create_dd$$UNLOCKED);
    }
}
