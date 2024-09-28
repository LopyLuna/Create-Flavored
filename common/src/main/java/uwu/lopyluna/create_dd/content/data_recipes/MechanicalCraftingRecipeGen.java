package uwu.lopyluna.create_dd.content.data_recipes;

import com.google.common.base.Supplier;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.simibubi.create.foundation.data.recipe.MechanicalCraftingRecipeBuilder;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.DesireUtil;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;
import uwu.lopyluna.create_dd.registry.DesiresItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.UnaryOperator;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"unused", "SameParameterValue"})
public class MechanicalCraftingRecipeGen extends CreateRecipeProvider {

    GeneratedRecipe

            HANDHELD_NOZZLE = create(DesiresItems.HANDHELD_NOZZLE::get).returns(1)
            .recipe(b -> b
                    .key('B', AllTags.forgeItemTag("storage_blocks/copper"))
                    .key('C', AllTags.forgeItemTag("ingots/copper"))
                    .key('W', ItemTags.WOOL)
                    .key('M', AllItems.PRECISION_MECHANISM.get())
                    .key('P', AllItems.PROPELLER.get())
                    .key('E', AllItems.ELECTRON_TUBE.get())
                    .key('F', AllBlocks.FLUID_PIPE.get())
                    .key('A', AllItems.ANDESITE_ALLOY.get())
                    .patternLine("WBACE  ")
                    .patternLine("WBPCMCC")
                    .patternLine(" AFFACC")
            ),

            BLOCK_ZAPPER = create(DesiresItems.BLOCK_ZAPPER::get).returns(1)
            .recipe(b -> b
                    .key('S', AllTags.forgeItemTag("storage_blocks/brass"))
                    .key('Y', AllTags.forgeItemTag("ingots/brass"))
                    .key('P', AllTags.forgeItemTag("plates/brass"))
                    .key('Z', AllTags.forgeItemTag("ingots/zinc"))
                    .key('B', AllTags.forgeItemTag("ingots/bury_blend"))
                    .key('I', AllTags.forgeItemTag("ingots/iron"))
                    .key('N', Tags.Items.NETHER_STARS)
                    .key('M', AllItems.PRECISION_MECHANISM.get())
                    .patternLine("ZBSYYY")
                    .patternLine(" MNPPP")
                    .patternLine("III   ")
            ),

            FURNACE_ENGINE = create(DesiresBlocks.FURNACE_ENGINE::get).returns(1)
            .recipe(b -> b
                    .key('S', AllTags.forgeItemTag("plates/brass"))
                    .key('O', AllTags.forgeItemTag("dusts/obsidian"))
                    .key('N', AllTags.forgeItemTag("nuggets/zinc"))
                    .key('B', AllTags.forgeItemTag("storage_blocks/zinc"))
                    .key('C', DesiresBlocks.INDUSTRIAL_CASING.get())
                    .key('A', AllItems.ANDESITE_ALLOY.get())
                    .patternLine(" S S ")
                    .patternLine(" ACA ")
                    .patternLine("NOBON")
            ),

            HYDRAULIC_PRESS = create(DesiresBlocks.HYDRAULIC_PRESS::get).returns(1)
            .recipe(b -> b
                    .key('P', AllBlocks.FLUID_PIPE.get())
                    .key('M', AllBlocks.MECHANICAL_PRESS.get())
                    .key('H', DesiresBlocks.HYDRAULIC_CASING.get())
                    .key('C', Items.COPPER_BLOCK)
                    .patternLine(" P ")
                    .patternLine(" H ")
                    .patternLine("CMC")
            ),

            EXCAVATION_DRILL = create(DesiresItems.EXCAVATION_DRILL::get).returns(1)
            .recipe(b -> b
                    .key('S', AllTags.forgeItemTag("storage_blocks/brass"))
                    .key('Y', AllTags.forgeItemTag("ingots/brass"))
                    .key('P', AllTags.forgeItemTag("plates/brass"))
                    .key('M', AllItems.PRECISION_MECHANISM.get())
                    .key('A', AllItems.ANDESITE_ALLOY.get())
                    .key('B', DesiresItems.BURY_BLEND.get())
                    .key('I', Items.IRON_INGOT)
                    .patternLine(" APPP  ")
                    .patternLine("AIMBB  ")
                    .patternLine(" ASYYYY")
            ),

