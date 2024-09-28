package uwu.lopyluna.create_dd.content.data_recipes;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.registry.DesiresPaletteStoneTypes;

@SuppressWarnings("unused")
public class FillingRecipeGen extends DesireProcessingRecipeGen {
    public FillingRecipeGen(DataGenerator generator) {
        super(generator);
    }
    GeneratedRecipe COBBLED_DEEPSLATE = create("cobbled_deepslate", b -> b
            .require(PotionFluidHandler.potionIngredient(Potions.STRONG_HARMING, 50))
            .require(Items.COBBLED_DEEPSLATE)
            .output(.5f, Items.NETHERRACK)
    );
    GeneratedRecipe DRIPSTONE_BLOCK = create("dripstone_block", b -> b
            .require(PotionFluidHandler.potionIngredient(Potions.HARMING, 10))
            .require(DesiresPaletteStoneTypes.GABBRO.getBaseBlock().get())
            .output(.75f, Items.DRIPSTONE_BLOCK)
    );
    GeneratedRecipe END_STONE = create("cobbled_deepslate", b -> b
            .require(PotionFluidHandler.potionIngredient(Potions.LONG_INVISIBILITY, 25))
            .require(Items.COBBLED_DEEPSLATE)
            .output(.5f, Items.END_STONE)
    );

    @Override
    protected @NotNull IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.FILLING;
    }
}
