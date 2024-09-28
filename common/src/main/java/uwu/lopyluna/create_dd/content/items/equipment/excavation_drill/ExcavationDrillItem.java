package uwu.lopyluna.create_dd.content.items.equipment.excavation_drill;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
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
import uwu.lopyluna.create_dd.content.items.equipment.BackTankPickaxeItem;
import uwu.lopyluna.create_dd.content.items.equipment.InvertFunctionPacket;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;
import uwu.lopyluna.create_dd.infrastructure.utility.BoreMining;
import uwu.lopyluna.create_dd.registry.DesiresItems;
import uwu.lopyluna.create_dd.registry.DesiresPackets;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import static uwu.lopyluna.create_dd.infrastructure.utility.VeinMining.findVein;
import static uwu.lopyluna.create_dd.registry.DesireTiers.Drill;
import static uwu.lopyluna.create_dd.registry.DesiresTags.AllBlockTags.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ExcavationDrillItem extends BackTankPickaxeItem {
    private static final Set<BlockPos> hashedBlocks = new HashSet<>();
    private static boolean veinExcavating = false;
    public ExcavationDrillItem(Properties pProperties) {
        super(Drill, 1, -2.8F, pProperties);
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return pState.is(EXCAVATION_DRILL_VEIN_VALID.tag) ? super.getDestroySpeed(pStack, pState) * 0.5f : super.getDestroySpeed(pStack, pState);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @SuppressWarnings("all")
    public static void destroyVein(Level pLevel, BlockState state, BlockPos pos, Player player) {
        boolean flagFakePlayer = player != null && !(player instanceof FakePlayer);
        boolean playerHeldShift;

        if (flagFakePlayer) {
            DesiresPackets.getChannel().sendToServer(new InvertFunctionPacket());
            boolean inverted = DesiresConfigs.client().invertExcavationDrillFunction.get();
            playerHeldShift = inverted != player.isShiftKeyDown();
        } else {
            playerHeldShift = true;
        }

        if (veinExcavating || !(state.is(EXCAVATION_DRILL_VEIN_VALID.tag)) || !playerHeldShift)
            return;
        Vec3 vec = player.getLookAngle();

        veinExcavating = true;
        findVein(pLevel, pos, EXCAVATION_DRILL_VEIN_VALID.tag, state.is(EXCAVATION_DRILL_VEIN_LARGE.tag) ? 32 : 12).destroyBlocks(pLevel, player, (dropPos, item) -> dropItemFromExcavatedVein(pLevel, pos, vec, dropPos, item));
        veinExcavating = false;
    }

    public static void dropItemFromExcavatedVein(Level world, BlockPos breakingPos, Vec3 fallDirection, BlockPos pos,
                                           ItemStack stack) {
        float distance = (float) Math.sqrt(pos.distSqr(breakingPos));
        Vec3 dropPos = VecHelper.getCenterOf(pos);
        ItemEntity entity = new ItemEntity(world, dropPos.x, dropPos.y, dropPos.z, stack);
        entity.setDeltaMovement(fallDirection.scale(distance / 16f));
        world.addFreshEntity(entity);
    }

    @SuppressWarnings("all")
    @SubscribeEvent
    public static void onBlockDestroyed(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        boolean flagFakePlayer = player != null && !(player instanceof FakePlayer);
        boolean playerHeldShift;

        if (flagFakePlayer) {
            DesiresPackets.getChannel().sendToServer(new InvertFunctionPacket());
            boolean inverted = DesiresConfigs.client().invertExcavationDrillFunction.get();
            playerHeldShift = inverted != player.isShiftKeyDown();
        } else {
            playerHeldShift = true;
        }

        Level level = (Level) event.getLevel();
        ItemStack heldItemMainhand = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
        BlockPos blockPos = event.getPos();

        boolean validOres = event.getLevel().getBlockState(blockPos).is(EXCAVATION_DRILL_VEIN_VALID.tag);

        if (DesiresItems.EXCAVATION_DRILL.isIn(heldItemMainhand) && validOres) {
            destroyVein((Level) event.getLevel(), event.getState(), event.getPos(), event.getPlayer());
        }

        if (DesiresItems.EXCAVATION_DRILL.isIn(heldItemMainhand) && player instanceof ServerPlayer serverPlayer && playerHeldShift && !validOres) {
            BlockPos initalBlockPos = event.getPos();

            if (hashedBlocks.contains(initalBlockPos)) {
                return;
            }

            for (BlockPos pos : Objects.requireNonNull(BoreMining.getBlocksToBeDestroyed(1, initalBlockPos, serverPlayer))) {
                if(pos == initalBlockPos || !(event.getLevel().getBlockState(pos).getDestroySpeed(event.getLevel(), pos) != 0.0F) ||
                        event.getLevel().getBlockState(pos).is(EXCAVATION_DRILL_VEIN_VALID.tag) ||
                        !DesiresItems.EXCAVATION_DRILL.get().isCorrectToolForDrops(heldItemMainhand, event.getLevel().getBlockState(pos))) {
                    continue;
                }

                hashedBlocks.add(pos);
                Vec3 vec = VecHelper.offsetRandomly(VecHelper.getCenterOf(pos), level.random, .125f);
                BlockHelper.destroyBlockAs(level, pos, player, heldItemMainhand, 1f, (stack) -> {
                    if (stack.isEmpty())
                        return;
                    if (!level.getGameRules()
                            .getBoolean(GameRules.RULE_DOBLOCKDROPS))
                        return;
                    if (level.restoringBlockSnapshots)
                        return;

                    ItemEntity itementity = new ItemEntity(level, vec.x, vec.y, vec.z, stack);
                    itementity.setDefaultPickUpDelay();
                    itementity.setDeltaMovement(Vec3.ZERO);
                    level.addFreshEntity(itementity);
                });
                hashedBlocks.remove(pos);
            }
        }
    }

    public void repairTool(ItemStack tool, int value) {
        tool.setDamageValue(tool.getDamageValue() - value);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack tool = pPlayer.getItemInHand(pUsedHand);
        ItemStack repairStack = pPlayer.getItemInHand(InteractionHand.MAIN_HAND == pUsedHand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);

        if (!pPlayer.isShiftKeyDown() && (repairStack.is(DesiresItems.BURY_BLEND.get()) || repairStack.is(AllItems.BRASS_INGOT.get())) && tool.isDamaged()) {
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

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new ExcavationDrillRenderer()));
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
        return Drill.getUses();
    }
}
