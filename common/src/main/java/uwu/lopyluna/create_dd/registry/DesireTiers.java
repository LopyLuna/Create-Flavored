package uwu.lopyluna.create_dd.registry;

import com.simibubi.create.AllItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class DesireTiers {

    public static final Tier GILDED_ROSE = new Tier()
    {
        @Override
        public int getUses() {
            return 1500;
        }

        @Override
        public float getSpeed() {
            return 12.0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 3.0F;
        }

        @Override
        public int getLevel() {
            return 3;
        }

        @Override
        public int getEnchantmentValue() {
            return 22;
        }

        @NotNull
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(AllItems.ROSE_QUARTZ.get());
        }
    };

    public static final Tier Deforester = new Tier()
    {
        @Override
        public int getUses() {
            return 512;
        }

        @Override
        public float getSpeed() {
            return 8.0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 2.0F;
        }

        @Override
        public int getLevel() {
            return 2;
        }

        @Override
        public int getEnchantmentValue() {
            return 15;
        }

        @NotNull
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(AllItems.ANDESITE_ALLOY.get());
        }
    };

    public static final Tier Drill = new Tier()
    {
        @Override
        public int getUses() {
            return 512;
        }

        @Override
        public float getSpeed() {
            return 8.0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 2.0F;
        }

        @Override
        public int getLevel() {
            return 3;
        }

        @Override
        public int getEnchantmentValue() {
            return 25;
        }

        @NotNull
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(DesiresItems.BURY_BLEND.get(), AllItems.BRASS_INGOT.get());
        }
    };
}
