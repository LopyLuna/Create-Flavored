package uwu.lopyluna.create_dd.registry;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;
import uwu.lopyluna.create_dd.infrastructure.registrate_classes.FluidBuilder;
import uwu.lopyluna.create_dd.infrastructure.registrate_classes.FluidEntry;
import uwu.lopyluna.create_dd.infrastructure.registrate_classes.SimpleFlowableFluid;

import javax.annotation.Nullable;
import java.util.function.Supplier;

import static uwu.lopyluna.create_dd.DesireUtil.randomChance;
import static uwu.lopyluna.create_dd.Desires.REGISTRATE;
import static uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs.client;


public class DesiresFluids {
	// TODO: There are so many forge extensions in this class I'm going to cry
	
	public static final FluidEntry<SimpleFlowableFluid> CHOCOLATE_MILKSHAKE = newFluid("Chocolate Milkshake",
			0xB2614D, "c", DesiresTags.AllFluidTags.CHOCOLATE.tag)
			.register();
	public static final FluidEntry<SimpleFlowableFluid> VANILLA_MILKSHAKE = newFluid("Vanilla Milkshake",
			0xEDDABA, "v", DesiresTags.AllFluidTags.VANILLA.tag)
			.register();
	public static final FluidEntry<SimpleFlowableFluid> STRAWBERRY_MILKSHAKE = newFluid("Strawberry Milkshake",
			0xD57A8B, "s", DesiresTags.AllFluidTags.STRAWBERRY.tag)
			.register();
	public static final FluidEntry<SimpleFlowableFluid> GLOWBERRY_MILKSHAKE = newFluid("Glowberry Milkshake",
			0xD8A155, "g", DesiresTags.AllFluidTags.GLOWBERRY.tag)
			.register();
	public static final FluidEntry<SimpleFlowableFluid> PUMPKIN_MILKSHAKE = newFluid("Pumpkin Milkshake",
			0xCB7B38, "p", DesiresTags.AllFluidTags.PUMPKIN.tag)
			.register();


	public static final FluidEntry<SimpleFlowableFluid> SAP = newFluid("Sap",
			0xC87E50, "", DesiresTags.AllFluidTags.SAP.tag)
			.register();

	//CHROMATIC WASTE - 0x753458

