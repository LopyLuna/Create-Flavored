package uwu.lopyluna.create_dd.content.data_recipes;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;
import uwu.lopyluna.create_dd.registry.DesiresItems;
import uwu.lopyluna.create_dd.registry.DesiresPaletteBlocks;

import static uwu.lopyluna.create_dd.registry.DesiresTags.forgeItemTag;

@MethodsReturnNonnullByDefault
@SuppressWarnings({"unused"})
public class ItemApplicationRecipeGen extends DesireProcessingRecipeGen {
    public ItemApplicationRecipeGen(DataGenerator generator) {
        super(generator);
    }

    GeneratedRecipe BURDEN_CHUTE = create("burden_chute", b -> b
            .require(AllBlocks.CHUTE.get()) //Block
            .require(DesiresItems.BURY_BLEND.get()) //Item to Apply
            .output(DesiresBlocks.BURDEN_CHUTE.get())); //Output

    GeneratedRecipe OVERBURDEN = create("overburden_casing", b -> b
            .require(AllBlocks.ANDESITE_CASING.get()) //Block
            .require(DesiresItems.BURY_BLEND.get()) //Item to Apply
            .output(DesiresBlocks.OVERBURDEN_CASING.get())); //Output

    GeneratedRecipe INDUSTRIAL = create("industrial_casing", b -> b
            .require(AllBlocks.INDUSTRIAL_IRON_BLOCK.get()) //Block
            .require(AllItems.ZINC_INGOT.get()) //Item to Apply
            .output(DesiresBlocks.INDUSTRIAL_CASING.get())); //Output

    GeneratedRecipe HYDRAULIC = create("hydraulic_casing", b -> b
            .require(AllBlocks.COPPER_CASING.get()) //Block
            .require(AllItems.COPPER_SHEET.get()) //Item to Apply
            .output(DesiresBlocks.HYDRAULIC_CASING.get())); //Output

    GeneratedRecipe COGWHEEL = create("cogwheel", b -> b
            .require(AllBlocks.SHAFT.get()) //Block
            .require(ItemTags.PLANKS) //Item to Apply
            .output(AllBlocks.COGWHEEL.get())); //Output

    GeneratedRecipe LARGE_COGWHEEL = create("large_cogwheel", b -> b
            .require(AllBlocks.COGWHEEL.get()) //Block
            .require(ItemTags.PLANKS) //Item to Apply
            .output(AllBlocks.LARGE_COGWHEEL.get())); //Output

    GeneratedRecipe DOWNGRADE_COGWHEEL = create("downgrade_cogwheel", b -> b
            .require(AllBlocks.COGWHEEL.get()) //Block
            .require(forgeItemTag("nuggets/coal")) //Item to Apply
            .output(AllBlocks.SHAFT.get())); //Output

    GeneratedRecipe DOWNGRADE_LARGE_COGWHEEL = create("downgrade_large_cogwheel", b -> b
            .require(AllBlocks.LARGE_COGWHEEL.get()) //Block
            .require(forgeItemTag("nuggets/coal")) //Item to Apply
            .output(AllBlocks.COGWHEEL.get())); //Output

    GeneratedRecipe
        BLACK_VELVET_BLOCK = create("black_velvet_block", b -> b
            .require(Blocks.BLACK_WOOL)
            .require(Tags.Items.NUGGETS_GOLD)
            .output(DesiresPaletteBlocks.BLACK_VELVET_BLOCK.get())),

        WHITE_VELVET_BLOCK = create("white_velvet_block", b -> b
                .require(Blocks.WHITE_WOOL)
                .require(Tags.Items.NUGGETS_GOLD)
                .output(DesiresPaletteBlocks.WHITE_VELVET_BLOCK.get())),

        BLUE_VELVET_BLOCK = create("blue_velvet_block", b -> b
                .require(Blocks.BLUE_WOOL)
                .require(Tags.Items.NUGGETS_GOLD)
                .output(DesiresPaletteBlocks.BLUE_VELVET_BLOCK.get())),

