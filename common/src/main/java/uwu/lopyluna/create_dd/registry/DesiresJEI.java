package uwu.lopyluna.create_dd.registry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.compat.jei.*;
import com.simibubi.create.compat.jei.category.*;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import uwu.lopyluna.create_dd.content.jei.category.*;
import uwu.lopyluna.create_dd.content.recipes.DragonBreathingRecipe;
import uwu.lopyluna.create_dd.registry.helper.Lang;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.DesireUtil;
import uwu.lopyluna.create_dd.content.recipes.FreezingRecipe;
import uwu.lopyluna.create_dd.content.recipes.SandingRecipe;
import uwu.lopyluna.create_dd.content.recipes.SeethingRecipe;
import uwu.lopyluna.create_dd.infrastructure.config.DRecipes;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.simibubi.create.compat.jei.CreateJEI.consumeTypedRecipes;

@JeiPlugin
@SuppressWarnings({"unused", "inline", "unchecked"})
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DesiresJEI implements IModPlugin {

	private static final ResourceLocation MOD_ID = new ResourceLocation(Desires.MOD_ID, "jei_plugin");

	@Override
	@Nonnull
	public ResourceLocation getPluginUid() {
		return MOD_ID;
	}

	private static final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();

    private void loadCategories() {
		allCategories.clear();

		CreateRecipeCategory<?>

		hydraulic_compacting = builder(BasinRecipe.class)
				.addTypedRecipes(DesiresRecipeTypes.HYDRAULIC_COMPACTING)
				.catalyst(DesiresBlocks.HYDRAULIC_PRESS::get)
				.catalyst(AllBlocks.BASIN::get)
				.doubleItemIcon(DesiresBlocks.HYDRAULIC_PRESS.get(), AllBlocks.BASIN.get())
				.emptyBackground(177, 103)
				.build("hydraulic_compacting", HydraulicCompactingCategory::new),

		dragon_breathing = builder(DragonBreathingRecipe.class)
				.addTypedRecipes(DesiresRecipeTypes.DRAGON_BREATHING)
				.catalystStack(DProcessingViaFanCategory.getFan("fan_dragon_breathing"))
				.doubleItemIcon(AllItems.PROPELLER.get(), Items.END_CRYSTAL)
				.emptyBackground(178, 72)
				.build("fan_dragon_breathing", FanDragonBreathingCategory::new),
		sanding = builder(SandingRecipe.class)
				.addTypedRecipes(DesiresRecipeTypes.SANDING)
				.catalystStack(DProcessingViaFanCategory.getFan("fan_sanding"))
				.doubleItemIcon(AllItems.PROPELLER.get(), Items.SAND)
				.emptyBackground(178, 72)
				.build("fan_sanding", FanSandingCategory::new),
		freezing = builder(FreezingRecipe.class)
				.addTypedRecipes(DesiresRecipeTypes.FREEZING)
				.catalystStack(DProcessingViaFanCategory.getFan("fan_freezing"))
				.doubleItemIcon(AllItems.PROPELLER.get(), Items.POWDER_SNOW_BUCKET)
				.emptyBackground(178, 72)
				.build("fan_freezing", FanFreezingCategory::new),
		seething = builder(SeethingRecipe.class)
				.addTypedRecipes(DesiresRecipeTypes.SEETHING)
				.catalystStack(DProcessingViaFanCategory.getFan("fan_seething"))
				.doubleItemIcon(AllItems.PROPELLER.get(), AllItems.BLAZE_CAKE.get())
				.emptyBackground(178, 72)
				.build("fan_seething", FanSeethingCategory::new);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
        IIngredientManager ingredientManager = registration.getIngredientManager();
		allCategories.forEach(c -> c.registerRecipes(registration));
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		loadCategories();
		registration.addRecipeCategories(allCategories.toArray(IRecipeCategory[]::new));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		allCategories.forEach(c -> c.registerCatalysts(registration));

		registration.getJeiHelpers().getRecipeType(new ResourceLocation("create", "pressing")).ifPresent(type ->
				registration.addRecipeCatalyst(new ItemStack(DesiresBlocks.HYDRAULIC_PRESS.get()), type));

		registration.getJeiHelpers().getRecipeType(new ResourceLocation("create", "fan_washing")).ifPresent(type ->
				registration.addRecipeCatalyst(new ItemStack(DesiresBlocks.INDUSTRIAL_FAN.get()), type));
		registration.getJeiHelpers().getRecipeType(new ResourceLocation("create", "fan_smoking")).ifPresent(type ->
				registration.addRecipeCatalyst(new ItemStack(DesiresBlocks.INDUSTRIAL_FAN.get()), type));
		registration.getJeiHelpers().getRecipeType(new ResourceLocation("create", "fan_blasting")).ifPresent(type ->
				registration.addRecipeCatalyst(new ItemStack(DesiresBlocks.INDUSTRIAL_FAN.get()), type));
		registration.getJeiHelpers().getRecipeType(new ResourceLocation("create", "fan_haunting")).ifPresent(type ->
				registration.addRecipeCatalyst(new ItemStack(DesiresBlocks.INDUSTRIAL_FAN.get()), type));

		registration.getJeiHelpers().getRecipeType(new ResourceLocation(Desires.MOD_ID, "fan_sanding")).ifPresent(type ->
				registration.addRecipeCatalyst(new ItemStack(AllBlocks.ENCASED_FAN.get()), type));
		registration.getJeiHelpers().getRecipeType(new ResourceLocation(Desires.MOD_ID, "fan_seething")).ifPresent(type ->
				registration.addRecipeCatalyst(new ItemStack(AllBlocks.ENCASED_FAN.get()), type));
		registration.getJeiHelpers().getRecipeType(new ResourceLocation(Desires.MOD_ID, "fan_freezing")).ifPresent(type ->
				registration.addRecipeCatalyst(new ItemStack(AllBlocks.ENCASED_FAN.get()), type));
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(new BlueprintTransferHandler(), RecipeTypes.CRAFTING);
	}

	private <T extends Recipe<?>> DesiresJEI.CategoryBuilder<T> builder(Class<? extends T> recipeClass) {
		return new CategoryBuilder<>(recipeClass);
	}

	private static class CategoryBuilder<T extends Recipe<?>> {
		private final Class<? extends T> recipeClass;
		private Predicate<DRecipes> predicate = cRecipes -> true;

		private IDrawable background;
		private IDrawable icon;

		private final List<Consumer<List<T>>> recipeListConsumers = new ArrayList<>();
		private final List<Supplier<? extends ItemStack>> catalysts = new ArrayList<>();

		public CategoryBuilder(Class<? extends T> recipeClass) {
			this.recipeClass = recipeClass;
		}

		public CategoryBuilder<T> enableIf(Predicate<DRecipes> predicate) {
			this.predicate = predicate;
			return this;
		}

		public CategoryBuilder<T> enableWhen(Function<DRecipes, ConfigBase.ConfigBool> configValue) {
			predicate = c -> configValue.apply(c).get();
			return this;
		}

		public CategoryBuilder<T> addRecipeListConsumer(Consumer<List<T>> consumer) {
			recipeListConsumers.add(consumer);
			return this;
		}

		public CategoryBuilder<T> addRecipes(Supplier<Collection<? extends T>> collection) {
			return addRecipeListConsumer(recipes -> recipes.addAll(collection.get()));
		}

		public CategoryBuilder<T> addAllRecipesIf(Predicate<Recipe<?>> pred) {
			return addRecipeListConsumer(recipes -> consumeAllRecipes(recipe -> {
				if (pred.test(recipe)) {
					recipes.add((T) recipe);
				}
			}));
		}

		public CategoryBuilder<T> addAllRecipesIf(Predicate<Recipe<?>> pred, Function<Recipe<?>, T> converter) {
			return addRecipeListConsumer(recipes -> consumeAllRecipes(recipe -> {
				if (pred.test(recipe)) {
					recipes.add(converter.apply(recipe));
				}
			}));
		}

		public CategoryBuilder<T> addTypedRecipes(IRecipeTypeInfo recipeTypeEntry) {
			return addTypedRecipes(recipeTypeEntry::getType);
		}

		public CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType) {
			return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipes::add, recipeType.get()));
		}

		public CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType, Function<Recipe<?>, T> converter) {
			return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipe -> recipes.add(converter.apply(recipe)), recipeType.get()));
		}

		public CategoryBuilder<T> addTypedRecipesIf(Supplier<RecipeType<? extends T>> recipeType, Predicate<Recipe<?>> pred) {
			return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipe -> {
				if (pred.test(recipe)) {
					recipes.add(recipe);
				}
			}, recipeType.get()));
		}

		public CategoryBuilder<T> addTypedRecipesExcluding(Supplier<RecipeType<? extends T>> recipeType,
														   Supplier<RecipeType<? extends T>> excluded) {
			return addRecipeListConsumer(recipes -> {
				List<Recipe<?>> excludedRecipes = DDgetTypedRecipes(excluded.get());
				CreateJEI.<T>consumeTypedRecipes(recipe -> {
					for (Recipe<?> excludedRecipe : excludedRecipes) {
						if (DDdoInputsMatch(recipe, excludedRecipe)) {
							return;
						}
					}
					recipes.add(recipe);
				}, recipeType.get());
			});
		}

		public CategoryBuilder<T> removeRecipes(Supplier<RecipeType<? extends T>> recipeType) {
			return addRecipeListConsumer(recipes -> {
				List<Recipe<?>> excludedRecipes = DDgetTypedRecipes(recipeType.get());
				recipes.removeIf(recipe -> {
					for (Recipe<?> excludedRecipe : excludedRecipes) {
						if (DDdoInputsMatch(recipe, excludedRecipe)) {
							return true;
						}
					}
					return false;
				});
			});
		}


		public static List<Recipe<?>> DDgetTypedRecipes(RecipeType<?> type) {
			List<Recipe<?>> recipes = new ArrayList<>();
			consumeTypedRecipes(recipes::add, type);
			return recipes;
		}

		public static List<Recipe<?>> DDgetTypedRecipesExcluding(RecipeType<?> type, Predicate<Recipe<?>> exclusionPred) {
			List<Recipe<?>> recipes = DDgetTypedRecipes(type);
			recipes.removeIf(exclusionPred);
			return recipes;
		}

		public static boolean DDdoInputsMatch(Recipe<?> recipe1, Recipe<?> recipe2) {
			if (recipe1.getIngredients()
					.isEmpty()
					|| recipe2.getIngredients()
					.isEmpty()) {
				return false;
			}
			ItemStack[] matchingStacks = recipe1.getIngredients()
					.get(0)
					.getItems();
			if (matchingStacks.length == 0) {
				return false;
			}
			return recipe2.getIngredients()
					.get(0)
					.test(matchingStacks[0]);
		}

		public CategoryBuilder<T> catalystStack(Supplier<ItemStack> supplier) {
			catalysts.add(supplier);
			return this;
		}

		public CategoryBuilder<T> catalyst(Supplier<ItemLike> supplier) {
			return catalystStack(() -> new ItemStack(supplier.get()
					.asItem()));
		}

		public void icon(IDrawable icon) {
			this.icon = icon;
		}

		public CategoryBuilder<T> itemIcon(ItemLike item) {
			icon(new ItemIcon(() -> new ItemStack(item)));
			return this;
		}

		public CategoryBuilder<T> doubleItemIcon(ItemLike item1, ItemLike item2) {
			icon(new DoubleItemIcon(() -> new ItemStack(item1), () -> new ItemStack(item2)));
			return this;
		}

		public void background(IDrawable background) {
			this.background = background;
		}

		public CategoryBuilder<T> emptyBackground(int width, int height) {
			background(new EmptyBackground(width, height));
			return this;
		}

		public CreateRecipeCategory<T> build(String name, CreateRecipeCategory.Factory<T> factory) {
			Supplier<List<T>> recipesSupplier;
			if (predicate.test(DesiresConfigs.server().recipes)) {
				recipesSupplier = () -> {
					List<T> recipes = new ArrayList<>();
					for (Consumer<List<T>> consumer : recipeListConsumers)
						consumer.accept(recipes);
					return recipes;
				};
			} else {
				recipesSupplier = Collections::emptyList;
			}

			CreateRecipeCategory.Info<T> info = new CreateRecipeCategory.Info<>(
					new mezz.jei.api.recipe.RecipeType<>(DesireUtil.asResource(name), recipeClass),
					Lang.translateDirect("recipe." + name), background, icon, recipesSupplier, catalysts);
			CreateRecipeCategory<T> category = factory.create(info);
			allCategories.add(category);
			return category;
		}
	}

	public static void consumeAllRecipes(Consumer<Recipe<?>> consumer) {
		Objects.requireNonNull(Minecraft.getInstance()
						.getConnection())
				.getRecipeManager()
				.getRecipes()
				.forEach(consumer);
	}

}