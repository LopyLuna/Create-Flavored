package uwu.lopyluna.create_dd.registry;

import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
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

import java.util.Collections;
import java.util.Objects;

import static uwu.lopyluna.create_dd.Desires.MOD_ID;
import static uwu.lopyluna.create_dd.registry.DesiresTags.NameSpace.FORGE;

@SuppressWarnings({"unused"})
public class DesiresTags {
	public static <T> TagKey<T> optionalTag(ResourceKey<Registry<T>> registry,
		ResourceLocation id) {
		return TagKey.create(registry, id);
	}

	public static <T> TagKey<T> forgeTag(ResourceKey<Registry<T>> registry, String path) {
		return optionalTag(registry, new ResourceLocation("forge", path));
	}
	public static <T> TagKey<T> mcTag(ResourceKey<Registry<T>> registry, String path) {
		return optionalTag(registry, new ResourceLocation("minecraft", path));
	}
	public static <T> TagKey<T> modTag(ResourceKey<Registry<T>> registry, String path) {
		return optionalTag(registry, new ResourceLocation(MOD_ID, path));
	}

	public static TagKey<Block> forgeBlockTag(String path) {
		return forgeTag(Registry.BLOCK_REGISTRY, path);
	}

	public static TagKey<Item> forgeItemTag(String path) {
		return forgeTag(Registry.ITEM_REGISTRY, path);
	}
	public static TagKey<Item> modItemTag(String path) {
		return modTag(Registry.ITEM_REGISTRY, path);
	}
	public static TagKey<Item> mcItemTag(String path) {
		return mcTag(Registry.ITEM_REGISTRY, path);
	}

	public static TagKey<Fluid> forgeFluidTag(String path) {
		return forgeTag(Registry.FLUID_REGISTRY, path);
	}

	public enum NameSpace {
		
		MOD(MOD_ID, false, true),
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

	public enum AllBlockTags {

		FAN_PROCESSING_CATALYSTS_DRAGON_BREATHING(NameSpace.MOD, "fan_processing_catalysts/dragon_breathing"),
		FAN_PROCESSING_CATALYSTS_SANDING(NameSpace.MOD, "fan_processing_catalysts/sanding"),
		FAN_PROCESSING_CATALYSTS_FREEZING(NameSpace.MOD, "fan_processing_catalysts/freezing"),
		FAN_PROCESSING_CATALYSTS_SEETHING(NameSpace.MOD, "fan_processing_catalysts/seething"),
		INDUSTRIAL_FAN_HEATER,
		INDUSTRIAL_FAN_TRANSPARENT,
		BACKPACKS,
		EXCAVATION_DRILL_VEIN_VALID,
		EXCAVATION_DRILL_VEIN_LARGE,
		MODULAR_VEIN,
		MODULAR_VEIN_SMALL,
		MODULAR_VEIN_MEDIUM,
		MODULAR_VEIN_LARGE,
		BLOCK_ZAPPER_REPLACEABLE,
		BLOCK_ZAPPER_BLACKLIST,
		FLAMMABLE_WOOD,
		ARTIFICIAL_ORE_GENERATOR,
		ORE_GENERATOR,

		;

		public final TagKey<Block> tag;
		public final boolean alwaysDatagen;

		AllBlockTags() {
			this(NameSpace.MOD);
		}

		AllBlockTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllBlockTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllBlockTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		AllBlockTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(Registry.BLOCK_REGISTRY, id);
			} else {
				tag = TagKey.create(Registry.BLOCK_REGISTRY, id);
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

	public enum AllItemTags {

		SEETHABLE,
		SANDABLE,
		FREEZABLE,
		ADDITIONAL_DROPS_TOOL,
		MAGNET_IGNORE,
		FLAMMABLE_WOOD,
		CROSSBOW(FORGE, "tools/crossbow"),
		SWORD(FORGE, "tools/sword"),
		PICKAXE(FORGE, "tools/pickaxe"),
		DRILL(FORGE, "tools/drill"),
		AXE(FORGE, "tools/axe"),
		SAW(FORGE, "tools/saw"),
		SHOVEL(FORGE, "tools/shovel"),
		HOE(FORGE, "tools/hoe"),
		SCYTHE(FORGE, "tools/scythe"),


		IRON_PLATE(FORGE, "plates/iron")


		;

		public final TagKey<Item> tag;
		public final boolean alwaysDatagen;

		AllItemTags() {
			this(NameSpace.MOD);
		}

		AllItemTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllItemTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllItemTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		AllItemTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(Registry.ITEM_REGISTRY, id);
			} else {
				tag = TagKey.create(Registry.ITEM_REGISTRY, id);
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

	public enum AllFluidTags {

		FAN_PROCESSING_CATALYSTS_DRAGON_BREATHING("fan_processing_catalysts/dragon_breathing"),
		FAN_PROCESSING_CATALYSTS_SANDING("fan_processing_catalysts/sanding"),
		FAN_PROCESSING_CATALYSTS_FREEZING("fan_processing_catalysts/freezing"),
		FAN_PROCESSING_CATALYSTS_SEETHING("fan_processing_catalysts/seething"),
		INDUSTRIAL_FAN_HEATER,

		MILKSHAKES(FORGE),
		CHOCOLATE(true),
		VANILLA(true),
		STRAWBERRY(true),
		GLOWBERRY(true),
		PUMPKIN(true),
		SAP(FORGE)

		;

		public final TagKey<Fluid> tag;
		public final boolean alwaysDatagen;

		AllFluidTags() {
			this(NameSpace.MOD);
		}

		AllFluidTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllFluidTags(boolean milkshake) {
            this(FORGE, milkshake ? "milkshake" : null, FORGE.optionalDefault, FORGE.alwaysDatagenDefault);
        }

		AllFluidTags(String path) {
			this(NameSpace.MOD, path, NameSpace.MOD.optionalDefault, NameSpace.MOD.alwaysDatagenDefault);
		}

		AllFluidTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllFluidTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		AllFluidTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ?
					Lang.asId(name()) : Objects.equals(path, "milkshake") ?
					"milkshake/" + Lang.asId(name()) :
					path
			);
			if (optional) {
				tag = optionalTag(Registry.FLUID_REGISTRY, id);
			} else {
				tag = TagKey.create(Registry.FLUID_REGISTRY, id);
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
	
	public enum AllEntityTags {

		FAN_PROCESSING_IMMUNE_DRAGON_BREATHING(NameSpace.MOD, "fan_processing_immune/dragon_breathing"),

		;

		public final TagKey<EntityType<?>> tag;
		public final boolean alwaysDatagen;

		AllEntityTags() {
			this(NameSpace.MOD);
		}

		AllEntityTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllEntityTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllEntityTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		AllEntityTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(Registry.ENTITY_TYPE_REGISTRY, id);
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
				tag = optionalTag(Registry.RECIPE_SERIALIZER_REGISTRY, id);
			} else {
				tag = TagKey.create(Registry.RECIPE_SERIALIZER_REGISTRY, id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		public boolean matches(RecipeSerializer<?> recipeSerializer) {
			return Registry.RECIPE_SERIALIZER.getHolder(Registry.RECIPE_SERIALIZER.getId(recipeSerializer)).orElseThrow().is(tag);
		}

		private static void init() {}
	}

	public static void init() {
		AllBlockTags.init();
		AllItemTags.init();
		AllFluidTags.init();
		AllEntityTags.init();
		DesiresRecipeSerializerTags.init();
	}
}
