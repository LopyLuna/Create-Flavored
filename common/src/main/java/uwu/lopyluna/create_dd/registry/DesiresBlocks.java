package uwu.lopyluna.create_dd.registry;

import com.simibubi.create.*;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.drill.DrillMovementBehaviour;
import com.simibubi.create.content.kinetics.gauge.GaugeGenerator;
import com.simibubi.create.content.kinetics.motor.CreativeMotorGenerator;
import com.simibubi.create.content.logistics.chute.ChuteGenerator;
import com.simibubi.create.content.logistics.chute.ChuteItem;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.content.redstone.displayLink.source.KineticSpeedDisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.KineticStressDisplaySource;
import com.simibubi.create.foundation.block.ItemUseOverrides;
import com.simibubi.create.foundation.data.*;
import com.simibubi.create.foundation.utility.Couple;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.DesireUtil;
import uwu.lopyluna.create_dd.content.blocks.contraptions.bore_block.BoreBlock;
import uwu.lopyluna.create_dd.content.blocks.contraptions.bore_block.BoreBlockMovementBehaviour;
import uwu.lopyluna.create_dd.content.blocks.contraptions.contraption_block.HelmBlock;
import uwu.lopyluna.create_dd.content.blocks.functional.AxisKineticBlock;
import uwu.lopyluna.create_dd.content.blocks.kinetics.brass_gearbox.BrassGearboxBlock;
import uwu.lopyluna.create_dd.content.blocks.kinetics.cog_crank.CogCrankBlockItem;
import uwu.lopyluna.create_dd.content.blocks.kinetics.modular_drill.ModularDrillBlock;
import uwu.lopyluna.create_dd.content.blocks.kinetics.multimeter.MultiMeterBlock;
import uwu.lopyluna.create_dd.content.blocks.kinetics.giant_gear.GiantGearBlock;
import uwu.lopyluna.create_dd.content.blocks.kinetics.giant_gear.GiantGearBlockItem;
import uwu.lopyluna.create_dd.content.blocks.kinetics.giant_gear.GiantGearStructuralBlock;
import uwu.lopyluna.create_dd.content.blocks.kinetics.hydraulic_press.HydraulicPressBlock;
import uwu.lopyluna.create_dd.content.blocks.kinetics.industrial_fan_block.IndustrialFanBlock;
import uwu.lopyluna.create_dd.content.blocks.kinetics.cog_crank.CogCrankBlock;
import uwu.lopyluna.create_dd.content.blocks.functional.FanSailBlock;
import uwu.lopyluna.create_dd.content.blocks.kinetics.furnace_engine.FurnaceEngineBlock;
import uwu.lopyluna.create_dd.content.blocks.kinetics.furnace_engine.FurnaceEngineGenerator;
import uwu.lopyluna.create_dd.content.blocks.kinetics.furnace_engine.PoweredFlywheelBlock;
import uwu.lopyluna.create_dd.content.blocks.kinetics.kinetic_motor.KineticMotorBlock;
import uwu.lopyluna.create_dd.content.blocks.kinetics.redstone_divider.RedstoneDividerBlock;
import uwu.lopyluna.create_dd.content.blocks.kinetics.transmission.InverseBoxBlock;
import uwu.lopyluna.create_dd.content.blocks.kinetics.worm_gear.WormGearBlock;
import uwu.lopyluna.create_dd.content.blocks.logistics.burden_chute.BurdenChuteBlock;
import uwu.lopyluna.create_dd.content.blocks.logistics.fluid_reservoir.FluidReservoirBlock;
import uwu.lopyluna.create_dd.content.blocks.logistics.fluid_reservoir.FluidReservoirCTBehaviour;
import uwu.lopyluna.create_dd.content.blocks.logistics.fluid_reservoir.FluidReservoirGenerator;
import uwu.lopyluna.create_dd.content.blocks.logistics.fluid_reservoir.FluidReservoirItem;
import uwu.lopyluna.create_dd.content.blocks.logistics.item_stockpile.ItemStockpileBlock;
import uwu.lopyluna.create_dd.content.blocks.logistics.item_stockpile.ItemStockpileCTBehaviour;
import uwu.lopyluna.create_dd.content.blocks.logistics.item_stockpile.ItemStockpileItem;
import uwu.lopyluna.create_dd.infrastructure.utility.ForgeTags;

import java.util.function.Consumer;

