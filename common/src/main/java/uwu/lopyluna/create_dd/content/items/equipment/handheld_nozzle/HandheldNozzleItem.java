package uwu.lopyluna.create_dd.content.items.equipment.handheld_nozzle;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static uwu.lopyluna.create_dd.DesireUtil.randomChance;
import static uwu.lopyluna.create_dd.registry.DesireTiers.Drill;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("all")
public class HandheldNozzleItem extends Item {

    private final List<Entity> pushingEntities = new ArrayList<>();

    public HandheldNozzleItem(Properties properties) {
        super(properties.durability(4096));
    }

    public boolean getIsBlowing(CompoundTag nbt) {
        return nbt.getBoolean("isBlowing");
    }
    public boolean getIsActive(CompoundTag nbt) {
        return nbt.getBoolean("isActive");
    }
    public void setIsBlowing(CompoundTag nbt, boolean value) {
        nbt.putBoolean("isBlowing", value);
    }
    public void setIsActive(CompoundTag nbt, boolean value) {
        nbt.putBoolean("isActive", value);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack tool = pPlayer.getItemInHand(InteractionHand.MAIN_HAND == pUsedHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
        ItemStack repairStack = pPlayer.getItemInHand(InteractionHand.MAIN_HAND == pUsedHand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);

        if ((repairStack.is(AllItems.PROPELLER.get()) || repairStack.is(Items.COPPER_INGOT))) {
            boolean flag = (tool.isDamaged() && tool.isBarVisible());
            int value = repairStack.is(AllItems.PROPELLER.get()) ? 100 : 10;
            if (tool.getItem() == this && tool.getMaxDamage() != tool.getDamageValue() && flag) {
                repairTool(tool, ((tool.getMaxDamage() - value) >= tool.getDamageValue()) ? value : tool.getMaxDamage() - tool.getDamageValue());

                if (!pPlayer.isCreative()) {
                    repairStack.shrink(1);
                }
            }
            if (pLevel.isClientSide() && flag) {
                float r = (pPlayer.random.nextFloat() - pPlayer.random.nextFloat()) * 0.2F;
                pPlayer.playSound(SoundEvents.AXE_SCRAPE, .25f, 0.85f + r);
                pPlayer.playSound(SoundEvents.NETHERITE_BLOCK_HIT, .15f, .75f + r);
                pPlayer.playSound(SoundEvents.COPPER_BREAK, .9f, 1.45f + r);
                pPlayer.playSound(SoundEvents.COPPER_BREAK, .9f, 0.65f + r);
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, pPlayer.getItemInHand(pUsedHand));
            } else {
                return new InteractionResultHolder<>(InteractionResult.FAIL, pPlayer.getItemInHand(pUsedHand));
            }
        } else {
            ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
            CompoundTag nbt = itemStack.getOrCreateTag();

            if (pPlayer.isShiftKeyDown()) {
                if (!pLevel.isClientSide) {
                    setIsBlowing(nbt, !getIsBlowing(nbt));
                    pPlayer.displayClientMessage(Component.literal("Mode switched to: " + (getIsBlowing(nbt) ? "Blowing" : "Vacuuming")), true);
                    pLevel.playSound(null, pPlayer.blockPosition(), AllSoundEvents.CONFIRM.getMainEvent(), SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            } else {
                if (!pLevel.isClientSide) {
                    setIsActive(nbt, !getIsActive(nbt));
                    pPlayer.displayClientMessage(Component.literal(getName(itemStack).getString() + ": " + (getIsActive(nbt) ? "Activated" : "De-Activated")), true);
                    pLevel.playSound(null, pPlayer.blockPosition(), getIsActive(nbt) ? AllSoundEvents.CONFIRM.getMainEvent() : AllSoundEvents.DENY.getMainEvent(), SoundSource.PLAYERS, 1.0F, 1.0F);

                }
            }
            return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
        }
    }

    public void repairTool(ItemStack tool, int value) {
        tool.setDamageValue(tool.getDamageValue() - value);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new HandheldNozzleRenderer()));
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

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        float range = DesiresConfigs.server().equipment.handheldNozzleRange.getF();
        float rangeP = DesiresConfigs.server().equipment.handheldNozzleRangePlayer.getF();
        float speedLimit = DesiresConfigs.server().equipment.handheldNozzleSpeedLimit.getF();
        CompoundTag nbt = pStack.getOrCreateTag();

        if (pEntity instanceof Player pPlayer) {
            if (pIsSelected || (pPlayer.getOffhandItem() == pStack && pSlotId == 0 && !(pIsSelected && pSlotId == 0 && pPlayer.getMainHandItem() == pStack))) {
                if (!pPlayer.getTags().contains("self")) {
                    pPlayer.addTag("self");
                }

                if (!(getIsActive(nbt) && !pPlayer.isShiftKeyDown())) return;

                Vec3 center = getCenterPos(pPlayer);

                if (!pLevel.isClientSide) {

                    if (!BacktankUtil.canAbsorbDamage(pPlayer, getMaxDamage(pStack))) {
                        pStack.hurtAndBreak(randomChance(75) ? 1 : 0, pPlayer, p -> p.broadcastBreakEvent((pPlayer.getOffhandItem() == pStack && pSlotId == 0 && !(pIsSelected && pSlotId == 0 && pPlayer.getMainHandItem() == pStack)) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND));
                    }

                    handleEntityInteractions(pLevel, pPlayer, center, range, nbt, speedLimit);
                    handlePlayerInteractions(pPlayer, rangeP, nbt, speedLimit);
                }
                if (pLevel.isClientSide) {
                    generateParticles(pLevel, pPlayer, range, rangeP, nbt);
                }

            } else {
                if (pPlayer.getTags().contains("self")) {
                    pushingEntities.clear();
                    pPlayer.removeTag("self");
                }
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    private void handleEntityInteractions(Level pLevel, Player pPlayer, Vec3 center, float range, CompoundTag nbt, float speedLimit) {
        pushingEntities.removeIf(entity -> entity == null);
        pushingEntities.removeIf(entity -> !entity.isAlive());
        if (range != 0) {
            AABB bb = new AABB(center, center).inflate(range);
            List<Entity> entitiesToRemove = new ArrayList<>();

            for (Entity entity : pLevel.getEntitiesOfClass(Entity.class, bb)) {
                if (entity == null && !entity.isAlive())
                    continue;

                Vec3 diff = getCenterPos(entity).subtract(center);
                double distance = diff.length();

                if (entity.noPhysics || entity.isInvulnerable() || entity.isSpectator() || entity.isMultipartEntity() || isGETEntitySize(entity, 1.95F, 2.2F) || isGETEntityVolume(entity, 1.95F, 2.2F) ||
                        distance > range || (!getIsBlowing(nbt) && distance <= 1.5) || entity.isShiftKeyDown() || AirCurrent.isPlayerCreativeFlying(entity) || entity.equals(pPlayer)) {
                    entitiesToRemove.add(entity);
                    continue;
                }

                if (!pushingEntities.contains(entity)) {
                    pushingEntities.add(entity);
                }

                Vec3 pushVec = diff.normalize().scale((range - distance) * (getIsBlowing(nbt) ? 1 : -1));
                float forceFactor = (entity instanceof ItemEntity) ? 1 / 128f : 1 / 32f;
                Vec3 force = pushVec.scale(forceFactor * 0.5);

                entity.push(force.x, force.y, force.z);
                entity.fallDistance = 0;
                entity.hurtMarked = true;

                Vec3 currentMovement = entity.getDeltaMovement();
                if (currentMovement.length() > speedLimit) {
                    Vec3 limitedMovement = currentMovement.normalize().scale(speedLimit);
                    entity.setDeltaMovement(limitedMovement);
                }
            }

            pushingEntities.removeAll(entitiesToRemove);
        } else {
            pushingEntities.clear();
        }
    }

    public boolean isGETEntitySize(Entity entity, float width, float height) {
        return entity.getBbWidth() >= width || entity.getBbHeight() >= height;
    }
    public boolean isGETEntityVolume(Entity entity, float width, float height) {
        return entity.getBbWidth() + entity.getBbHeight() >= width + height;
    }

    private void handlePlayerInteractions(Player pPlayer, float rangeP, CompoundTag nbt, float speedLimit) {
        Vec3 cursorPos = getBlockSideLookingAt(pPlayer, rangeP);
        if (rangeP != 0 && cursorPos != null && pPlayer != null && pPlayer.isAlive()) {
            Vec3 diffP = getCenterPos(pPlayer).subtract(cursorPos);
            double distanceP = diffP.length();
            Vec3 lookVec = pPlayer.getLookAngle();
            Vec3 pushForce = lookVec.scale(-0.025);

            if (!(distanceP > rangeP || pPlayer.isShiftKeyDown() || AirCurrent.isPlayerCreativeFlying(pPlayer))) {
                if (!(!getIsBlowing(nbt) && distanceP < 1.5f)) {
                    Vec3 pushVecP = diffP.normalize().scale((rangeP - distanceP) * (getIsBlowing(nbt) ? 1 : -1));

                    pPlayer.setDeltaMovement(pPlayer.getDeltaMovement().add(pushVecP.scale(1 / 32f)));
                    pPlayer.setDeltaMovement(pPlayer.getDeltaMovement().add(pushForce));
                    pPlayer.fallDistance = 0;
                    pPlayer.hurtMarked = true;

                    Vec3 currentMovement = pPlayer.getDeltaMovement();
                    if (currentMovement.length() > speedLimit) {
                        Vec3 limitedMovement = currentMovement.normalize().scale(speedLimit);
                        pPlayer.setDeltaMovement(limitedMovement);
                    }
                }
            }
        }
    }

    private void generateParticles(Level pLevel, Player pPlayer, float range, float rangeP, CompoundTag nbt) {
        Vec3 center = getCenterPos(pPlayer);
        Vec3 cursorPos = getBlockSideLookingAt(pPlayer, rangeP);

        if (range != 0) {
            if (randomChance(getIsBlowing(nbt) ? 90 : 60, pLevel)) {
                Vec3 start = VecHelper.offsetRandomly(center, pLevel.random, getIsBlowing(nbt) ? 1 : range / 2);
                Vec3 motionBlow = start.subtract(center)
                        .normalize()
                        .scale(Mth.clamp(range * 0.5, 0, 0.5f));
                Vec3 motionPull = center.subtract(start)
                        .normalize()
                        .scale(Mth.clamp(range, 0.5f, 1.0f));

                Vec3 motion = getIsBlowing(nbt) ? motionBlow : motionPull;

                pLevel.addParticle(ParticleTypes.POOF, start.x, start.y, start.z, motion.x, motion.y, motion.z);
            }
        }

        if (pLevel.random.nextInt(Mth.clamp(((int) rangeP), 1, 2)) == 0) {
            if (cursorPos != null) {
                Vec3 playerPos = getCenterPos(pPlayer).add(0, pPlayer.getEyeHeight(), 0);
                Vec3 direction = cursorPos.subtract(playerPos).normalize();
                double distance = playerPos.distanceTo(cursorPos);
                if (distance > rangeP) distance = rangeP;

                int particleCount = (int) (distance * 0.25);
                for (int i = 0; i < particleCount; i++) {
                    double progress = (double) i / particleCount;
                    Vec3 beamCorePos = playerPos.add(direction.scale(progress * distance));
                    Vec3 randomOffset = new Vec3(
                            (pLevel.random.nextDouble() - 0.5) * 4,
                            (pLevel.random.nextDouble() - 0.5) * 4,
                            (pLevel.random.nextDouble() - 0.5) * 4
                    );
                    Vec3 particlePos = beamCorePos.add(randomOffset);
                    Vec3 motion = direction.scale(getIsBlowing(nbt) ? 0.5 : -0.5)
                            .normalize()
                            .scale(Mth.clamp(range, 0.5f, 1.0f));
                    pLevel.addParticle(ParticleTypes.POOF, particlePos.x, particlePos.y, particlePos.z, motion.x, motion.y, motion.z);
                }
            }
        }
    }

    public static Vec3 getCenterPos(Entity entity) {
        AABB boundingBox = entity.getBoundingBox();

        double centerX = (boundingBox.minX + boundingBox.maxX) / 2.0;
        double centerY = (boundingBox.minY + boundingBox.maxY) / 2.0;
        double centerZ = (boundingBox.minZ + boundingBox.maxZ) / 2.0;

        return new Vec3(centerX, centerY, centerZ);
    }

    public static Vec3 getBlockSideLookingAt(Player player, double maxRange) {

        Vec3 eyePosition = player.getEyePosition(1.0F);
        Vec3 lookDirection = player.getLookAngle();
        Vec3 endPosition = eyePosition.add(lookDirection.scale(maxRange));

        BlockHitResult hitResult = player.level.clip(new ClipContext(
                eyePosition,
                endPosition,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player));

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return hitResult.getLocation();
        }

        return null;
    }

    private boolean canSee(Entity entity, Vec3 pos, Level level) {
        ClipContext context = new ClipContext(getCenterPos(entity), pos,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity);
        return pos.equals(level.clip(context).getLocation());
    }
}