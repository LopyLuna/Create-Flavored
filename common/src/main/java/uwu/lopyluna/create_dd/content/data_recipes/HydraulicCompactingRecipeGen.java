package uwu.lopyluna.create_dd.content.data_recipes;

import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import uwu.lopyluna.create_dd.registry.DesiresFluids;
import uwu.lopyluna.create_dd.registry.DesiresItems;
import uwu.lopyluna.create_dd.registry.DesiresRecipeTypes;

import static uwu.lopyluna.create_dd.registry.DesiresTags.forgeItemTag;

@MethodsReturnNonnullByDefault
@SuppressWarnings({"unused"})
public class HydraulicCompactingRecipeGen extends DesireProcessingRecipeGen {
    public HydraulicCompactingRecipeGen(DataGenerator generator) {
        super(generator);
    }
    GeneratedRecipe RAW_RUBBER = create("raw_rubber_from_sap", b -> b
            .requiresHeat(HeatCondition.HEATED)
            .require(DesiresFluids.SAP.get(), 500)
            .output(DesiresItems.RAW_RUBBER.get(), 2)
    );

    GeneratedRecipe SAP_LOGS = create("sap_from_logs", b -> b
            .requiresHeat(HeatCondition.HEATED)
            .require(forgeItemTag("stripped_logs"))
            .require(forgeItemTag("stripped_logs"))
            .output(DesiresFluids.SAP.get(), 500)
    );

    GeneratedRecipe SAP_WOOD = create("sap_from_wood", b -> b
            .requiresHeat(HeatCondition.HEATED)
            .require(forgeItemTag("stripped_wood"))
            .require(forgeItemTag("stripped_wood"))
            .output(DesiresFluids.SAP.get(), 500)
    );

    GeneratedRecipe TUFF = create("tuff_from_gravel", b -> b
            .requiresHeat(HeatCondition.SUPERHEATED)
            .require(Items.GRAVEL)
            .require(Fluids.LAVA, 500)
            .require(Items.DEEPSLATE)
            .require(Items.ANDESITE)
            .output(Items.TUFF, 2)
    );
    GeneratedRecipe CALCITE = create("calcite_from_bone", b -> b
            .requiresHeat(HeatCondition.HEATED)
            .require(Items.BONE_BLOCK)
            .require(Items.DIORITE)
            .output(Items.CALCITE, 2)
    );
    GeneratedRecipe COBBLE_GENERATOR = create("cobblestone_generator", b -> b
            .require(Fluids.LAVA, 100)
            .require(Fluids.WATER, 500)
            .output(Items.COBBLESTONE, 10)
    );
    GeneratedRecipe STONE_GENERATOR = create("stone_generator", b -> b
            .requiresHeat(HeatCondition.HEATED)
            .require(Fluids.LAVA, 100)
            .require(Fluids.WATER, 500)
            .output(Items.STONE, 10)
    );
    GeneratedRecipe BASALT_GENERATOR = create("basalt_generator", b -> b
            .requiresHeat(HeatCondition.HEATED)
            .require(Fluids.LAVA, 100)
            .require(Items.BLUE_ICE)
            .require(Items.SOUL_SOIL)
            .output(Items.BASALT, 10)
            .output(Items.BLUE_ICE, 1)
            .output(Items.SOUL_SOIL, 1)
    );




    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return DesiresRecipeTypes.HYDRAULIC_COMPACTING;
    }

}
