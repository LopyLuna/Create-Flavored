package uwu.lopyluna.create_dd.content.data_recipes;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.recipe.CompatMetals;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.simibubi.create.foundation.data.recipe.Mods;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import uwu.lopyluna.create_dd.DesireUtil;
import uwu.lopyluna.create_dd.Desires;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"all"})
public abstract class DesireProcessingRecipeGen extends CreateRecipeProvider {

    protected static final List<DesireProcessingRecipeGen> GENERATORS = new ArrayList<>();


    public static void registerAll(DataGenerator gen) {
        GENERATORS.add(new WashingRecipeGen(gen));
        GENERATORS.add(new SandingRecipeGen(gen));
        GENERATORS.add(new FreezingRecipeGen(gen));
        GENERATORS.add(new SeethingRecipeGen(gen));
        GENERATORS.add(new ItemApplicationRecipeGen(gen));
        GENERATORS.add(new CuttingRecipeGen(gen));
        GENERATORS.add(new MixingRecipeGen(gen));
        GENERATORS.add(new HydraulicCompactingRecipeGen(gen));
        GENERATORS.add(new DrainRecipeGen(gen));

        gen.addProvider(true, new DataProvider() {

            @Override
            public String getName() {
                return Desires.NAME + " Processing Recipes";
            }

            @Override
            public void run(CachedOutput dc) throws IOException {
                GENERATORS.forEach(g -> {
                    try {
                        g.run(dc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public DesireProcessingRecipeGen(DataGenerator generator) {
        super(generator);
    }

    /**
     * Create a processing recipe with a single itemstack ingredient, using its id
     * as the name of the recipe
     */
    protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(String namespace,
                                                                     Supplier<ItemLike> singleIngredient, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = getSerializer();
        GeneratedRecipe generatedRecipe = c -> {
            ItemLike itemLike = singleIngredient.get();
            transform
                    .apply(new ProcessingRecipeBuilder<>(serializer.getFactory(),
                            new ResourceLocation(namespace, RegisteredObjects.getKeyOrThrow(itemLike.asItem())
                                    .getPath())).withItemIngredients(Ingredient.of(itemLike)))
                    .build(c);
        };
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    /**
     * Create a processing recipe with a single itemstack ingredient, using its id
     * as the name of the recipe
     */
    <T extends ProcessingRecipe<?>> GeneratedRecipe create(Supplier<ItemLike> singleIngredient,
                                                           UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return create(Desires.MOD_ID, singleIngredient, transform);
    }


    protected <T extends ProcessingRecipe<?>> GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name,
                                                                                   UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = getSerializer();
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), name.get()))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    /**
     * Create a new processing recipe, with recipe definitions provided by the
     * function
     */
    public <T extends ProcessingRecipe<?>> GeneratedRecipe create(ResourceLocation name,
                                                                  UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return createWithDeferredId(() -> name, transform);
    }

    /**
     * Create a new processing recipe, with recipe definitions provided by the
     * function
     */
    public <T extends ProcessingRecipe<?>> GeneratedRecipe create(String name,
                                                           UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return create(DesireUtil.asResource(name), transform);
    }

    protected abstract IRecipeTypeInfo getRecipeType();

    protected <T extends ProcessingRecipe<?>> ProcessingRecipeSerializer<T> getSerializer() {
        return getRecipeType().getSerializer();
    }

    @Override
    public String getName() {
        return "Create's Processing Recipes: " + getRecipeType().getId()
                .getPath();
    }




    //HELPER
    public CreateRecipeProvider.GeneratedRecipe convert(Block block, Block result) {
        return create(() -> block, b -> b.output(result));
    }

    public CreateRecipeProvider.GeneratedRecipe convert(Item item, Item result) {
        return create(() -> item, b -> b.output(result));
    }
    public CreateRecipeProvider.GeneratedRecipe convert(Supplier<ItemLike> item, Supplier<ItemLike> result) {
        return create(item, b -> b.output((ItemLike) result));
    }
    public CreateRecipeProvider.GeneratedRecipe convert(Item item, Item result, float chance) {
        return create(() -> item, b -> b.output(chance, result));
    }
    public CreateRecipeProvider.GeneratedRecipe convert(Item item, Item result1, float chance1, Item result2, float chance2) {
        return create(() -> item, b -> b.output(chance1, result1).output(chance2, result2));
    }
    public CreateRecipeProvider.GeneratedRecipe convert(Supplier<ItemLike> item, Supplier<ItemLike> result, float chance) {
        return create(item, b -> b.output(chance, (ItemLike) result));
    }

    public CreateRecipeProvider.GeneratedRecipe convert(ItemEntry<Item> item, ItemEntry<Item> result) {
        return create(item::get, b -> b.output(result::get));
    }

    public CreateRecipeProvider.GeneratedRecipe secondaryRecipe(Supplier<ItemLike> item, Supplier<ItemLike> first, Supplier<ItemLike> secondary,
                                                                float secondaryChance) {
        return create(item, b -> b.output(first.get(), 1)
                .output(secondaryChance, secondary.get(), 1));
    }

    public GeneratedRecipe convertChanceRecipe(ItemLike item, ItemLike result, float chance) {
        return create(DesireUtil.asResource(getItemName(result) + "_from_" + getItemName(item)), b -> b.withItemIngredients(Ingredient.of(item)).output(chance, result, 1));
    }

    public GeneratedRecipe crushedOre(Supplier<ItemLike> crushed, ItemLike ingot, ItemLike secondary,
                                      float secondaryChance) {
        return create(crushed::get, b -> b.output(ingot, 1)
                .output(secondaryChance, secondary, 1));
    }

    public GeneratedRecipe moddedCrushedOre(ItemEntry<? extends Item> crushed, CompatMetals metal) {
        String metalName = metal.getName();
        for (Mods mod : metal.getMods()) {
            ResourceLocation ingot = mod.ingotOf(metalName);
            create(mod.getId() + "/" + crushed.getId()
                            .getPath(),
                    b -> b.withItemIngredients(Ingredient.of(crushed::get))
                            .output(1, ingot, 1)
                            .output(0.5f, ingot, 1)
                            .whenModLoaded(mod.getId()));
        }
        return null;
    }
}
