package uwu.lopyluna.create_dd.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import uwu.lopyluna.create_dd.registry.DesiresTags;

@Mixin(value = ForgeHooks.class, remap = false)
public class MixinForgeHooks {

    @ModifyExpressionValue(method = "getLootingLevel(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)I",
            at = @At(value = "INVOKE", target =
                    "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getMobLooting(Lnet/minecraft/world/entity/LivingEntity;)I"))
    private static int create_dd$getMobLooting(int original, Entity target, Entity killer){
        ItemStack pTool = ((LivingEntity) killer).getItemInHand(((LivingEntity) killer).getUsedItemHand());
        return DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.matches(pTool) ? original * original * original + 1 : original;
    }

    @ModifyExpressionValue(method = "getLootingLevel(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)I",
            at = @At(value = "INVOKE", target =
                    "Lnet/minecraftforge/common/ForgeHooks;getLootingLevel(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;I)I"))
    private static int create_dd$getLootingLevel(int original, Entity target, Entity killer){
        ItemStack pTool = ((LivingEntity) target).getItemInHand(((LivingEntity) target).getUsedItemHand());

        return DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.matches(pTool) ? original * original * original + 1 : original;
    }
}
