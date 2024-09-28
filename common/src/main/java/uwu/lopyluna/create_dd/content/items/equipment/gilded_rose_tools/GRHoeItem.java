package uwu.lopyluna.create_dd.content.items.equipment.gilded_rose_tools;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import uwu.lopyluna.create_dd.registry.DesiresItems;
import uwu.lopyluna.create_dd.registry.DesiresTags;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class GRHoeItem extends HoeItem implements RoseTools {
    public GRHoeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier * 0.5F, pProperties);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.matches(pStack)) {
            if (pEntity instanceof Player pPlayer && pIsSelected) {
                setOnCooldown(pStack, pPlayer.getCooldowns().isOnCooldown(pPlayer.getMainHandItem().getItem()));
                setOffHandPower(pStack, DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.matches(pPlayer.getOffhandItem().getItem()));
            }
        }
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        if (DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.matches(pStack)) {
            return isOnCooldown(pStack) ? super.getDestroySpeed(pStack, pState) * 0.1F : super.getDestroySpeed(pStack, pState) * 1.5F;
        } else return super.getDestroySpeed(pStack, pState);
    }

    @Override
    public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        return !isOnCooldown(new ItemStack(DesiresItems.GILDED_ROSE_HOE.get()));
    }

    @Override
    public float getAttackDamage() {
        return isOnCooldown(new ItemStack(DesiresItems.GILDED_ROSE_HOE.get())) ? 0.0F : super.getAttackDamage();
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.matches(pStack)) {
            if (pAttacker instanceof Player pPlayer && !isOnCooldown(pStack)) {
                resetCooldown(pStack, pPlayer, getUseDuration(pStack));
                return super.hurtEnemy(pStack, pTarget, pAttacker);
            }
            return false;
        } else return super.hurtEnemy(pStack, pTarget, pAttacker);
    }


    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.matches(pStack)) {
            if (pEntityLiving instanceof Player pPlayer && !isOnCooldown(pStack)) {
                resetCooldown(pStack, pPlayer, getUseDuration(pStack));
                return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
            }
            return false;
        } else return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.matches(stack)) {
            if (enchantment == Enchantments.VANISHING_CURSE)
                return true;
            if (enchantment == Enchantments.BLOCK_EFFICIENCY)
                return true;
            return enchantment == Enchantments.BLOCK_FORTUNE;
        } else return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.matches(itemStack) ? new ItemStack(DesiresItems.GILDED_ROSE_HOE.get()) : super.getCraftingRemainingItem(itemStack);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack pStack, int amount, T entity, Consumer<T> onBroken) {
        if (DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.matches(pStack)) {
            int fortuneLevel = pStack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
            return super.damageItem(pStack, amount, entity, onBroken) + (fortuneLevel + fortuneLevel) * 5;
        } else
            return super.damageItem(pStack, amount, entity, onBroken);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        if (DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.matches(pStack)) {
            int fortuneLevel = pStack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
            if (isInOffHandPower(pStack)) {
                return (int)(((fortuneLevel * fortuneLevel) * 5) / (efficiencyDuration(pStack) + 1));
            } else {
                return (int)(((fortuneLevel * fortuneLevel) * 10) / (efficiencyDuration(pStack) + 1));
            }
        } else return super.getUseDuration(pStack);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        if (DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.matches(pStack)) {
            return false;
        } else return super.isFoil(pStack);
    }
    @Override
    public boolean isEnchantable(ItemStack pStack) {
        if (DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.matches(pStack)) {
            return false;
        } else return super.isEnchantable(pStack);
    }
}