	@SafeVarargs
    public static FluidBuilder<SimpleFlowableFluid, CreateRegistrate> newFluid(String name, int hexColor, String type, TagKey<Fluid>... tags) {
		String id = name.toLowerCase().replace(" ", "_");
		return REGISTRATE.standardFluid(id, SolidRenderedPlaceableFluidType.create(hexColor, () -> 1f / 4f * (type.equals("c") ?
						client().chocolateTransparencyMultiplier : type.equals("v") ?
						client().vanillaTransparencyMultiplier : type.equals("s") ?
						client().strawberryTransparencyMultiplier : type.equals("g") ?
						client().glowberryTransparencyMultiplier : type.equals("p") ?
						client().pumpkinTransparencyMultiplier :
						client().sapTransparencyMultiplier).getF()
				)).lang(name)
				.properties(b -> b.viscosity(1000)
						.density(1400))
				.fluidProperties(p -> p.levelDecreasePerBlock(2)
						.tickRate(10)
						.slopeFindDistance(3)
						.explosionResistance(100f))
				.tag(tags)
				.source(ForgeFlowingFluid.Source::new)
				.bucket()
				.tab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB)
				.tag(DesiresTags.forgeItemTag("buckets/" + id))
				.build();
	}

	// Load this class

	public static void register() {}

	public static void registerFluidInteractions() {

		addMilkshakeInteraction(CHOCOLATE_MILKSHAKE.get(), AllPaletteStoneTypes.VERIDIUM.getBaseBlock().get(), AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get());
		addMilkshakeInteraction(VANILLA_MILKSHAKE.get(), AllPaletteStoneTypes.ASURINE.getBaseBlock().get(), DesiresPaletteStoneTypes.DOLOMITE.getBaseBlock().get());
		addMilkshakeInteraction(STRAWBERRY_MILKSHAKE.get(), AllPaletteStoneTypes.CRIMSITE.getBaseBlock().get(), Blocks.COBBLED_DEEPSLATE);
		addMilkshakeInteraction(GLOWBERRY_MILKSHAKE.get(), AllPaletteStoneTypes.OCHRUM.getBaseBlock().get(), DesiresPaletteStoneTypes.GABBRO.getBaseBlock().get());
		addMilkshakeInteraction(PUMPKIN_MILKSHAKE.get(), DesiresPaletteStoneTypes.BRECCIA.getBaseBlock().get(), Blocks.DRIPSTONE_BLOCK);
	}

	@Nullable
	public static BlockState getLavaInteraction(FluidState fluidState, Level level, BlockPos pos) {
		if (addMilkshakeFlag(fluidState, CHOCOLATE_MILKSHAKE.get(), level, pos)) {
			return addMilkshakeStones(AllPaletteStoneTypes.VERIDIUM.getBaseBlock().get(), AllPaletteStoneTypes.SCORCHIA.getBaseBlock().get(), level, pos);
		}
		if (addMilkshakeFlag(fluidState, VANILLA_MILKSHAKE.get(), level, pos)) {
			return addMilkshakeStones(AllPaletteStoneTypes.ASURINE.getBaseBlock().get(), DesiresPaletteStoneTypes.DOLOMITE.getBaseBlock().get(), level, pos);
		}
		if (addMilkshakeFlag(fluidState, STRAWBERRY_MILKSHAKE.get(), level, pos)) {
			return addMilkshakeStones(AllPaletteStoneTypes.CRIMSITE.getBaseBlock().get(), Blocks.COBBLED_DEEPSLATE, level, pos);
		}
		if (addMilkshakeFlag(fluidState, GLOWBERRY_MILKSHAKE.get(), level, pos)) {
			return addMilkshakeStones(AllPaletteStoneTypes.OCHRUM.getBaseBlock().get(), DesiresPaletteStoneTypes.GABBRO.getBaseBlock().get(), level, pos);
		}
		if (addMilkshakeFlag(fluidState, PUMPKIN_MILKSHAKE.get(), level, pos)) {
			return addMilkshakeStones(DesiresPaletteStoneTypes.BRECCIA.getBaseBlock().get(), Blocks.DRIPSTONE_BLOCK, level, pos);
		}

		return null;
	}



	public static void addMilkshakeInteraction(Fluid fluid, Block stoneBedrock, Block stoneDefault) {
		FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
				(level, currentPos, relativePos, fluidState) ->
						level.getFluidState(relativePos).is(fluid) &&
								fluidState.isSource(),

				Blocks.OBSIDIAN.defaultBlockState()
		));
		FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
				(level, currentPos, relativePos, fluidState) ->
						 randomChance(DesiresConfigs.server().chanceForOreStone.get(), level) &&
								level.getBlockState(currentPos.below()).is(DesiresTags.AllBlockTags.ORE_GENERATOR.tag) &&
								level.getFluidState(relativePos).is(fluid) &&
								!fluidState.isSource(),

				stoneBedrock.defaultBlockState()
		));
		FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
				(level, currentPos, relativePos, fluidState) ->
						randomChance(DesiresConfigs.server().chanceForArtificialOreStone.get(), level) &&
								level.getBlockState(currentPos.below()).is(DesiresTags.AllBlockTags.ARTIFICIAL_ORE_GENERATOR.tag) &&
								level.getFluidState(relativePos).is(fluid) &&
								!fluidState.isSource(),

				stoneBedrock.defaultBlockState()
		));
		FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
				(level, currentPos, relativePos, fluidState) ->
						level.getFluidState(relativePos).is(fluid) &&
								!fluidState.isSource(),

				stoneDefault.defaultBlockState()
		));

	}

	public static boolean addMilkshakeFlag(FluidState fluidState, Fluid milkshake, Level level, BlockPos pos) {
		Fluid fluid = fluidState.getType();
		boolean isMilkshake = fluid.isSame(milkshake);
		boolean validMilkshakePoints = level.getFluidState(pos.relative(Direction.Axis.X, 1)).is(milkshake) || level.getFluidState(pos.relative(Direction.Axis.Y, 1)).is(milkshake) || level.getFluidState(pos.relative(Direction.Axis.Z, 1)).is(milkshake);
		boolean pointIsEmpty = level.getBlockState(pos).isAir() || level.getBlockState(pos).getMaterial().isReplaceable();
		boolean isValidMilkshake = validMilkshakePoints && pointIsEmpty && !(level.getBlockState(pos).getMaterial().isLiquid());
		return isMilkshake || isValidMilkshake;
	}

	public static BlockState addMilkshakeStones(Block stoneBedrock, Block stoneDefault, Level level, BlockPos pos) {
		if (level.getBlockState(pos.below()).is(DesiresTags.AllBlockTags.ORE_GENERATOR.tag) && randomChance(DesiresConfigs.server().chanceForOreStone.get(), level)) {
			return stoneBedrock.defaultBlockState();
		} else if (level.getBlockState(pos.below()).is(DesiresTags.AllBlockTags.ARTIFICIAL_ORE_GENERATOR.tag) && randomChance(DesiresConfigs.server().chanceForArtificialOreStone.get(), level)) {
			return stoneBedrock.defaultBlockState();
		} else {
			return stoneDefault.defaultBlockState();
		}
	}

	public static abstract class TintedFluidType extends FluidType {

		protected static final int NO_TINT = 0xffffffff;
		private final ResourceLocation stillTexture;
		private final ResourceLocation flowingTexture;

		public TintedFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
			super(properties);
			this.stillTexture = stillTexture;
			this.flowingTexture = flowingTexture;
		}

		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
			consumer.accept(new IClientFluidTypeExtensions() {

				@Override
				public ResourceLocation getStillTexture() {
					return stillTexture;
				}

				@Override
				public ResourceLocation getFlowingTexture() {
					return flowingTexture;
				}

				@Override
				public int getTintColor(FluidStack stack) {
					return TintedFluidType.this.getTintColor(stack);
				}

				@Override
				public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
					return TintedFluidType.this.getTintColor(state, getter, pos);
				}
				
				@Override
				public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
					int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
					Vector3f customFogColor = TintedFluidType.this.getCustomFogColor();
					return customFogColor == null ? fluidFogColor : customFogColor;
				}

				@Override
				public void modifyFogRender(Camera camera, FogMode mode, float renderDistance, float partialTick,
					float nearDistance, float farDistance, FogShape shape) {
					float modifier = TintedFluidType.this.getFogDistanceModifier();
					float baseWaterFog = 96.0f;
					if (modifier != 1f) {
						RenderSystem.setShaderFogShape(FogShape.CYLINDER);
						RenderSystem.setShaderFogStart(-8);
						RenderSystem.setShaderFogEnd(baseWaterFog * modifier);
					}
				}

			});
		}

		protected abstract int getTintColor(FluidStack stack);

		protected abstract int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos);
		
		protected Vector3f getCustomFogColor() {
			return null;
		}
		
		protected float getFogDistanceModifier() {
			return 1f;
		}

	}

	private static class SolidRenderedPlaceableFluidType extends TintedFluidType {

		private Vector3f fogColor;
		private Supplier<Float> fogDistance;

		public static FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
			return (p, s, f) -> {
				SolidRenderedPlaceableFluidType fluidType = new SolidRenderedPlaceableFluidType(p, s, f);
				fluidType.fogColor = new Color(fogColor, false).asVectorF();
				fluidType.fogDistance = fogDistance;
				return fluidType;
			};
		}

		private SolidRenderedPlaceableFluidType(Properties properties, ResourceLocation stillTexture,
			ResourceLocation flowingTexture) {
			super(properties, stillTexture, flowingTexture);
		}

		@Override
		protected int getTintColor(FluidStack stack) {
			return NO_TINT;
		}

		/*
		 * Removing alpha from tint prevents optifine from forcibly applying biome
		 * colors to modded fluids (this workaround only works for fluids in the solid
		 * render layer)
		 */
		@Override
		public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
			return 0x00ffffff;
		}
		
		@Override
		protected Vector3f getCustomFogColor() {
			return fogColor;
		}
		
		@Override
		protected float getFogDistanceModifier() {
			return fogDistance.get();
		}

	}

}
