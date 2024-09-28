package uwu.lopyluna.create_dd.content.blocks.kinetics.modular_drill;

import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.registry.helper.Lang;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModularDrillHeadItem extends DiggerItem {

    public enum DrillType implements StringRepresentable {
        VEIN,
        ENCHANTABLE,
        CRUSHER,
        SMELTING;

        @Override
        public @NotNull String getSerializedName() {
            return Lang.asId(name());
        }
    }

    private final DrillType drillType;

    @SuppressWarnings("all")
    public ModularDrillHeadItem(Properties properties, DrillType drillType) {
        super(1, 1, Tiers.STONE, null, properties);
        this.drillType = drillType;
    }

    public DrillType getDrillType() {
        return drillType;
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return 1.0F;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        return true;
    }
    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        return true;
    }
    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return super.getDefaultAttributeModifiers(pEquipmentSlot);
    }
    
    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return drillType == DrillType.ENCHANTABLE;
    }
}