            GILDED_ROSE_SWORD = create(DesiresItems.GILDED_ROSE_SWORD::get).returns(1)
            .recipe(b -> b
                    .key('R', AllItems.ROSE_QUARTZ.get())
                    .key('G', AllTags.forgeItemTag("plates/gold"))
                    .key('Z', AllTags.forgeItemTag("ingots/zinc"))
                    .key('E', AllItems.EXP_NUGGET.get())
                    .key('T', Items.DIAMOND_SWORD)
                    .patternLine(" R ")
                    .patternLine(" R ")
                    .patternLine("RER")
                    .patternLine("ZTZ")
                    .patternLine(" G ")
            ),
            GILDED_ROSE_PICKAXE = create(DesiresItems.GILDED_ROSE_PICKAXE::get).returns(1)
            .recipe(b -> b
                    .key('R', AllItems.ROSE_QUARTZ.get())
                    .key('G', AllTags.forgeItemTag("plates/gold"))
                    .key('Z', AllTags.forgeItemTag("ingots/zinc"))
                    .key('E', AllItems.EXP_NUGGET.get())
                    .key('T', Items.DIAMOND_PICKAXE)
                    .patternLine(" E ")
                    .patternLine("RGR")
                    .patternLine("RTR")
                    .patternLine(" Z ")
                    .patternLine(" Z ")
            ),
            GILDED_ROSE_AXE = create(DesiresItems.GILDED_ROSE_AXE::get).returns(1)
            .recipe(b -> b
                    .key('R', AllItems.ROSE_QUARTZ.get())
                    .key('G', AllTags.forgeItemTag("plates/gold"))
                    .key('Z', AllTags.forgeItemTag("ingots/zinc"))
                    .key('E', AllItems.EXP_NUGGET.get())
                    .key('T', Items.DIAMOND_AXE)
                    .patternLine("RE ")
                    .patternLine("RGR")
                    .patternLine("RT ")
                    .patternLine(" Z ")
                    .patternLine(" Z ")
            ),
            GILDED_ROSE_SHOVEL = create(DesiresItems.GILDED_ROSE_SHOVEL::get).returns(1)
            .recipe(b -> b
                    .key('R', AllItems.ROSE_QUARTZ.get())
                    .key('G', AllTags.forgeItemTag("plates/gold"))
                    .key('Z', AllTags.forgeItemTag("ingots/zinc"))
                    .key('E', AllItems.EXP_NUGGET.get())
                    .key('T', Items.DIAMOND_SHOVEL)
                    .patternLine(" R ")
                    .patternLine("RER")
                    .patternLine("GTG")
                    .patternLine(" Z ")
                    .patternLine(" Z ")
            ),
            GILDED_ROSE_HOE = create(DesiresItems.GILDED_ROSE_HOE::get).returns(1)
            .recipe(b -> b
                    .key('R', AllItems.ROSE_QUARTZ.get())
                    .key('G', AllTags.forgeItemTag("plates/gold"))
                    .key('Z', AllTags.forgeItemTag("ingots/zinc"))
                    .key('E', AllItems.EXP_NUGGET.get())
                    .key('T', Items.DIAMOND_HOE)
                    .patternLine(" E ")
                    .patternLine("RRG")
                    .patternLine(" T ")
                    .patternLine(" Z ")
                    .patternLine(" Z ")
            )

    ;

    public MechanicalCraftingRecipeGen(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder(result);
    }

    class GeneratedRecipeBuilder {

        private String suffix;
        private final Supplier<ItemLike> result;
        private int amount;

        public GeneratedRecipeBuilder(Supplier<ItemLike> result) {
            this.suffix = "";
            this.result = result;
            this.amount = 1;
        }

        GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }

        GeneratedRecipeBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        GeneratedRecipe recipe(UnaryOperator<MechanicalCraftingRecipeBuilder> builder) {
            return register(consumer -> {
                MechanicalCraftingRecipeBuilder b =
                        builder.apply(MechanicalCraftingRecipeBuilder.shapedRecipe(result.get(), amount));
                ResourceLocation location = DesireUtil.asResource("mechanical_crafting/" + RegisteredObjects.getKeyOrThrow(result.get()
                                .asItem())
                        .getPath() + suffix);
                b.build(consumer, location);
            });
        }
    }

    @Override
    public String getName() {
        return Desires.NAME + " Mechanical Crafting Recipes";
    }
    
}
