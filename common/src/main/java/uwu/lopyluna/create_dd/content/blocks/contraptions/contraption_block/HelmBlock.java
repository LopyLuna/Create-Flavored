package uwu.lopyluna.create_dd.content.blocks.contraptions.contraption_block;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import uwu.lopyluna.create_dd.registry.DesiresBlockEntityTypes;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"deprecation"})
public class HelmBlock extends ControlsBlock implements IBE<HelmBlockEntity> {

    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");
    public static final BooleanProperty ASSEMBLING = BooleanProperty.create("assembling");
    public static final BooleanProperty DISASSEMBLING = BooleanProperty.create("disassembling");

    public HelmBlock(Properties p_54120_) {
        super(p_54120_);
        registerDefaultState(defaultBlockState().setValue(ASSEMBLED, false).setValue(ASSEMBLING, false).setValue(DISASSEMBLING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(ASSEMBLED, ASSEMBLING, DISASSEMBLING));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
                                 BlockHitResult pHit) {
        ItemStack itemInHand = pPlayer.getItemInHand(pHand);
        if (pPlayer.isShiftKeyDown())
            return InteractionResult.PASS;
        if (AllItems.WRENCH.isIn(itemInHand))
            return InteractionResult.PASS;

        if (pState.getValue(ASSEMBLED)) {
            return onBlockEntityUse(pLevel, pPos, cte -> {
                if (!pLevel.isClientSide()) {
                    cte.notifyUpdate();
                    AllSoundEvents.CONTROLLER_CLICK.play(cte.getLevel(), null, cte.getBlockPos(), 1,0.8f);
                    cte.disassembleNextTick = true;
                    pState.setValue(HelmBlock.DISASSEMBLING, true);
                    cte.sendData();
                }
                return InteractionResult.SUCCESS;
            });
        } else {
            return onBlockEntityUse(pLevel, pPos, cte -> {
                if (!pLevel.isClientSide()) {
                    cte.notifyUpdate();
                    AllSoundEvents.CONTROLLER_CLICK.play(cte.getLevel(), null, cte.getBlockPos(), 1,1.5f);
                    cte.assembleNextTick = true;
                    pState.setValue(HelmBlock.ASSEMBLING, true);
                    cte.sendData();
                }
                return InteractionResult.SUCCESS;
            });
        }
    }


    @Override
    public Class<HelmBlockEntity> getBlockEntityClass() {
        return HelmBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends HelmBlockEntity> getBlockEntityType() {
        return DesiresBlockEntityTypes.HELM.get();
    }
}
