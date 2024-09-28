package uwu.lopyluna.create_dd.content.items.equipment.gilded_rose_tools;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

public interface RoseTools {

    default boolean isOnCooldown(ItemStack pStack) {
        return pStack.getOrCreateTag().getBoolean("isOnCooldown");
    }
    default void setOnCooldown(ItemStack pStack, boolean pValue) {
        pStack.getOrCreateTag().putBoolean("isOnCooldown", pValue);
    }

    default boolean isInOffHandPower(ItemStack pStack) {
        return pStack.getOrCreateTag().getBoolean("offHandPower");
    }
    default void setOffHandPower(ItemStack pStack, boolean pValue) {
        pStack.getOrCreateTag().putBoolean("offHandPower", pValue);
    }

    default float efficiencyDuration(ItemStack pStack) {
        int efficiencyLevel = pStack.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
        return efficiencyLevel == 0 ? 1.5F :
               efficiencyLevel == 1 ? 2.0F :
               efficiencyLevel == 2 ? 2.5F :
               efficiencyLevel == 3 ? 3.0F :
               efficiencyLevel == 4 ? 3.5F :
               efficiencyLevel == 5 ? 4.0F :
                                      1.0F ;
    }

    default float sweepingDuration(ItemStack pStack) {
        int sweepingLevel = pStack.getEnchantmentLevel(Enchantments.SWEEPING_EDGE);
        return sweepingLevel == 0 ? 1.5F :
               sweepingLevel == 1 ? 2.0F :
               sweepingLevel == 2 ? 2.5F :
               sweepingLevel == 3 ? 3.0F :
                                    1.0F ;
    }

    default void resetCooldown(ItemStack pStack, Player pPlayer, float getUseDuration) {
        if (!pPlayer.getCooldowns().isOnCooldown(pPlayer.getMainHandItem().getItem())) {
            pPlayer.getCooldowns().addCooldown(pStack.getItem(), (int) getUseDuration);
        }
        pStack.hurtAndBreak(2, pPlayer, (p_43276_) -> p_43276_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    }
}
