package uwu.lopyluna.create_dd.content.items.equipment.deforester_saw;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.utility.AbstractBlockBreakQueue;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.content.items.equipment.BackTankAxeItem;
import uwu.lopyluna.create_dd.content.items.equipment.InvertFunctionPacket;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;
import uwu.lopyluna.create_dd.registry.DesiresItems;
import uwu.lopyluna.create_dd.registry.DesiresPackets;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Optional;
import java.util.function.Consumer;

import static uwu.lopyluna.create_dd.infrastructure.utility.SpecialTreeCutter.findDynamicTree;
import static uwu.lopyluna.create_dd.infrastructure.utility.SpecialTreeCutter.findTree;
import static uwu.lopyluna.create_dd.registry.DesireTiers.Deforester;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DeforesterSawItem extends BackTankAxeItem {
    private static boolean deforesting = false; // required as to not run into "recursions" over forge events on tree cutting
    public DeforesterSawItem(Properties pProperties) {
        super(Deforester, 6.0F, -3.2F, pProperties);
    }

    @SuppressWarnings("all")
public static void destroyTree(Level pLevel, BlockState state, BlockPos pos, Player player) {
    boolean flagFakePlayer = player != null && !(player instanceof FakePlayer);
    boolean playerHeldShift;

    if (flagFakePlayer) {
        DesiresPackets.getChannel().sendToServer(new InvertFunctionPacket());
        boolean inverted = DesiresConfigs.client().invertDeforesterSawFunction.get();
        playerHeldShift = inverted != player.isShiftKeyDown();
    } else {
        playerHeldShift = true;
    }

    if (deforesting || !(state.is(BlockTags.LOGS) || AllTags.AllBlockTags.SLIMY_LOGS.matches(state)) || !playerHeldShift)
        return;
    Vec3 vec = player.getLookAngle();

    deforesting = true;
    Optional<AbstractBlockBreakQueue> dynamicTree = findDynamicTree(state.getBlock(), pos);
    if (dynamicTree.isPresent()) {
        dynamicTree.get().destroyBlocks(pLevel, player, (dropPos, item) -> dropItemFromCutTree(pLevel, pos, vec, dropPos, item));
        deforesting = false;
        return;
    }
    findTree(pLevel, pos).destroyBlocks(pLevel, player, (dropPos, item) -> dropItemFromCutTree(pLevel, pos, vec, dropPos, item));
    deforesting = false;
    }

    @SubscribeEvent
    public static void onBlockDestroyed(BlockEvent.BreakEvent event) {
        ItemStack heldItemMainhand = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);

        if (!DesiresItems.DEFORESTER_SAW.isIn(heldItemMainhand))
            return;
        destroyTree((Level) event.getLevel(), event.getState(), event.getPos(), event.getPlayer());
    }

    public void repairTool(ItemStack tool, int value) {
        tool.setDamageValue(tool.getDamageValue() - value);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack tool = pPlayer.getItemInHand(pUsedHand);
        ItemStack repairStack = pPlayer.getItemInHand(InteractionHand.MAIN_HAND == pUsedHand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);

        if (!pPlayer.isShiftKeyDown() && repairStack.is(AllItems.ANDESITE_ALLOY.get()) && tool.isDamaged()) {
            if (pPlayer.getMainHandItem().getItem() == this && tool.getMaxDamage() != tool.getDamageValue()) {
                repairTool(tool, ((tool.getMaxDamage() - 10) >= tool.getDamageValue()) ? 10 : tool.getMaxDamage() - tool.getDamageValue());

                if (!pPlayer.isCreative()) {
                    repairStack.shrink(1);
                }
            }
            if (pLevel.isClientSide()) {
                float r = (pPlayer.random.nextFloat() - pPlayer.random.nextFloat()) * 0.2F;
                pPlayer.playSound(SoundEvents.IRON_GOLEM_REPAIR, .25f + r, 1.25f + r);
                pPlayer.playSound(SoundEvents.NETHERITE_BLOCK_HIT, .25f, .75f + r);
                pPlayer.playSound(SoundEvents.COPPER_BREAK, .9f, 1.45f + r);
                pPlayer.playSound(SoundEvents.WOOD_BREAK, .9f, 1.05f + r);
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, pPlayer.getItemInHand(pUsedHand));
            } else {
                return new InteractionResultHolder<>(InteractionResult.FAIL, pPlayer.getItemInHand(pUsedHand));
            }
        } else {
            return super.use(pLevel, pPlayer, pUsedHand);
        }
    }

    public static void dropItemFromCutTree(Level world, BlockPos breakingPos, Vec3 fallDirection, BlockPos pos,
                                           ItemStack stack) {
        float distance = (float) Math.sqrt(pos.distSqr(breakingPos));
        Vec3 dropPos = VecHelper.getCenterOf(pos);
        ItemEntity entity = new ItemEntity(world, dropPos.x, dropPos.y, dropPos.z, stack);
        entity.setDeltaMovement(fallDirection.scale(distance / 16f));
        world.addFreshEntity(entity);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new DeforesterSawRenderer()));
    }


    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return BacktankUtil.isBarVisible(stack, maxUses());
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return BacktankUtil.getBarWidth(stack, maxUses());
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return BacktankUtil.getBarColor(stack, maxUses());
    }


    public static int maxUses() {
        return Deforester.getUses();
    }

}
