package uwu.lopyluna.create_dd.registry;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeFactory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import uwu.lopyluna.create_dd.registry.helper.Lang;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.content.recipes.*;
import uwu.lopyluna.create_dd.DesireUtil;

import java.util.Optional;
import java.util.function.Supplier;

public enum DesiresRecipeTypes implements IRecipeTypeInfo {

	HYDRAULIC_COMPACTING(HydraulicCompactingRecipe::new),
	DRAGON_BREATHING(DragonBreathingRecipe::new),
	SANDING(SandingRecipe::new),
	FREEZING(FreezingRecipe::new),
	SEETHING(SeethingRecipe::new);



	private final ResourceLocation id;
	private final RegistrySupplier<RecipeSerializer<?>> serializerObject;
	private final Supplier<RecipeType<?>> type;

	DesiresRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
		String name = Lang.asId(name());
		id = DesireUtil.asResource(name);
		serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
		@Nullable RegistrySupplier<RecipeType<?>> typeObject = Registers.TYPE_REGISTER.register(name, () -> RecipeType.simple(id));
		type = typeObject;
	}

	DesiresRecipeTypes(ProcessingRecipeFactory<?> processingFactory) {
		this(() -> new ProcessingRecipeSerializer<>(processingFactory));
	}

	public static void register() {
		ShapedRecipe.setCraftingSize(9, 9);
		Registers.SERIALIZER_REGISTER.register();
		Registers.TYPE_REGISTER.register();
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends RecipeSerializer<?>> T getSerializer() {
		return (T) serializerObject.get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends RecipeType<?>> T getType() {
		return (T) type.get();
	}

	public <C extends Container, T extends Recipe<C>> Optional<T> find(C inv, Level world) {
		return world.getRecipeManager()
			.getRecipeFor(getType(), inv, world);
	}

	private static class Registers {
		private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(Desires.MOD_ID, Registry.RECIPE_SERIALIZER_REGISTRY);
		private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Desires.MOD_ID, Registry.RECIPE_TYPE_REGISTRY);
	}

}
