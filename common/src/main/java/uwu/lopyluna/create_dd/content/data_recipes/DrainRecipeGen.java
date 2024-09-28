package uwu.lopyluna.create_dd.content.data_recipes;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.registry.DesiresFluids;

import static uwu.lopyluna.create_dd.registry.DesiresTags.forgeItemTag;

@SuppressWarnings("unused")
public class DrainRecipeGen extends DesireProcessingRecipeGen {


    GeneratedRecipe

    SAP_LOGS = create("sap_from_logs", b -> b.require(forgeItemTag("stripped_logs"))
            .output(DesiresFluids.SAP.get(), 100)
    ),

    SAP_WOOD = create("sap_from_wood", b -> b.require(forgeItemTag("stripped_wood"))
            .output(DesiresFluids.SAP.get(), 100)
    );



    public DrainRecipeGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected @NotNull IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.EMPTYING;
    }
}
