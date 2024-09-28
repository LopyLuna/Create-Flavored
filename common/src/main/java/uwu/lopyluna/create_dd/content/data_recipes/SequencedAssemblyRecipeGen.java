package uwu.lopyluna.create_dd.content.data_recipes;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.DesireUtil;
import uwu.lopyluna.create_dd.registry.DesiresItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.UnaryOperator;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"unused"})
public class SequencedAssemblyRecipeGen extends CreateRecipeProvider {

    GeneratedRecipe

            KINETIC_MECHANISM = create("kinetic_mechanism", b -> b.require(AllItems.IRON_SHEET.get())
            .transitionTo(DesiresItems.INCOMPLETE_KINETIC_MECHANISM.get())
            .addOutput(DesiresItems.KINETIC_MECHANISM.get(), 480)
            .addOutput(AllItems.ANDESITE_ALLOY.get(), 16)
            .addOutput(AllItems.IRON_SHEET.get(), 8)
            .addOutput(AllBlocks.COGWHEEL.get(), 5)
            .addOutput(AllItems.SUPER_GLUE.asStack(), 4)
            .addOutput(AllItems.ZINC_NUGGET.get(), 3)
            .addOutput(AllBlocks.SHAFT.get(), 2)
            .addOutput(AllItems.CRUSHED_IRON.get(), 2)
            .addOutput(Items.IRON_INGOT, 1)
            .loops(2)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllItems.ANDESITE_ALLOY.get()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllBlocks.COGWHEEL.get()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(Items.SLIME_BALL))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllItems.ZINC_NUGGET.get())))

    ;

    public SequencedAssemblyRecipeGen(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    protected GeneratedRecipe create(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new SequencedAssemblyRecipeBuilder(DesireUtil.asResource(name)))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    @Override
    public String getName() {
        return Desires.NAME + " Sequenced Assembly Recipes";
    }
    
}
