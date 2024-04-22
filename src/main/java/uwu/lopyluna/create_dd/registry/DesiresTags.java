package uwu.lopyluna.create_dd.registry;

import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import uwu.lopyluna.create_dd.DesiresCreate;

import java.util.Collections;

@SuppressWarnings({"all"})
public class DesiresTags {
	public static <T> TagKey<T> optionalTag(IForgeRegistry<T> registry,
		ResourceLocation id) {
		return registry.tags()
			.createOptionalTagKey(id, Collections.emptySet());
	}

	public static <T> TagKey<T> forgeTag(IForgeRegistry<T> registry, String path) {
		return optionalTag(registry, new ResourceLocation("forge", path));
	}

	public static TagKey<Block> forgeBlockTag(String path) {
		return forgeTag(ForgeRegistries.BLOCKS, path);
	}

	public static TagKey<Item> forgeItemTag(String path) {
		return forgeTag(ForgeRegistries.ITEMS, path);
	}

	public static TagKey<Fluid> forgeFluidTag(String path) {
		return forgeTag(ForgeRegistries.FLUIDS, path);
	}

	public enum NameSpace {
		
		MOD(DesiresCreate.MOD_ID, false, true),
		CREATE("create"),
		FORGE("forge"),
		TIC("tconstruct"),
		QUARK("quark")

		;

		public final String id;
		public final boolean optionalDefault;
		public final boolean alwaysDatagenDefault;

		NameSpace(String id) {
			this(id, true, false);
		}

		NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
			this.id = id;
			this.optionalDefault = optionalDefault;
			this.alwaysDatagenDefault = alwaysDatagenDefault;
		}
	}

	public enum DesiresBlockTags {

		FAN_PROCESSING_CATALYSTS_FREEZING(NameSpace.MOD, "fan_processing_catalysts/freezing"),
		FAN_PROCESSING_CATALYSTS_SEETHING(NameSpace.MOD, "fan_processing_catalysts/seething"),
		INDUSTRIAL_FAN_TRANSPARENT,
		BACKPACKS,

		;

		public final TagKey<Block> tag;
		public final boolean alwaysDatagen;

		DesiresBlockTags() {
			this(NameSpace.MOD);
		}

		DesiresBlockTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		DesiresBlockTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		DesiresBlockTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		DesiresBlockTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(ForgeRegistries.BLOCKS, id);
			} else {
				tag = BlockTags.create(id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		@SuppressWarnings("deprecation")
		public boolean matches(Block block) {
			return block.builtInRegistryHolder()
				.is(tag);
		}

		public boolean matches(ItemStack stack) {
			return stack != null && stack.getItem() instanceof BlockItem blockItem && matches(blockItem.getBlock());
		}

		public boolean matches(BlockState state) {
			return state.is(tag);
		}

		private static void init() {}
		
	}

	public enum DesiresItemTags {

		;

		public final TagKey<Item> tag;
		public final boolean alwaysDatagen;

		DesiresItemTags() {
			this(NameSpace.MOD);
		}

		DesiresItemTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		DesiresItemTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		DesiresItemTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		DesiresItemTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(ForgeRegistries.ITEMS, id);
			} else {
				tag = ItemTags.create(id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		@SuppressWarnings("deprecation")
		public boolean matches(Item item) {
			return item.builtInRegistryHolder()
				.is(tag);
		}

		public boolean matches(ItemStack stack) {
			return stack.is(tag);
		}

		private static void init() {}
		
	}

	public enum DesiresFluidTags {

		FAN_PROCESSING_CATALYSTS_FREEZING(NameSpace.MOD, "fan_processing_catalysts/freezing"),
		FAN_PROCESSING_CATALYSTS_SEETHING(NameSpace.MOD, "fan_processing_catalysts/seething"),

		SAP(NameSpace.FORGE)

		;

		public final TagKey<Fluid> tag;
		public final boolean alwaysDatagen;

		DesiresFluidTags() {
			this(NameSpace.MOD);
		}

		DesiresFluidTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		DesiresFluidTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		DesiresFluidTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		DesiresFluidTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(ForgeRegistries.FLUIDS, id);
			} else {
				tag = FluidTags.create(id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		@SuppressWarnings("deprecation")
		public boolean matches(Fluid fluid) {
			return fluid.is(tag);
		}

		public boolean matches(FluidState state) {
			return state.is(tag);
		}

		private static void init() {}
		
	}
	
	public enum DesiresEntityTags {

		;

		public final TagKey<EntityType<?>> tag;
		public final boolean alwaysDatagen;

		DesiresEntityTags() {
			this(NameSpace.MOD);
		}

		DesiresEntityTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		DesiresEntityTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		DesiresEntityTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		DesiresEntityTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(ForgeRegistries.ENTITY_TYPES, id);
			} else {
				tag = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		public boolean matches(EntityType<?> type) {
			return type.is(tag);
		}

		public boolean matches(Entity entity) {
			return matches(entity.getType());
		}

		private static void init() {}
		
	}
	
	public enum DesiresRecipeSerializerTags {

		;

		public final TagKey<RecipeSerializer<?>> tag;
		public final boolean alwaysDatagen;

		DesiresRecipeSerializerTags() {
			this(NameSpace.MOD);
		}

		DesiresRecipeSerializerTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		DesiresRecipeSerializerTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		DesiresRecipeSerializerTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		DesiresRecipeSerializerTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(ForgeRegistries.RECIPE_SERIALIZERS, id);
			} else {
				tag = TagKey.create(Registry.RECIPE_SERIALIZER_REGISTRY, id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		public boolean matches(RecipeSerializer<?> recipeSerializer) {
			return ForgeRegistries.RECIPE_SERIALIZERS.getHolder(recipeSerializer).orElseThrow().is(tag);
		}

		private static void init() {}
	}

	public static void init() {
		DesiresBlockTags.init();
		DesiresItemTags.init();
		DesiresFluidTags.init();
		DesiresEntityTags.init();
		DesiresRecipeSerializerTags.init();
	}
}