import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours.assignDataBehaviour;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.*;
import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;
import static uwu.lopyluna.create_dd.Desires.REGISTRATE;
import static uwu.lopyluna.create_dd.registry.DesiresPaletteBlocks.rawRubberDecorTag;
import static uwu.lopyluna.create_dd.registry.DesiresPaletteBlocks.rubberDecorTag;
import static uwu.lopyluna.create_dd.registry.DesiresSoundEvents.CREATVEDITE;
import static uwu.lopyluna.create_dd.registry.DesiresSoundEvents.RUBBER;

@SuppressWarnings({"unused", "removal", "all"})
public class DesiresBlocks {

	public static final BlockEntry<Block> RAW_RUBBER_BLOCK = REGISTRATE.block("raw_rubber_block", Block::new)
			.properties(p -> p.color(MaterialColor.TERRACOTTA_WHITE))
			.properties(p -> p.sound(RUBBER))
			.properties(p -> p.strength(0.5f,1.5f))
			.lang("Block of Raw Rubber")
			.item()
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.tag(rawRubberDecorTag)
			.build()
			.register();

	public static final BlockEntry<Block> RUBBER_BLOCK = REGISTRATE.block("rubber_block", Block::new)
			.properties(p -> p.color(MaterialColor.TERRACOTTA_GRAY))
			.properties(p -> p.sound(RUBBER))
			.properties(p -> p.strength(0.5f,1.5f))
			.lang("Block of Rubber")
			.item()
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.tag(rubberDecorTag)
			.build()
			.register();

	public static final BlockEntry<CasingBlock> CREATIVE_CASING = REGISTRATE.block("creative_casing", CasingBlock::new)
			.transform(BuilderTransformers.casing(() -> DesiresSpriteShifts.CREATIVE_CASING))
			.properties(p -> p.color(MaterialColor.COLOR_BLACK)
					.requiresCorrectToolForDrops())
			.properties(p -> p.sound(CREATVEDITE))
			.transform(pickaxeOnly())
			.properties(p -> p.lightLevel($ -> 5))
			.lang("Creative Casing")
			.item()
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.properties(p -> p.rarity(Rarity.EPIC))
			.build()
			.register();

	public static final BlockEntry<CasingBlock> OVERBURDEN_CASING = REGISTRATE.block("overburden_casing", CasingBlock::new)
			.transform(BuilderTransformers.casing(() -> DesiresSpriteShifts.OVERBURDEN_CASING))
			.properties(p -> p.color(MaterialColor.COLOR_CYAN)
					.requiresCorrectToolForDrops()
					.sound(SoundType.NETHERITE_BLOCK))
			.transform(pickaxeOnly())
			.lang("Overburden Casing")
			.item()
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.build()
			.register();

	public static final BlockEntry<CasingBlock> HYDRAULIC_CASING = REGISTRATE.block("hydraulic_casing", CasingBlock::new)
			.transform(BuilderTransformers.casing(() -> DesiresSpriteShifts.HYDRAULIC_CASING))
			.properties(p -> p.color(MaterialColor.COLOR_ORANGE)
					.requiresCorrectToolForDrops()
					.sound(SoundType.COPPER))
			.transform(pickaxeOnly())
			.lang("Hydraulic Casing")
			.item()
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.build()
			.register();

	public static final BlockEntry<CasingBlock> INDUSTRIAL_CASING = REGISTRATE.block("industrial_casing", CasingBlock::new)
			.transform(BuilderTransformers.casing(() -> DesiresSpriteShifts.INDUSTRIAL_CASING))
			.properties(p -> p.color(MaterialColor.TERRACOTTA_CYAN)
					.requiresCorrectToolForDrops()
					.sound(SoundType.NETHERITE_BLOCK))
			.transform(pickaxeOnly())
			.lang("Industrial Casing")
			.item()
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.build()
			.register();

	public static final BlockEntry<IndustrialFanBlock> INDUSTRIAL_FAN = REGISTRATE.block("industrial_fan", IndustrialFanBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.noOcclusion()
					.color(MaterialColor.TERRACOTTA_CYAN)
					.requiresCorrectToolForDrops()
					.sound(SoundType.NETHERITE_BLOCK))
			.blockstate(BlockStateGen.directionalBlockProvider(true))
			.addLayer(() -> RenderType::cutoutMipped)
			.transform(pickaxeOnly())
			.transform(BlockStressDefaults.setImpact(4.0))
			.transform(BlockStressDefaults.setCapacity(16))
			.recipe((c, p) -> {
				ShapedRecipeBuilder.shaped(c.get(), 4)
						.pattern("CIP")
						.define('P', AllItems.PROPELLER.get())
						.define('C', AllBlocks.COGWHEEL.get())
						.define('I', INDUSTRIAL_CASING.get())
						.unlockedBy("has_casing", has(INDUSTRIAL_CASING.get()))
						.save(p, DesireUtil.asResource("crafting/" + c.getName()));
			})
			.lang("Industrial Fan")
			.item()
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.transform(customItemModel())
			.register();


