package uwu.lopyluna.create_dd.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import uwu.lopyluna.create_dd.registry.DesiresTags;

@Mixin(value = Block.class)
public class MixinBlock {
//Thanks IThundxr for this

    @WrapOperation(method = "playerDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"))
    private void create_dd$roseToolFortune(Player instance, float pExhaustion, Operation<Void> original, @Local(argsOnly = true) Level pLevel, @Local(argsOnly = true) Player pPlayer,
                                           @Local(argsOnly = true) BlockPos pPos, @Local(argsOnly = true) BlockState pState, @Local(argsOnly = true) ItemStack pTool) {
        if (DesiresTags.AllItemTags.ADDITIONAL_DROPS_TOOL.matches(pTool)) {
            int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, pTool);
            int lootingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, pTool);

            pTool.setTag(null);
            pTool.enchant(Enchantments.BLOCK_FORTUNE, fortuneLevel + fortuneLevel + lootingLevel + 1);
            pLevel.playSound(pPlayer, pPos, pState.getBlock().defaultBlockState().getSoundType().getPlaceSound(), SoundSource.BLOCKS,
                    (pState.getBlock().defaultBlockState().getSoundType().getVolume() + 1.0F) / 2.0F, pState.getBlock().defaultBlockState().getSoundType().getPitch() * 1.2F);

            original.call(instance, 0.001F);
        } else {
            original.call(instance, pExhaustion);
        }
    }


}
