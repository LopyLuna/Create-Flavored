package uwu.lopyluna.create_dd.content.data_recipes;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import uwu.lopyluna.create_dd.registry.DesiresFluids;
import uwu.lopyluna.create_dd.registry.DesiresItems;

import java.util.stream.Stream;

import static uwu.lopyluna.create_dd.registry.DesiresFluids.*;

@MethodsReturnNonnullByDefault
@SuppressWarnings({"unused"})
public class MixingRecipeGen extends DesireProcessingRecipeGen {

    GeneratedRecipe

    CHOCOLATE = create(CHOCOLATE_MILKSHAKE.getId().getPath(), b -> b
            .require(Tags.Fluids.MILK, 250)
            .require(Items.SUGAR)
            .require(Ingredient.fromValues(Stream.of(
                    addItemValue(AllItems.BAR_OF_CHOCOLATE.get())
            )))
            .require(Ingredient.fromValues(Stream.of(
                    addItemValue(Items.SNOW_BLOCK),
                    addItemValue(Items.PACKED_ICE)
            )))
            .output(CHOCOLATE_MILKSHAKE.get(), 250)),

    STRAWBERRY = create(STRAWBERRY_MILKSHAKE.getId().getPath(), b -> b
            .require(Tags.Fluids.MILK, 250)
            .require(Items.SUGAR)
            .require(Ingredient.fromValues(Stream.of(
                    addItemValue(Items.SWEET_BERRIES)
            )))
            .require(Ingredient.fromValues(Stream.of(
                    addItemValue(Items.SNOW_BLOCK),
                    addItemValue(Items.PACKED_ICE)
            )))
            .output(STRAWBERRY_MILKSHAKE.get(), 250)),

    VANILLA = create(VANILLA_MILKSHAKE.getId().getPath(), b -> b
            .require(Tags.Fluids.MILK, 250)
            .require(Items.SUGAR)
            .require(Ingredient.fromValues(Stream.of(
                    addItemValue(Items.DANDELION),
                    addItemValue(Items.BLUE_ORCHID)
            )))
            .require(Ingredient.fromValues(Stream.of(
                    addItemValue(Items.SNOW_BLOCK),
                    addItemValue(Items.PACKED_ICE)
            )))
            .output(VANILLA_MILKSHAKE.get(), 250)),

    GLOWBERRY = create(GLOWBERRY_MILKSHAKE.getId().getPath(), b -> b
            .require(Tags.Fluids.MILK, 250)
            .require(Items.SUGAR)
            .require(Ingredient.fromValues(Stream.of(
                    addItemValue(Items.GLOW_BERRIES)
            )))
            .require(Ingredient.fromValues(Stream.of(
                    addItemValue(Items.SNOW_BLOCK),
                    addItemValue(Items.PACKED_ICE)
            )))
            .output(GLOWBERRY_MILKSHAKE.get(), 250)),

    PUMPKIN = create(PUMPKIN_MILKSHAKE.getId().getPath(), b -> b
            .require(Tags.Fluids.MILK, 250)
            .require(Items.SUGAR)
            .require(Ingredient.fromValues(Stream.of(
                    addItemValue(Items.PUMPKIN),
                    addItemValue(Items.PUMPKIN_PIE),
                    addItemValue(Items.CARVED_PUMPKIN)
            )))
            .require(Ingredient.fromValues(Stream.of(
                    addItemValue(Items.SNOW_BLOCK),
                    addItemValue(Items.PACKED_ICE)
            )))
            .output(PUMPKIN_MILKSHAKE.get(), 250)),


    RAW_RUBBER = create("raw_rubber", b -> b
            .require(DesiresFluids.SAP.get(), 1000)
            .output(DesiresItems.RAW_RUBBER.get(), 1)),

    BURY_BLEND = create("bury_blend", b -> b
            .require(AllItems.CRUSHED_IRON.get())
            .require(Items.LAPIS_LAZULI)
            .output(DesiresItems.BURY_BLEND.get(), 1));

    public MixingRecipeGen(DataGenerator generator) {
        super(generator);
    }


    public Ingredient.ItemValue addItemValue(ItemLike item) {
        return new Ingredient.ItemValue(new ItemStack(item));
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.MIXING;
    }
}