        LIGHT_BLUE_VELVET_BLOCK = create("light_blue_velvet_block", b -> b
                .require(Blocks.LIGHT_BLUE_WOOL)
                .require(Tags.Items.NUGGETS_GOLD)
                .output(DesiresPaletteBlocks.LIGHT_BLUE_VELVET_BLOCK.get())),

        RED_VELVET_BLOCK = create("red_velvet_block", b -> b
                .require(Blocks.RED_WOOL)
                .require(Tags.Items.NUGGETS_GOLD)
                .output(DesiresPaletteBlocks.RED_VELVET_BLOCK.get())),

        GREEN_VELVET_BLOCK = create("green_velvet_block", b -> b
                .require(Blocks.GREEN_WOOL)
                .require(Tags.Items.NUGGETS_GOLD)
                .output(DesiresPaletteBlocks.GREEN_VELVET_BLOCK.get())),

        LIME_VELVET_BLOCK = create("lime_velvet_block", b -> b
                .require(Blocks.LIME_WOOL)
                .require(Tags.Items.NUGGETS_GOLD)
                .output(DesiresPaletteBlocks.LIME_VELVET_BLOCK.get())),

        PINK_VELVET_BLOCK = create("pink_velvet_block", b -> b
                .require(Blocks.PINK_WOOL)
                .require(Tags.Items.NUGGETS_GOLD)
                .output(DesiresPaletteBlocks.PINK_VELVET_BLOCK.get())),

        MAGENTA_VELVET_BLOCK = create("magenta_velvet_block", b -> b
                .require(Blocks.MAGENTA_WOOL)
                .require(Tags.Items.NUGGETS_GOLD)
                .output(DesiresPaletteBlocks.MAGENTA_VELVET_BLOCK.get())),

        YELLOW_VELVET_BLOCK = create("yellow_velvet_block", b -> b
                .require(Blocks.YELLOW_WOOL)
                .require(Tags.Items.NUGGETS_GOLD)
                .output(DesiresPaletteBlocks.YELLOW_VELVET_BLOCK.get())),

        GRAY_VELVET_BLOCK = create("gray_velvet_block", b -> b
                .require(Blocks.GRAY_WOOL)
                .require(Tags.Items.NUGGETS_GOLD)
                .output(DesiresPaletteBlocks.GRAY_VELVET_BLOCK.get())),

        LIGHT_GRAY_VELVET_BLOCK = create("light_gray_velvet_block", b -> b
                .require(Blocks.LIGHT_GRAY_WOOL)
                .require(Tags.Items.NUGGETS_GOLD)
                .output(DesiresPaletteBlocks.LIGHT_GRAY_VELVET_BLOCK.get())),

        BROWN_VELVET_BLOCK = create("brown_velvet_block", b -> b
                .require(Blocks.BROWN_WOOL)
                .require(Tags.Items.NUGGETS_GOLD)
                .output(DesiresPaletteBlocks.BROWN_VELVET_BLOCK.get())),

        CYAN_VELVET_BLOCK = create("cyan_velvet_block", b -> b
                .require(Blocks.CYAN_WOOL)
                .require(Tags.Items.NUGGETS_GOLD)
                .output(DesiresPaletteBlocks.CYAN_VELVET_BLOCK.get())),

        PURPLE_VELVET_BLOCK = create("purple_velvet_block", b -> b
                .require(Blocks.PURPLE_WOOL)
                .require(Tags.Items.NUGGETS_GOLD)
                .output(DesiresPaletteBlocks.PURPLE_VELVET_BLOCK.get())),

        ORANGE_VELVET_BLOCK = create("orange_velvet_block", b -> b
                .require(Blocks.ORANGE_WOOL)
                .require(Tags.Items.NUGGETS_GOLD)
                .output(DesiresPaletteBlocks.ORANGE_VELVET_BLOCK.get()))
    ;

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.ITEM_APPLICATION;
    }
}
