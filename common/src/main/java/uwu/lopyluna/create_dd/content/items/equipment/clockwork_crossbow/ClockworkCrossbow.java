package uwu.lopyluna.create_dd.content.items.equipment.clockwork_crossbow;

import com.simibubi.create.AllSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;
import uwu.lopyluna.create_dd.registry.DesiresItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
@SuppressWarnings({"unused"})
public class ClockworkCrossbow extends ProjectileWeaponItem implements Vanishable {

    private static boolean isPlayerCreative = false;

    private static final String currentTicksInShots = "currentTicksInShots";
    private static final String currentTicksInReloadShots = "currentTicksInReloadShots";
    private static final String currentTotalShots = "currentTotalShots";
    private static final String affirmativeShots = "affirmativeShots";
    private static final String readyShots = "readyShots";
    private static final String currentCooldown = "currentCooldown";
    private static final String affirmativeCooldown = "affirmativeCooldown";
    private static final String readyCooldown = "readyCooldown";
    private static final String isCurrentlyShooting = "isCurrentlyShooting";
    private static final String isDoneShooting = "isDoneShooting";

    public ClockworkCrossbow(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.VANISHING_CURSE)
            return true;
        if (enchantment == Enchantments.QUICK_CHARGE)
            return true;
        if (enchantment == Enchantments.MULTISHOT)
            return true;
        if (enchantment == Enchantments.POWER_ARROWS)
            return true;
        if (enchantment == Enchantments.INFINITY_ARROWS)
            return true;
        if (enchantment == Enchantments.UNBREAKING)
            return false;
        if (enchantment == Enchantments.MENDING)
            return false;
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public void onUsingTick(ItemStack pStack, LivingEntity pLivingEntity, int count) {
        Player pPlayer = (Player)pLivingEntity;
        Level pLevel = pPlayer.getLevel();


        super.onUsingTick(pStack, pLivingEntity, count);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        Player pPlayer = (Player)pEntity;
        isPlayerCreative = pPlayer.isCreative();
        if (!pPlayer.isSpectator()) {
            ItemStack pProjectile = getProjectile(pPlayer, pStack);
            boolean crossbowMainHand = DesiresItems.CLOCKWORK_CROSSBOW.isIn(pPlayer.getMainHandItem());
            boolean crossbowOffHand = DesiresItems.CLOCKWORK_CROSSBOW.isIn(pPlayer.getOffhandItem());
            boolean isOnlyOneCrossbowSelected = (crossbowOffHand || crossbowMainHand) && !(crossbowOffHand && crossbowMainHand);
            boolean isPlayerShifting = pPlayer.isCrouching() || (pPlayer.isCreative() && pPlayer.isShiftKeyDown() && !pPlayer.isOnGround());
            boolean multiShot = !(pStack.getEnchantmentLevel(Enchantments.MULTISHOT) == 0);
            int multiShotLevel = pStack.getEnchantmentLevel(Enchantments.MULTISHOT);
            int quickChargeLevel = pStack.getEnchantmentLevel(Enchantments.QUICK_CHARGE);
            int maxTotalShots = maxTotalShots(multiShot, multiShotLevel);
            int maxCooldown = maxCooldown(quickChargeLevel);
            int maxTicksInShots = maxTicksInShots(quickChargeLevel);
            int maxTicksInReloadShots = maxTicksInReloadShots(quickChargeLevel);

            //SAFETY SHIT :3
            if (getCurrentTicksInShots(pStack) > maxTicksInShots) setCurrentTicksInShots(pStack, maxTicksInShots);
            if (getCurrentTicksInReloadShots(pStack) > maxTicksInReloadShots)
                setCurrentTicksInReloadShots(pStack, maxTicksInReloadShots);
            if (getCurrentTotalShots(pStack) > maxTotalShots) setCurrentTotalShots(pStack, maxTotalShots);
            if (getCurrentCooldown(pStack) < 0) setCurrentCooldown(pStack, 0);
            if (getCurrentTicksInShots(pStack) < 0) setCurrentTicksInShots(pStack, 0);
            if (getCurrentTicksInReloadShots(pStack) < 0) setCurrentTicksInReloadShots(pStack, 0);
            if (getCurrentTotalShots(pStack) < 0) setCurrentTotalShots(pStack, 0);

            SoundSource soundsource = pEntity instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            if (isAffirmativeCooldown(pStack)) {
                AllSoundEvents.CONFIRM.playOnServer(pPlayer.level, pPlayer.blockPosition(), 0.25f, .5f);
                pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.AXE_SCRAPE,
                        soundsource, 0.6F, 0.6F);
                pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.CANDLE_EXTINGUISH,
                        soundsource, 0.8F, 0.8F);
                pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.PISTON_CONTRACT,
                        soundsource, 0.1F, 0.75F);
                setAffirmativeCooldown(pStack, false);
            }
            if (isAffirmativeShots(pStack)) {
                AllSoundEvents.CONFIRM.playOnServer(pPlayer.level, pPlayer.blockPosition(), 0.25f, 1);
                pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.CROSSBOW_LOADING_END,
                        soundsource, 0.8F, 0.75F / (pLevel.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
                pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.ARMOR_EQUIP_CHAIN,
                        soundsource, 0.4F, 0.75F / (pLevel.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
                setAffirmativeShots(pStack, false);
            }
            if (isDoneShooting(pStack)) {
                pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.COPPER_BREAK,
                        soundsource, 1.0F, 0.75F / (pLevel.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
                pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.ITEM_BREAK,
                        soundsource, 0.1F, 0.75F / (pLevel.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
                pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.TRIDENT_HIT_GROUND,
                        soundsource, 0.6F, 0.5F);
                setDoneShooting(pStack, false);
            }
            if (isOnlyOneCrossbowSelected) {
                if (getCurrentTicksInShots(pStack) > 0) {
                    setCurrentTicksInShots(pStack, getCurrentTicksInShots(pStack) - 1);
                }
                if (!isCurrentlyShooting(pStack)) {
                    if (getCurrentTicksInReloadShots(pStack) > 0) {
                        setCurrentTicksInReloadShots(pStack, getCurrentTicksInReloadShots(pStack) - 1);
                    }
                    if (getCurrentCooldown(pStack) > 0) {
                        setCurrentCooldown(pStack, getCurrentCooldown(pStack) - 1);
                    }
                    if (getCurrentCooldown(pStack) == 0 && !isAffirmativeCooldown(pStack) && !isReadyCooldown(pStack)) {
                        setAffirmativeCooldown(pStack, true);
                        setReadyCooldown(pStack, true);
                    }
                }
            }

            if (isCurrentlyShooting(pStack)) {
                if (getCurrentTicksInShots(pStack) == 0 && getCurrentTotalShots(pStack) > 0) {
                    setCurrentTicksInShots(pStack, maxTicksInShots);
                    setCurrentTotalShots(pStack, getCurrentTotalShots(pStack) - 1);

                    pStack.hurtAndBreak(1, pPlayer, (p_40665_) -> p_40665_.broadcastBreakEvent(pPlayer.getUsedItemHand()));
                    pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.CROSSBOW_SHOOT,
                            soundsource, 0.5F, 2.0F / (pLevel.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
                    pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.COPPER_HIT,
                            soundsource, 1.0F, 0.75F / (pLevel.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);

                    if (pProjectile.isEmpty())
                        return;

                    int powerLevel = pStack.getEnchantmentLevel(Enchantments.POWER_ARROWS);
                    boolean flag1 = pPlayer.getAbilities().instabuild || (pProjectile.getItem() instanceof ArrowItem && ((ArrowItem) pProjectile.getItem()).isInfinite(pProjectile, pStack, pPlayer));
                    ArrowItem arrowitem = (ArrowItem) (pProjectile.getItem() instanceof ArrowItem ? pProjectile.getItem() : Items.ARROW);
                    AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, pProjectile, pPlayer);
                    abstractarrow = customArrow(abstractarrow);
                    abstractarrow.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, (1.0F + (getCurrentTotalShots(pStack) / 2.0F)) * ((powerLevel * 0.35F) + 0.5F), 0.5F + ((getCurrentTotalShots(pStack) / 10.0F) / (((powerLevel * 0.55F) + 1.0F))));
                    pLevel.addFreshEntity(abstractarrow);
                    pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.CROSSBOW_SHOOT,
                            soundsource, 0.5F, 1.0F + (((float) getCurrentTotalShots(pStack) / maxTotalShots) / 2.0F));
                    pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.CANDLE_EXTINGUISH,
                            soundsource, 0.75F, 0.5F + ((float) getCurrentTotalShots(pStack) / maxTotalShots));
                    pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.TRIDENT_THROW,
                            soundsource, 0.15F, 0.5F + (((float) getCurrentTotalShots(pStack) / maxTotalShots) / 2F));
                    if (flag1 || pPlayer.getAbilities().instabuild && (pProjectile.is(Items.SPECTRAL_ARROW) || pProjectile.is(Items.TIPPED_ARROW))) {
                        abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }
                    if (!flag1 && !pPlayer.isCreative()) {
                        pProjectile.shrink(1);
                        if (pProjectile.isEmpty()) {
                            pPlayer.getInventory().removeItem(pProjectile);
                        }
                    }
                } else if (getCurrentTotalShots(pStack) == 0) {
                    setDoneShooting(pStack, true);
                    setCurrentlyShooting(pStack, false);
                    pPlayer.awardStat(Stats.ITEM_USED.get(this));
                }
            }

            if (pIsSelected && isOnlyOneCrossbowSelected && !isCurrentlyShooting(pStack)) {
                if (isPlayerShifting && isReadyCooldown(pStack) && !isReadyShots(pStack)) {
                    if (getCurrentTicksInReloadShots(pStack) == 0 && getCurrentTotalShots(pStack) < maxTotalShots) {
                        setCurrentTicksInReloadShots(pStack, maxTicksInReloadShots);
                        setCurrentTotalShots(pStack, getCurrentTotalShots(pStack) + 1);

                        pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.COPPER_HIT,
                                soundsource, 1.0F, 1.0F + ((float) getCurrentTotalShots(pStack) / maxTotalShots));
                        pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.PISTON_EXTEND,
                                soundsource, 0.25F, 1.5F + ((float) getCurrentTotalShots(pStack) / maxTotalShots));
                        pLevel.playSound(null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.CROSSBOW_QUICK_CHARGE_3,
                                soundsource, 0.5F, 0.9F + ((float) getCurrentTotalShots(pStack) / maxTotalShots));
                    } else if (getCurrentTotalShots(pStack) == maxTotalShots && !isAffirmativeShots(pStack)) {
                        setAffirmativeShots(pStack, true);
                        setReadyShots(pStack, true);
                    }
                }

                if (pPlayer.isUsingItem() && !isPlayerShifting && isReadyShots(pStack) && isReadyCooldown(pStack)) {
                    setCurrentlyShooting(pStack, true);
                    setCurrentCooldown(pStack, maxCooldown);
                    setReadyCooldown(pStack, false);
                    setReadyShots(pStack, false);
                }
            }
            super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        boolean multiShot = !(pStack.getEnchantmentLevel(Enchantments.MULTISHOT) == 0);
        int multiShotLevel = pStack.getEnchantmentLevel(Enchantments.MULTISHOT);
        int quickChargeLevel = pStack.getEnchantmentLevel(Enchantments.QUICK_CHARGE);
        int powerLevel = pStack.getEnchantmentLevel(Enchantments.POWER_ARROWS);
        int maxTotalShots = maxTotalShots(multiShot, multiShotLevel);
        int maxCooldown = maxCooldown(quickChargeLevel);
        int maxTicksInShots = maxTicksInShots(quickChargeLevel);
        int maxTicksInReloadShots = maxTicksInReloadShots(quickChargeLevel);

        if (pLevel == null) {
            return;
        }
        if (pLevel.isClientSide() && (isPlayerCreative && DesiresConfigs.client().equipmentsDebug.get())) {
            pTooltipComponents.add(Component.literal("inaccuracy " + (0.5F + ((getCurrentTotalShots(pStack) / 10.0F) / (((powerLevel * 0.55F) + 1.0F))))).withStyle(ChatFormatting.GOLD));
            pTooltipComponents.add(Component.literal("velocity " + (1.0F + ((1.0F + (getCurrentTotalShots(pStack) / 2.0F)) * ((powerLevel * 0.35F) + 0.5F)))).withStyle(ChatFormatting.GOLD));

            pTooltipComponents.add(Component.literal("maxTicksInShots " + maxTicksInShots).withStyle(ChatFormatting.GOLD));
            pTooltipComponents.add(Component.literal("currentTicksInShots " + getCurrentTicksInShots(pStack)).withStyle(ChatFormatting.YELLOW));
            pTooltipComponents.add(Component.literal("maxTicksInReloadShots " + maxTicksInReloadShots).withStyle(ChatFormatting.GOLD));
            pTooltipComponents.add(Component.literal("currentTicksInReloadShots " + getCurrentTicksInReloadShots(pStack)).withStyle(ChatFormatting.YELLOW));
            pTooltipComponents.add(Component.literal("maxTotalShots " + maxTotalShots).withStyle(ChatFormatting.GOLD));
            pTooltipComponents.add(Component.literal("currentTotalShots " + getCurrentTotalShots(pStack)).withStyle(ChatFormatting.YELLOW));
            pTooltipComponents.add(Component.literal("affirmativeShots " + isAffirmativeShots(pStack)).withStyle(ChatFormatting.DARK_AQUA));
            pTooltipComponents.add(Component.literal("readyShots " + isReadyShots(pStack)).withStyle(ChatFormatting.DARK_PURPLE));
            pTooltipComponents.add(Component.literal("maxCooldown " + maxCooldown).withStyle(ChatFormatting.GOLD));
            pTooltipComponents.add(Component.literal("currentCooldown " + getCurrentCooldown(pStack)).withStyle(ChatFormatting.YELLOW));
            pTooltipComponents.add(Component.literal("affirmativeCooldown " + isAffirmativeCooldown(pStack)).withStyle(ChatFormatting.DARK_AQUA));
            pTooltipComponents.add(Component.literal("readyCooldown " + isReadyCooldown(pStack)).withStyle(ChatFormatting.DARK_PURPLE));
            pTooltipComponents.add(Component.literal("isCurrentlyShooting " + isCurrentlyShooting(pStack)).withStyle(ChatFormatting.DARK_PURPLE));
            pTooltipComponents.add(Component.literal("isDoneShooting " + isDoneShooting(pStack)).withStyle(ChatFormatting.DARK_AQUA));
        }
    }

    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        boolean flag = !pPlayer.getProjectile(itemstack).isEmpty();

        InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onArrowNock(itemstack, pLevel, pPlayer, pHand, flag);
        if (ret != null) return ret;

        if (!pPlayer.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public ItemStack getProjectile(Player player, ItemStack pStack) {
        return player.getProjectile(pStack);
    }

    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.CROSSBOW;
    }

    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    public AbstractArrow customArrow(AbstractArrow arrow) {
        return arrow;
    }

    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        boolean base = super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
        //should fix the weird re-equipping issue when tags are consistently changing
        return base || ( getCurrentTicksInShots(newStack) > 0  || getCurrentTicksInShots(oldStack) > 0
                || getCurrentTicksInReloadShots(newStack) > 0 || getCurrentTicksInReloadShots(oldStack) > 0
                || getCurrentCooldown(newStack) < 0 || getCurrentCooldown(oldStack) < 0
        );
    }



    //ADVANCE SHIT

    //TICKS BETWEEN SHOTS WHEN SHOOTING
    private static int maxTicksInShots(int quickChargeLevel) {return 8 - (quickChargeLevel * 2);}
    public static int getCurrentTicksInShots(ItemStack pStack) {
        return pStack.getOrCreateTag().getInt("currentTicksInShots");
    }
    public static void setCurrentTicksInShots(ItemStack pStack, int pValue) {
        pStack.getOrCreateTag().putInt("currentTicksInShots", pValue);
    }

    //TICKS BETWEEN RELOADING SHOTS WHEN RELOADING
    private static int maxTicksInReloadShots(int quickChargeLevel) {return 18 - (quickChargeLevel * 4);}
    public static int getCurrentTicksInReloadShots(ItemStack pStack) {
        return pStack.getOrCreateTag().getInt("currentTicksInReloadShots");
    }
    public static void setCurrentTicksInReloadShots(ItemStack pStack, int pValue) {
        pStack.getOrCreateTag().putInt("currentTicksInReloadShots", pValue);
    }

    //AMOUNT OF ARROWS GETTING SHOT
    private static int maxTotalShots(boolean multiShot, int multiShotLevel) {return multiShot ? multiShotLevel >= 2 ? (multiShotLevel * 3) + 3 : 6 : 3;}
    public static int getCurrentTotalShots(ItemStack pStack) {
        return pStack.getOrCreateTag().getInt("currentTotalShots");
    }
    public static void setCurrentTotalShots(ItemStack pStack, int pValue) {
        pStack.getOrCreateTag().putInt("currentTotalShots", pValue);
    }
    public static boolean isAffirmativeShots(ItemStack pStack) {
        return pStack.getOrCreateTag().getBoolean("affirmativeShots");
    }
    public static void setAffirmativeShots(ItemStack pStack, boolean pValue) {
        pStack.getOrCreateTag().putBoolean("affirmativeShots", pValue);
    }
    public static boolean isReadyShots(ItemStack pStack) {
        return pStack.getOrCreateTag().getBoolean("readyShots");
    }
    public static void setReadyShots(ItemStack pStack, boolean pValue) {
        pStack.getOrCreateTag().putBoolean("readyShots", pValue);
    }

    //COOLDOWN AFTER SHOT
    private static int maxCooldown(int quickChargeLevel) {
        return 80 - (20 * quickChargeLevel);
    }
    public static int getCurrentCooldown(ItemStack pStack) {
        return pStack.getOrCreateTag().getInt("currentCooldown");
    }
    public static void setCurrentCooldown(ItemStack pStack, int pValue) {
        pStack.getOrCreateTag().putInt("currentCooldown", pValue);
    }
    public static boolean isAffirmativeCooldown(ItemStack pStack) {
        return pStack.getOrCreateTag().getBoolean("affirmativeCooldown");
    }
    public static void setAffirmativeCooldown(ItemStack pStack, boolean pValue) {
        pStack.getOrCreateTag().putBoolean("affirmativeCooldown", pValue);
    }
    public static boolean isReadyCooldown(ItemStack pStack) {
        return pStack.getOrCreateTag().getBoolean("readyCooldown");
    }
    public static void setReadyCooldown(ItemStack pStack, boolean pValue) {
        pStack.getOrCreateTag().putBoolean("readyCooldown", pValue);
    }

    //IF PLAYER IS CURRENTLY SHOOTING
    public static boolean isCurrentlyShooting(ItemStack pStack) {
        return pStack.getOrCreateTag().getBoolean("isCurrentlyShooting");
    }
    public static void setCurrentlyShooting(ItemStack pStack, boolean pValue) {
        pStack.getOrCreateTag().putBoolean("isCurrentlyShooting", pValue);
    }
    public static boolean isDoneShooting(ItemStack pStack) {
        return pStack.getOrCreateTag().getBoolean("isDoneShooting");
    }
    public static void setDoneShooting(ItemStack pStack, boolean pValue) {
        pStack.getOrCreateTag().putBoolean("isDoneShooting", pValue);
    }
}