	public static final BlockEntry<HydraulicPressBlock> HYDRAULIC_PRESS = REGISTRATE.block("hydraulic_press", HydraulicPressBlock::new)
			.initialProperties(SharedProperties::copperMetal)
			.properties(BlockBehaviour.Properties::noOcclusion)
			.properties(p -> p.noOcclusion().color(MaterialColor.TERRACOTTA_ORANGE))
			.transform(pickaxeOnly())
			.blockstate(BlockStateGen.horizontalBlockProvider(true))
			.transform(BlockStressDefaults.setImpact(64.0))
			.item(AssemblyOperatorBlockItem::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<BoreBlock> BORE_BLOCK = REGISTRATE.block("bore_block", BoreBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.color(MaterialColor.STONE))
			.properties(p -> p.sound(new SoundType(0.9f, 1.25f, SoundEvents.NETHERITE_BLOCK_BREAK,
					SoundEvents.NETHERITE_BLOCK_STEP, SoundEvents.NETHERITE_BLOCK_PLACE,
					SoundEvents.NETHERITE_BLOCK_HIT, SoundEvents.NETHERITE_BLOCK_FALL)))
			.onRegister(movementBehaviour(new BoreBlockMovementBehaviour()))
			.transform(pickaxeOnly())
			.recipe((c, p) -> {
				ShapedRecipeBuilder.shaped(c.get(), 4)
						.pattern("AIA")
						.pattern("ICI")
						.pattern("AIA")
						.define('A', AllItems.ANDESITE_ALLOY.get())
						.define('C', AllBlocks.ANDESITE_ALLOY_BLOCK.get())
						.define('I', Items.IRON_INGOT)
						.unlockedBy("has_" + c.getName(), has(c.get()))
						.save(p, DesireUtil.asResource("crafting/" + c.getName()));
			})
			.item()
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.build()
			.register();

	public static final BlockEntry<ModularDrillBlock> MODULAR_DRILL = REGISTRATE.block("modular_drill", ModularDrillBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.transform(axeOrPickaxe())
			.blockstate((c, p) -> {})
			//.blockstate(BlockStateGen.directionalBlockProvider(true))
			.transform(BlockStressDefaults.setImpact(8.0))
			.onRegister(movementBehaviour(new DrillMovementBehaviour()))
			.item()
			.tab(() -> DesiresCreativeModeTabs.BETA_CREATIVE_TAB)
			.model((c, p) -> {})
			.tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<MultiMeterBlock> MULTIMETER = REGISTRATE.block("multimeter", MultiMeterBlock::new)
			.initialProperties(SharedProperties::wooden)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.transform(axeOrPickaxe())
			.transform(BlockStressDefaults.setNoImpact())
			.blockstate(new GaugeGenerator()::generate)
			.onRegister(assignDataBehaviour(new KineticSpeedDisplaySource(), "kinetic_speed"))
			.onRegister(assignDataBehaviour(new KineticStressDisplaySource(), "kinetic_stress"))
			.recipe((c, p) -> ShapelessRecipeBuilder.shapeless(c.get(), 2)
					.requires(AllBlocks.STRESSOMETER.get())
					.requires(AllBlocks.SPEEDOMETER.get())
					.unlockedBy("has_" + getItemName(Items.COMPASS), has(Items.COMPASS))
					.save(p, DesireUtil.asResource("crafting/multimeter")))
			.item()
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.transform(ModelGen.customItemModel("gauge", "_", "item"))
			.register();

	public static final BlockEntry<BrassGearboxBlock> BRASS_GEARBOX = REGISTRATE.block("brass_gearbox", BrassGearboxBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.noOcclusion().color(MaterialColor.PODZOL))
			.transform(BlockStressDefaults.setNoImpact())
			.transform(axeOrPickaxe())
			.blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
			.item()
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.transform(customItemModel())
			.register();


	public static final BlockEntry<RedstoneDividerBlock> REDSTONE_DIVIDER = REGISTRATE.block("redstone_divider", RedstoneDividerBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.noOcclusion().color(MaterialColor.PODZOL))
			.addLayer(() -> RenderType::cutoutMipped)
			.transform(BlockStressDefaults.setNoImpact())
			.transform(axeOrPickaxe())
			.blockstate((c, p) -> BlockStateGen.axisBlock(c, p, s -> {
			int power = s.getValue(BlockStateProperties.POWER);
				return AssetLookup.partialBaseModel(c, p, "power_" + (
				power == 0 || power == 1 || power == 2 ? 0 :
				power == 3 || power == 4 || power == 5 ? 1 :
				power == 6 || power == 7 || power == 8 ? 2 :
				power == 9 || power == 10 || power == 11 ? 3 : 4));
			}))
			.recipe((c, p) -> ShapelessRecipeBuilder.shapeless(c.get(), 1)
					.requires(AllBlocks.ANDESITE_CASING.get())
					.requires(AllBlocks.LARGE_COGWHEEL.get())
					.requires(Items.REDSTONE)
					.unlockedBy("has_" + getItemName(AllBlocks.COGWHEEL.get()), has(AllBlocks.COGWHEEL.get()))
					.save(p, DesireUtil.asResource("crafting/kinetics/redstone_divider")))
			.item()
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<InverseBoxBlock> INVERSE_BOX = REGISTRATE.block("inverse_box", InverseBoxBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.noOcclusion().color(MaterialColor.PODZOL))
			.addLayer(() -> RenderType::cutoutMipped)
			.transform(BlockStressDefaults.setNoImpact())
			.transform(axeOrPickaxe())
			.blockstate(BlockStateGen.axisBlockProvider(true))
			.recipe((c, p) -> ShapelessRecipeBuilder.shapeless(c.get(), 1)
					.requires(AllBlocks.ANDESITE_CASING.get())
					.requires(AllBlocks.COGWHEEL.get())
					.unlockedBy("has_" + getItemName(AllBlocks.COGWHEEL.get()), has(AllBlocks.COGWHEEL.get()))
					.save(p, DesireUtil.asResource("crafting/kinetics/inverse_box")))
			.item()
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<KineticMotorBlock> KINETIC_MOTOR = REGISTRATE
			.block("kinetic_motor", KineticMotorBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.color(MaterialColor.COLOR_GRAY))
			.tag(AllTags.AllBlockTags.SAFE_NBT.tag)
			.transform(axeOrPickaxe())
			.recipe((c, p) -> ShapelessRecipeBuilder.shapeless(c.get(), 1)
					.requires(AllBlocks.ANDESITE_CASING.get())
					.requires(DesiresItems.KINETIC_MECHANISM.get())
					.unlockedBy("has_" + getItemName(DesiresItems.KINETIC_MECHANISM.get()), has(DesiresItems.KINETIC_MECHANISM.get()))
					.save(p, DesireUtil.asResource("crafting/kinetics/kinetic_motor")))
			.blockstate(new CreativeMotorGenerator()::generate)
			.transform(BlockStressDefaults.setCapacity(48))
			.transform(BlockStressDefaults.setGeneratorSpeed(() -> Couple.create(0, 32)))
			.item()
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<CogCrankBlock> COG_CRANK = REGISTRATE.block("cog_crank", CogCrankBlock::small)
			.initialProperties(SharedProperties::wooden)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.transform(axeOrPickaxe())
			.blockstate(BlockStateGen.axisBlockProvider(true))
			.transform(BlockStressDefaults.setCapacity(8.0))
			.transform(BlockStressDefaults.setGeneratorSpeed(CogCrankBlock::getSpeedRange))
			.tag(AllTags.AllBlockTags.BRITTLE.tag)
			.recipe((ctx, prov) -> ShapelessRecipeBuilder.shapeless(ctx.getEntry(), 1)
					.requires(AllBlocks.HAND_CRANK.get())
					.requires(AllBlocks.COGWHEEL.get())
					.unlockedBy("has_item", RegistrateRecipeProvider.has(ctx.get()))
					.save(prov, DesireUtil.asResource("crafting/kinetics/cog_crank")))
			.onRegister(ItemUseOverrides::addBlock)
			.item(CogCrankBlockItem::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.transform(customItemModel())
			.register();
	
	public static final BlockEntry<CogCrankBlock> LARGE_COG_CRANK = REGISTRATE.block("large_cog_crank", CogCrankBlock::large)
			.initialProperties(SharedProperties::wooden)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.transform(axeOrPickaxe())
		.blockstate(BlockStateGen.axisBlockProvider(true))
			.transform(BlockStressDefaults.setCapacity(8.0))
			.transform(BlockStressDefaults.setGeneratorSpeed(CogCrankBlock::getSpeedRange))
			.tag(AllTags.AllBlockTags.BRITTLE.tag)
			.recipe((c, p) -> {
				ShapelessRecipeBuilder.shapeless(c.getEntry(), 1)
						.requires(AllBlocks.HAND_CRANK.get())
						.requires(AllBlocks.LARGE_COGWHEEL.get())
						.unlockedBy("has_item", RegistrateRecipeProvider.has(c.get()))
						.save(p, DesireUtil.asResource("crafting/kinetics/large_cog_crank"));

				ShapelessRecipeBuilder.shapeless(c.getEntry(), 1)
						.requires(COG_CRANK.get())
						.requires(ItemTags.PLANKS)
						.unlockedBy("has_item", RegistrateRecipeProvider.has(c.get()))
						.save(p, DesireUtil.asResource("crafting/kinetics/cog_crank_to_large"));
			})
			.onRegister(ItemUseOverrides::addBlock)
			.item(CogCrankBlockItem::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<WormGearBlock> WORM_GEAR =
			REGISTRATE.block("worm_gear", WormGearBlock::new)
					.initialProperties(SharedProperties::stone)
					.properties(p -> p.color(MaterialColor.COLOR_LIGHT_GRAY))
					.transform(axeOrPickaxe())
					.blockstate((c, p) -> p.directionalBlock(c.get(), s -> {
						boolean isFlipped = s.getValue(WormGearBlock.FACING)
								.getAxisDirection() == Direction.AxisDirection.NEGATIVE;
						String partName = s.getValue(WormGearBlock.PART)
								.getSerializedName();
						String flipped = isFlipped ? "_flipped" : "";
						ModelFile existing = AssetLookup.partialBaseModel(c, p, partName);
						if (!isFlipped)
							return existing;
						return p.models().withExistingParent("block/worm_gear/block" + "_" + partName + flipped, existing.getLocation())
								.texture("2", p.modLoc("block/" + c.getName() + flipped));
					}))
					.transform(BlockStressDefaults.setNoImpact())
					.item()
					.tab(() -> DesiresCreativeModeTabs.BETA_CREATIVE_TAB)
					.transform(customItemModel("_", "block_single"))
					.register();

	public static final BlockEntry<FurnaceEngineBlock> FURNACE_ENGINE =
			REGISTRATE.block("furnace_engine", FurnaceEngineBlock::new)
					.initialProperties(SharedProperties::softMetal)
					.properties(p -> p.color(MaterialColor.TERRACOTTA_CYAN)
							.sound(SoundType.NETHERITE_BLOCK))
					.properties(BlockBehaviour.Properties::noOcclusion)
					.transform(pickaxeOnly())
					.tag(AllTags.AllBlockTags.BRITTLE.tag)
					.blockstate(new FurnaceEngineGenerator()::generate)
					.transform(BlockStressDefaults.setCapacity(256.0))
					.transform(BlockStressDefaults.setGeneratorSpeed(FurnaceEngineBlock::getSpeedRange))
					.item()
					.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
					.transform(ModelGen.customItemModel())
					.register();

	public static final BlockEntry<PoweredFlywheelBlock> POWERED_FLYWHEEL =
			REGISTRATE.block("powered_flywheel", PoweredFlywheelBlock::new)
					.initialProperties(SharedProperties::softMetal)
					.properties(p -> p.color(MaterialColor.METAL))
					.transform(pickaxeOnly())
					.blockstate(BlockStateGen.axisBlockProvider(false))
					.loot((lt, block) -> lt.dropOther(block, AllBlocks.FLYWHEEL.get()))
					.register();

	public static final BlockEntry<GiantGearBlock> GIANT_GEAR = REGISTRATE.block("giant_gear", GiantGearBlock::new)
			.initialProperties(SharedProperties::netheriteMetal)
			.properties(p -> p.noOcclusion().sound(SoundType.METAL).color(MaterialColor.COLOR_YELLOW))
			.transform(pickaxeOnly())
			.transform(BlockStressDefaults.setImpact(8.0))
			.blockstate(BlockStateGen.axisBlockProvider(true))
			.item(GiantGearBlockItem::new)
			.tab(() -> DesiresCreativeModeTabs.BETA_CREATIVE_TAB)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<GiantGearStructuralBlock> GIANT_GEAR_STRUCTURAL = REGISTRATE.block("giant_gear_structure", GiantGearStructuralBlock::new)
			.initialProperties(SharedProperties::netheriteMetal)
			.blockstate((c, p) -> p.getVariantBuilder(c.get())
					.forAllStatesExcept(BlockStateGen.mapToAir(p), GiantGearStructuralBlock.FACING))
			.properties(p -> p.noOcclusion().sound(SoundType.METAL).color(MaterialColor.COLOR_YELLOW))
			.transform(pickaxeOnly())
			.lang("Giant Gear")
			.register();

	public static final BlockEntry<HelmBlock> HELM = REGISTRATE.block("helm", HelmBlock::new)
			.initialProperties(SharedProperties::copperMetal)
			.properties(p -> p.noOcclusion().color(MaterialColor.COLOR_ORANGE))
			.blockstate(BlockStateGen.horizontalBlockProvider(true))
			.addLayer(() -> RenderType::cutoutMipped)
			.item()
			.tab(() -> DesiresCreativeModeTabs.BETA_CREATIVE_TAB)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<AxisKineticBlock> INCOMPLETE_WATER_WHEEL = REGISTRATE.block("incomplete_water_wheel", AxisKineticBlock::small)
			.initialProperties(SharedProperties::wooden)
			.properties(p -> p.noOcclusion().color(MaterialColor.DIRT))
			.transform(axeOrPickaxe())
			.blockstate(BlockStateGen.axisBlockProvider(true))
			.addLayer(() -> RenderType::cutoutMipped)
			.item()
			.tab(() -> DesiresCreativeModeTabs.BETA_CREATIVE_TAB)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<AxisKineticBlock> INCOMPLETE_LARGE_WATER_WHEEL = REGISTRATE.block("incomplete_large_water_wheel", AxisKineticBlock::large)
			.initialProperties(SharedProperties::wooden)
			.properties(p -> p.noOcclusion().color(MaterialColor.DIRT))
			.transform(axeOrPickaxe())
			.blockstate(BlockStateGen.axisBlockProvider(true))
			.addLayer(() -> RenderType::cutoutMipped)
			.item()
			.tab(() -> DesiresCreativeModeTabs.BETA_CREATIVE_TAB)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<BurdenChuteBlock> BURDEN_CHUTE = REGISTRATE.block("burden_chute", BurdenChuteBlock::new)
			.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.color(MaterialColor.COLOR_CYAN).sound(SoundType.NETHERITE_BLOCK))
			.transform(pickaxeOnly())
			.addLayer(() -> RenderType::cutoutMipped)
			.blockstate(new ChuteGenerator()::generate)
			.item(ChuteItem::new)
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.transform(customItemModel("_", "block"))
			.register();

	public static final BlockEntry<ItemStockpileBlock> ITEM_STOCKPILE = REGISTRATE.block("item_stockpile", ItemStockpileBlock::new)
			.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.color(MaterialColor.TERRACOTTA_BLUE)
					.sound(SoundType.NETHERITE_BLOCK)
					.explosionResistance(1200))
			.transform(pickaxeOnly())
			.blockstate((c, p) -> p.getVariantBuilder(c.get())
					.forAllStates(s -> ConfiguredModel.builder()
							.modelFile(AssetLookup.standardModel(c, p))
							.build()))
			.onRegister(connectedTextures(ItemStockpileCTBehaviour::new))
			.item(ItemStockpileItem::new)
			.recipe((c, p) -> {
				p.stonecutting(DataIngredient.items(AllBlocks.ITEM_VAULT), c, 1);
				p.stonecutting(DataIngredient.items(c), AllBlocks.ITEM_VAULT, 1);

				ShapedRecipeBuilder.shaped(c.get(), 1)
						.define('B', AllTags.forgeItemTag("plates/iron"))
						.define('C', ForgeTags.Items.BARRELS_WOODEN)
						.pattern("BCB")
						.unlockedBy("has_" + getItemName(Items.BARREL.asItem()), has(ForgeTags.Items.BARRELS_WOODEN))
						.save(p, DesireUtil.asResource("crafting/kinetics/" + c.getName()));
			})
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.build()
			.register();

	public static final BlockEntry<FluidReservoirBlock> FLUID_RESERVOIR = REGISTRATE.block("fluid_reservoir", FluidReservoirBlock::new)
			.initialProperties(SharedProperties::copperMetal)
			.properties(p -> p.noOcclusion().isRedstoneConductor((p1, p2, p3) -> true))
			.transform(pickaxeOnly())
			.blockstate(new FluidReservoirGenerator()::generate)
			.onRegister(connectedTextures(FluidReservoirCTBehaviour::new))
			.addLayer(() -> RenderType::cutoutMipped)
			.item(FluidReservoirItem::new)
			.transform(b -> b.model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/" + c.getName() + "_window"))))
			.recipe((c, p) -> {
				p.stonecutting(DataIngredient.items(AllBlocks.FLUID_TANK), c, 1);
				p.stonecutting(DataIngredient.items(c), AllBlocks.FLUID_TANK, 1);

				ShapedRecipeBuilder.shaped(c.get(), 1)
						.define('B', AllTags.forgeItemTag("plates/copper"))
						.define('C', ForgeTags.Items.BARRELS_WOODEN)
						.pattern("BCB")
						.unlockedBy("has_" + getItemName(Items.BARREL.asItem()), has(ForgeTags.Items.BARRELS_WOODEN))
						.save(p, DesireUtil.asResource("crafting/kinetics/" + c.getName()));
			})
			.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
			.build()
			.register();


	public static final BlockEntry<FanSailBlock> SPLASHING_SAIL =
			REGISTRATE.block("splashing_sail", FanSailBlock::sail)
					.initialProperties(SharedProperties::wooden)
					.properties(p -> p.color(MaterialColor.DIRT))
					.properties(p -> p.sound(SoundType.SCAFFOLDING)
							.noOcclusion())
					.transform(axeOnly())
					.blockstate(BlockStateGen.directionalBlockProvider(false))
					.tag(AllTags.AllBlockTags.WINDMILL_SAILS.tag)
					.tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
					.tag(AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SPLASHING.tag)
					.recipe((c, p) -> fanSailCrafting(c.get(), Items.WATER_BUCKET, p, c))
					.lang("Splashing Catalyst Sail")
					.item()
					.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
					.build()
					.register();

	public static final BlockEntry<FanSailBlock> HAUNTING_SAIL =
			REGISTRATE.block("haunting_sail", FanSailBlock::sail)
					.initialProperties(SharedProperties::wooden)
					.properties(p -> p.color(MaterialColor.DIRT))
					.properties(p -> p.sound(SoundType.SCAFFOLDING)
							.noOcclusion())
					.properties(p -> p.lightLevel(s -> 8))
					.transform(axeOnly())
					.blockstate(BlockStateGen.directionalBlockProvider(false))
					.tag(AllTags.AllBlockTags.WINDMILL_SAILS.tag)
					.tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
					.tag(AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_HAUNTING.tag)
					.recipe((c, p) -> fanSailCrafting(c.get(), Items.SOUL_CAMPFIRE, p, c))
					.lang("Haunting Catalyst Sail")
					.item()
					.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
					.build()
					.register();

	public static final BlockEntry<FanSailBlock> SMOKING_SAIL =
			REGISTRATE.block("smoking_sail", FanSailBlock::sail)
					.initialProperties(SharedProperties::wooden)
					.properties(p -> p.color(MaterialColor.DIRT))
					.properties(p -> p.sound(SoundType.SCAFFOLDING)
							.noOcclusion())
					.properties(p -> p.lightLevel(s -> 8))
					.transform(axeOnly())
					.blockstate(BlockStateGen.directionalBlockProvider(false))
					.tag(AllTags.AllBlockTags.WINDMILL_SAILS.tag)
					.tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
					.tag(AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SMOKING.tag)
					.recipe((c, p) -> fanSailCrafting(c.get(), Items.CAMPFIRE, p, c))
					.lang("Smoking Catalyst Sail")
					.item()
					.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
					.build()
					.register();

	public static final BlockEntry<FanSailBlock> BLASTING_SAIL =
			REGISTRATE.block("blasting_sail", FanSailBlock::sail)
					.initialProperties(SharedProperties::wooden)
					.properties(p -> p.color(MaterialColor.DIRT))
					.properties(p -> p.sound(SoundType.SCAFFOLDING)
							.noOcclusion())
					.properties(p -> p.lightLevel(s -> 12))
					.transform(axeOnly())
					.blockstate(BlockStateGen.directionalBlockProvider(false))
					.tag(AllTags.AllBlockTags.WINDMILL_SAILS.tag)
					.tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
					.tag(AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_BLASTING.tag)
					.recipe((c, p) -> fanSailCrafting(c.get(), Items.LAVA_BUCKET, p, c))
					.lang("Blasting Catalyst Sail")
					.item()
					.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
					.build()
					.register();

	public static final BlockEntry<FanSailBlock> SEETHING_SAIL =
			REGISTRATE.block("seething_sail", FanSailBlock::sail)
					.initialProperties(SharedProperties::wooden)
					.properties(p -> p.color(MaterialColor.DIRT))
					.properties(p -> p.sound(SoundType.SCAFFOLDING)
							.noOcclusion())
					.properties(p -> p.lightLevel(s -> 15))
					.transform(axeOnly())
					.blockstate(BlockStateGen.directionalBlockProvider(false))
					.tag(AllTags.AllBlockTags.WINDMILL_SAILS.tag)
					.tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
					.tag(DesiresTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SEETHING.tag)
					.recipe((c, p) ->
						ShapedRecipeBuilder.shaped(c.get(), 4)
								.pattern("SCS")
								.pattern("CRC")
								.pattern("SCS")
								.define('S', BLASTING_SAIL.get())
								.define('R', Items.NETHERITE_SCRAP)
								.define('C', DesiresItems.SEETHING_ABLAZE_ROD.get())
								.unlockedBy("has_" + getItemName(DesiresItems.SEETHING_ABLAZE_ROD.get()), has(DesiresItems.SEETHING_ABLAZE_ROD.get()))
								.save(p, DesireUtil.asResource("crafting/fan_catalyst/" + c.getName()))
					)
					.tag(DesiresTags.AllBlockTags.INDUSTRIAL_FAN_HEATER.tag)
					.lang("Seething Catalyst Sail")
					.item()
					.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
					.build()
					.register();

	public static final BlockEntry<FanSailBlock> FREEZING_SAIL =
			REGISTRATE.block("freezing_sail", FanSailBlock::sail)
					.initialProperties(SharedProperties::wooden)
					.properties(p -> p.color(MaterialColor.DIRT))
					.properties(p -> p.sound(SoundType.SCAFFOLDING)
							.noOcclusion())
					.transform(axeOnly())
					.blockstate(BlockStateGen.directionalBlockProvider(false))
					.tag(AllTags.AllBlockTags.WINDMILL_SAILS.tag)
					.tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
					.tag(DesiresTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_FREEZING.tag)
					.recipe((c, p) -> fanSailCrafting(c.get(), Items.POWDER_SNOW_BUCKET, p, c))
					.lang("Freezing Catalyst Sail")
					.item()
					.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
					.build()
					.register();

	public static final BlockEntry<FanSailBlock> SANDING_SAIL =
			REGISTRATE.block("sanding_sail", FanSailBlock::sail)
					.initialProperties(SharedProperties::wooden)
					.properties(p -> p.color(MaterialColor.DIRT))
					.properties(p -> p.sound(SoundType.SCAFFOLDING)
							.noOcclusion())
					.transform(axeOnly())
					.blockstate(BlockStateGen.directionalBlockProvider(false))
					.tag(AllTags.AllBlockTags.WINDMILL_SAILS.tag)
					.tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
					.tag(DesiresTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SANDING.tag)
					.recipe((c, p) -> fanSailCrafting(c.get(), Items.SAND, p, c))
					.lang("Sanding Catalyst Sail")
					.item()
					.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
					.build()
					.register();

	public static void fanSailCrafting(ItemLike itemLike, ItemLike cataylst, Consumer<FinishedRecipe> pFinishedRecipeConsumer, DataGenContext c) {
		ShapedRecipeBuilder.shaped(itemLike, 4)
				.pattern("SCS")
				.pattern("CRC")
				.pattern("SCS")
				.define('S', AllBlocks.SAIL_FRAME.get())
				.define('R', RUBBER_BLOCK.get())
				.define('C', cataylst)
				.unlockedBy("has_" + getItemName(cataylst), has(cataylst))
				.save(pFinishedRecipeConsumer, DesireUtil.asResource("crafting/fan_catalyst/" + c.getName()));
	}

	protected static String getItemName(ItemLike pItemLike) {
		return Registry.ITEM.getKey(pItemLike.asItem()).getPath();
	}

	// Load this class

	public static void register() {}

}
