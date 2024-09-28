package uwu.lopyluna.create_dd.content.data_recipes;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.registry.DesiresWoodType;
import uwu.lopyluna.create_dd.registry.helper.woodtype.WoodEntry;

@SuppressWarnings("unused")
public class CuttingRecipeGen extends DesireProcessingRecipeGen {

    public CuttingRecipeGen(DataGenerator generator) {
        super(generator);
    }

    GeneratedRecipe
            ROSE = woodEntrySet(DesiresWoodType.ROSE),
            SMOKED = woodEntrySet(DesiresWoodType.SMOKED),
            RUBBER = woodEntrySet(DesiresWoodType.RUBBER),
            SPIRIT = woodEntrySet(DesiresWoodType.SPIRIT)
    ;


    GeneratedRecipe woodEntrySet(WoodEntry entry) {
        stripAndMakePlanks(entry.log.get(), entry.strippedLog.get(), entry.plank.get());
        return stripAndMakePlanks(entry.wood.get(), entry.strippedWood.get(), entry.plank.get());
    }

     GeneratedRecipe stripAndMakePlanks(Block wood, Block stripped, Block planks) {
        create(() -> wood, b -> b.duration(50)
                .output(stripped));
        return create(() -> stripped, b -> b.duration(50)
                .output(planks, 6));
    }

    @Override
    protected @NotNull IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.CUTTING;
    }
}
