package uwu.lopyluna.create_dd.content.data_tags;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;
import uwu.lopyluna.create_dd.registry.DesiresPaletteStoneTypes;
import uwu.lopyluna.create_dd.registry.DesiresTags;

import static uwu.lopyluna.create_dd.registry.DesiresTags.forgeBlockTag;
import static uwu.lopyluna.create_dd.registry.DesiresTags.forgeItemTag;

public class DesiresRegistrateTags {

	public static void addGenerators() {
		Desires.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, DesiresRegistrateTags::genBlockTags);
		Desires.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, DesiresRegistrateTags::genItemTags);
		Desires.REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, DesiresRegistrateTags::genEntityTags);
		//Desires.REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, DesiresRegistrateTags::genFluidTags);*
	}
	private static void genItemTags(RegistrateTagsProvider<Item> prov) {

		prov.tag(forgeItemTag("dusts/obsidian"))
				.add(AllItems.POWDERED_OBSIDIAN.get())
		;
		prov.tag(forgeItemTag("dusts"))
				.add(AllItems.POWDERED_OBSIDIAN.get())
		;
		prov.tag(DesiresTags.AllItemTags.SEETHABLE.tag)
				.add(Items.ENDER_PEARL)
				.add(Items.NETHERRACK)
				.add(Items.SLIME_BALL)
				.add(Items.OBSIDIAN)
				.add(Items.COBBLESTONE)
				.add(Items.CALCITE)
				.add(Items.COAL_BLOCK)
				.add(Items.DEEPSLATE_COAL_ORE)
				.add(Items.ANCIENT_DEBRIS)
				.add(AllItems.CRUSHED_COPPER.get())
				.add(AllItems.CRUSHED_ZINC.get())
				.add(AllItems.CRUSHED_GOLD.get())
				.add(AllItems.CRUSHED_IRON.get())
				.add(AllItems.CRUSHED_OSMIUM.get())
				.add(AllItems.CRUSHED_PLATINUM.get())
				.add(AllItems.CRUSHED_SILVER.get())
				.add(AllItems.CRUSHED_TIN.get())
				.add(AllItems.CRUSHED_LEAD.get())
				.add(AllItems.CRUSHED_QUICKSILVER.get())
				.add(AllItems.CRUSHED_BAUXITE.get())
				.add(AllItems.CRUSHED_URANIUM.get())
				.add(AllItems.CRUSHED_NICKEL.get())
		;
		prov.tag(DesiresTags.AllItemTags.FREEZABLE.tag)
				.add(AllItems.BLAZE_CAKE.get())
				.add(Items.ICE)
				.add(Items.PACKED_ICE)
				.add(Items.WATER_BUCKET)
				.add(Items.MAGMA_CREAM)
				.add(Items.SNOWBALL)
				.add(Items.SNOW)
				.add(Items.CRYING_OBSIDIAN)
		;
		prov.tag(DesiresTags.AllItemTags.SANDABLE.tag)
				.add(Items.EXPOSED_COPPER)
				.add(Items.WEATHERED_COPPER)
				.add(Items.OXIDIZED_COPPER)
				.add(Items.EXPOSED_CUT_COPPER)
				.add(Items.WEATHERED_CUT_COPPER)
				.add(Items.OXIDIZED_CUT_COPPER)
				.add(Items.EXPOSED_CUT_COPPER_SLAB)
				.add(Items.WEATHERED_CUT_COPPER_SLAB)
				.add(Items.OXIDIZED_CUT_COPPER_SLAB)
				.add(Items.EXPOSED_CUT_COPPER_STAIRS)
				.add(Items.WEATHERED_CUT_COPPER_STAIRS)
				.add(Items.OXIDIZED_CUT_COPPER_STAIRS)
				.add(Items.POLISHED_ANDESITE)
				.add(Items.POLISHED_ANDESITE_SLAB)
				.add(Items.POLISHED_ANDESITE_STAIRS)
				.add(Items.POLISHED_GRANITE)
				.add(Items.POLISHED_GRANITE_SLAB)
				.add(Items.POLISHED_GRANITE_STAIRS)
				.add(Items.POLISHED_DIORITE)
				.add(Items.POLISHED_DIORITE_SLAB)
				.add(Items.POLISHED_DIORITE_STAIRS)
				.add(Items.POLISHED_DEEPSLATE)
				.add(Items.POLISHED_DEEPSLATE_SLAB)
				.add(Items.POLISHED_DEEPSLATE_STAIRS)
				.add(Items.POLISHED_DEEPSLATE_WALL)
				.add(Items.POLISHED_BASALT)
				.add(Items.MUD)
				.add(Items.WARPED_NYLIUM)
				.add(Items.CRIMSON_NYLIUM)
				.add(Items.MAGMA_BLOCK)
				.add(AllItems.ROSE_QUARTZ.get())
		;

	}

	private static void genBlockTags(RegistrateTagsProvider<Block> prov) {

		prov.tag(DesiresTags.AllBlockTags.ORE_GENERATOR.tag)
				.add(Blocks.BEDROCK)
		;
		prov.tag(DesiresTags.AllBlockTags.ARTIFICIAL_ORE_GENERATOR.tag)
				.add(Blocks.NETHERITE_BLOCK)
				.add(Blocks.REINFORCED_DEEPSLATE)
				.add(Blocks.DRAGON_EGG)
				.add(Blocks.END_PORTAL)
				.add(Blocks.BEACON)
		;

		prov.tag(DesiresTags.AllBlockTags.BLOCK_ZAPPER_BLACKLIST.tag)
				.add(Blocks.BEDROCK)
				.add(Blocks.MOVING_PISTON)
				.add(Blocks.NETHER_PORTAL)
				.add(Blocks.END_PORTAL)
				.add(Blocks.END_PORTAL_FRAME)
				.add(Blocks.DRAGON_EGG)
				.add(Blocks.COMMAND_BLOCK)
				.add(Blocks.BARRIER)
				.add(Blocks.END_GATEWAY)
				.add(Blocks.REPEATING_COMMAND_BLOCK)
				.add(Blocks.CHAIN_COMMAND_BLOCK)
				.add(Blocks.STRUCTURE_BLOCK)
				.add(Blocks.JIGSAW)
				.add(Blocks.REINFORCED_DEEPSLATE)
				.add(Blocks.BEACON)
				.add(Blocks.NETHERITE_BLOCK)
		;

		prov.tag(DesiresTags.AllBlockTags.EXCAVATION_DRILL_VEIN_LARGE.tag)
				.addTag(forgeBlockTag("ores"))
		;
		prov.tag(DesiresTags.AllBlockTags.EXCAVATION_DRILL_VEIN_VALID.tag)
				.addTag(forgeBlockTag("ores"))
				.add(AllPaletteStoneTypes.ASURINE.baseBlock.get())
				.add(AllPaletteStoneTypes.CRIMSITE.baseBlock.get())
				.add(AllPaletteStoneTypes.OCHRUM.baseBlock.get())
				.add(AllPaletteStoneTypes.VERIDIUM.baseBlock.get())
				.add(AllPaletteStoneTypes.ASURINE.baseBlock.get())
				.add(DesiresPaletteStoneTypes.BRECCIA.baseBlock.get())
		;
		prov.tag(DesiresTags.AllBlockTags.MODULAR_VEIN.tag)
				.addTag(forgeBlockTag("ores"))
				.add(AllPaletteStoneTypes.ASURINE.baseBlock.get())
				.add(AllPaletteStoneTypes.CRIMSITE.baseBlock.get())
				.add(AllPaletteStoneTypes.OCHRUM.baseBlock.get())
				.add(AllPaletteStoneTypes.VERIDIUM.baseBlock.get())
				.add(AllPaletteStoneTypes.ASURINE.baseBlock.get())
				.add(AllPaletteStoneTypes.SCORCHIA.baseBlock.get())
				.add(AllPaletteStoneTypes.SCORIA.baseBlock.get())
				.add(AllPaletteStoneTypes.LIMESTONE.baseBlock.get())
				.add(DesiresPaletteStoneTypes.DOLOMITE.baseBlock.get())
				.add(DesiresPaletteStoneTypes.GABBRO.baseBlock.get())
				.add(DesiresPaletteStoneTypes.WEATHERED_LIMESTONE.baseBlock.get())
				.add(DesiresPaletteStoneTypes.BRECCIA.baseBlock.get())
				.addTag(BlockTags.BASE_STONE_OVERWORLD)
				.addTag(BlockTags.BASE_STONE_NETHER)
		;
		prov.tag(DesiresTags.AllBlockTags.MODULAR_VEIN_LARGE.tag)
				.addTag(forgeBlockTag("ores"))
		;
		prov.tag(DesiresTags.AllBlockTags.MODULAR_VEIN_MEDIUM.tag)
				.add(AllPaletteStoneTypes.ASURINE.baseBlock.get())
				.add(AllPaletteStoneTypes.CRIMSITE.baseBlock.get())
				.add(AllPaletteStoneTypes.OCHRUM.baseBlock.get())
				.add(AllPaletteStoneTypes.VERIDIUM.baseBlock.get())
				.add(AllPaletteStoneTypes.ASURINE.baseBlock.get())
				.add(DesiresPaletteStoneTypes.BRECCIA.baseBlock.get())
		;
		prov.tag(DesiresTags.AllBlockTags.MODULAR_VEIN_SMALL.tag)
				.add(AllPaletteStoneTypes.SCORCHIA.baseBlock.get())
				.add(AllPaletteStoneTypes.SCORIA.baseBlock.get())
				.add(AllPaletteStoneTypes.LIMESTONE.baseBlock.get())
				.add(DesiresPaletteStoneTypes.DOLOMITE.baseBlock.get())
				.add(DesiresPaletteStoneTypes.GABBRO.baseBlock.get())
				.add(DesiresPaletteStoneTypes.WEATHERED_LIMESTONE.baseBlock.get())
				.addTag(BlockTags.BASE_STONE_OVERWORLD)
				.addTag(BlockTags.BASE_STONE_NETHER)
		;

		prov.tag(DesiresTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_DRAGON_BREATHING.tag)
				.add(Blocks.DRAGON_WALL_HEAD);

		prov.tag(DesiresTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SANDING.tag)
				.add(Blocks.SAND)
				.add(Blocks.RED_SAND);

		prov.tag(DesiresTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_FREEZING.tag)
				.add(Blocks.POWDER_SNOW);

		prov.tag(DesiresTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SEETHING.tag)
				.add(AllBlocks.BLAZE_BURNER.get());

		prov.tag(DesiresTags.AllBlockTags.INDUSTRIAL_FAN_HEATER.tag)
				.add(Blocks.LAVA)
				.add(DesiresBlocks.SEETHING_SAIL.get())
				.add(AllBlocks.BLAZE_BURNER.get());

		prov.tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
				.addTag(DesiresTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SANDING.tag)
				.addTag(DesiresTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_FREEZING.tag)
				.addTag(DesiresTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SEETHING.tag);

		prov.tag(DesiresTags.AllBlockTags.INDUSTRIAL_FAN_TRANSPARENT.tag)
				.addTag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag);

	}


	private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> prov) {
		prov.tag(DesiresTags.AllEntityTags.FAN_PROCESSING_IMMUNE_DRAGON_BREATHING.tag)
				.add(EntityType.ENDERMITE)
				.add(EntityType.ENDERMAN)
				.add(EntityType.ENDER_DRAGON)
				.add(EntityType.ENDER_PEARL)
				.add(EntityType.EYE_OF_ENDER)
				.add(EntityType.END_CRYSTAL)
				.add(EntityType.WANDERING_TRADER)
				.add(EntityType.WITHER)
				.add(EntityType.WITHER_SKELETON)
				.add(EntityType.WITHER_SKULL)
				.add(EntityType.DRAGON_FIREBALL)
				.add(EntityType.SILVERFISH);

	}
}
