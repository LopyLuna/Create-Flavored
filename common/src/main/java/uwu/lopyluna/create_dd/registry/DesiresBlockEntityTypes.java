package uwu.lopyluna.create_dd.registry;

import com.simibubi.create.content.kinetics.base.*;
import com.simibubi.create.content.kinetics.transmission.SplitShaftInstance;
import com.simibubi.create.content.kinetics.transmission.SplitShaftRenderer;
import com.simibubi.create.content.logistics.chute.ChuteRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import uwu.lopyluna.create_dd.content.blocks.contraptions.contraption_block.HelmBlockEntity;
import uwu.lopyluna.create_dd.content.blocks.kinetics.brass_gearbox.BrassGearboxBlockEntity;
import uwu.lopyluna.create_dd.content.blocks.kinetics.brass_gearbox.BrassGearboxInstance;
import uwu.lopyluna.create_dd.content.blocks.kinetics.brass_gearbox.BrassGearboxRenderer;
import uwu.lopyluna.create_dd.content.blocks.kinetics.modular_drill.ModularDrillBlockEntity;
import uwu.lopyluna.create_dd.content.blocks.kinetics.modular_drill.ModularDrillInstance;
import uwu.lopyluna.create_dd.content.blocks.kinetics.modular_drill.ModularDrillRenderer;
import uwu.lopyluna.create_dd.content.blocks.kinetics.multimeter.MultiMeterBlockEntity;
import uwu.lopyluna.create_dd.content.blocks.kinetics.giant_gear.GiantGearBlockEntity;
import uwu.lopyluna.create_dd.content.blocks.kinetics.hydraulic_press.HydraulicPressBlockEntity;
import uwu.lopyluna.create_dd.content.blocks.kinetics.hydraulic_press.HydraulicPressInstance;
import uwu.lopyluna.create_dd.content.blocks.kinetics.hydraulic_press.HydraulicPressRenderer;
import uwu.lopyluna.create_dd.content.blocks.kinetics.industrial_fan_block.IndustrialFanBlockEntity;
import uwu.lopyluna.create_dd.content.blocks.kinetics.industrial_fan_block.IndustrialFanInstance;
import uwu.lopyluna.create_dd.content.blocks.kinetics.industrial_fan_block.IndustrialFanRemderer;
import uwu.lopyluna.create_dd.content.blocks.kinetics.cog_crank.CogCrankBlockEntity;
import uwu.lopyluna.create_dd.content.blocks.kinetics.cog_crank.CogCrankInstance;
import uwu.lopyluna.create_dd.content.blocks.kinetics.cog_crank.CogCrankRenderer;
import uwu.lopyluna.create_dd.content.blocks.kinetics.furnace_engine.*;
import uwu.lopyluna.create_dd.content.blocks.kinetics.kinetic_motor.KineticMotorBlockEntity;
import uwu.lopyluna.create_dd.content.blocks.kinetics.kinetic_motor.KineticMotorRenderer;
import uwu.lopyluna.create_dd.content.blocks.kinetics.redstone_divider.RedstoneDividerBlockEntity;
import uwu.lopyluna.create_dd.content.blocks.kinetics.transmission.InverseBoxBlockEntity;
import uwu.lopyluna.create_dd.content.blocks.kinetics.worm_gear.WormGearBlockEntity;
import uwu.lopyluna.create_dd.content.blocks.logistics.burden_chute.BurdenChuteBlockEntity;
import uwu.lopyluna.create_dd.content.blocks.logistics.fluid_reservoir.FluidReservoirBlockEntity;
import uwu.lopyluna.create_dd.content.blocks.logistics.fluid_reservoir.FluidReservoirRenderer;
import uwu.lopyluna.create_dd.content.blocks.logistics.item_stockpile.ItemStockpileBlockEntity;

import static uwu.lopyluna.create_dd.Desires.REGISTRATE;

public class DesiresBlockEntityTypes {

	public static final BlockEntityEntry<IndustrialFanBlockEntity> INDUSTRIAL_FAN = REGISTRATE
			.blockEntity("industrial_fan", IndustrialFanBlockEntity::new)
			.instance(() -> IndustrialFanInstance::new, false)
			.validBlocks(DesiresBlocks.INDUSTRIAL_FAN)
			.renderer(() -> IndustrialFanRemderer::new)
			.register();

	public static final BlockEntityEntry<HydraulicPressBlockEntity> HYDRAULIC_PRESS = REGISTRATE
			.blockEntity("hydraulic_press", HydraulicPressBlockEntity::new)
			.instance(() -> HydraulicPressInstance::new)
			.validBlocks(DesiresBlocks.HYDRAULIC_PRESS)
			.renderer(() -> HydraulicPressRenderer::new)
			.register();

	public static final BlockEntityEntry<CogCrankBlockEntity> COG_CRANK = REGISTRATE
			.blockEntity("cog_crank", CogCrankBlockEntity::new)
			.instance(() -> CogCrankInstance::new)
			.validBlocks(DesiresBlocks.COG_CRANK, DesiresBlocks.LARGE_COG_CRANK)
			.renderer(() -> CogCrankRenderer::new)
			.register();
	
	public static final BlockEntityEntry<ItemStockpileBlockEntity> ITEM_STOCKPILE = REGISTRATE
			.blockEntity("item_stockpile", ItemStockpileBlockEntity::new)
			.validBlocks(DesiresBlocks.ITEM_STOCKPILE)
			.register();

	public static final BlockEntityEntry<FluidReservoirBlockEntity> FLUID_RESERVOIR = REGISTRATE
			.blockEntity("fluid_reservoir", FluidReservoirBlockEntity::new)
			.validBlocks(DesiresBlocks.FLUID_RESERVOIR)
			.renderer(() -> FluidReservoirRenderer::new)
			.register();

	public static final BlockEntityEntry<FurnaceEngineBlockEntity> FURNACE_ENGINE = REGISTRATE
			.blockEntity("furnace_engine", FurnaceEngineBlockEntity::new)
			.instance(()->FurnaceEngineInstance::new)
			.validBlocks(DesiresBlocks.FURNACE_ENGINE)
			.renderer(()-> FurnaceEngineRenderer::new)
			.register();

	public static final BlockEntityEntry<PoweredFlywheelBlockEntity> POWERED_FLYWHEEL = REGISTRATE
			.blockEntity("powered_flywheel", PoweredFlywheelBlockEntity::new)
			.instance(() -> PoweredFlywheelInstance::new, false)
			.validBlocks(DesiresBlocks.POWERED_FLYWHEEL)
			.renderer(() -> PoweredFlywheelRenderer::new)
			.register();

	public static final BlockEntityEntry<InverseBoxBlockEntity> INVERSE_BOX = REGISTRATE
			.blockEntity("inverse_box", InverseBoxBlockEntity::new)
			.instance(() -> SplitShaftInstance::new, false)
			.validBlocks(DesiresBlocks.INVERSE_BOX)
			.renderer(() -> SplitShaftRenderer::new)
			.register();

	public static final BlockEntityEntry<KineticMotorBlockEntity> KINETIC_MOTOR = REGISTRATE
			.blockEntity("motor", KineticMotorBlockEntity::new)
			.instance(() -> HalfShaftInstance::new, false)
			.validBlocks(DesiresBlocks.KINETIC_MOTOR)
			.renderer(() -> KineticMotorRenderer::new).register();


	public static final BlockEntityEntry<GiantGearBlockEntity> GIANT_GEAR = REGISTRATE
			.blockEntity("giant_gear", GiantGearBlockEntity::new)
			.instance(() -> CutoutRotatingInstance::new, false)
			.validBlocks(DesiresBlocks.GIANT_GEAR)
			.renderer(() -> KineticBlockEntityRenderer::new)
			.register();

	public static final BlockEntityEntry<KineticBlockEntity> AXIS_BLOCK = REGISTRATE
			.blockEntity("axis_block", KineticBlockEntity::new)
			.instance(() -> CutoutRotatingInstance::new, false)
			.validBlocks(DesiresBlocks.INCOMPLETE_WATER_WHEEL, DesiresBlocks.INCOMPLETE_LARGE_WATER_WHEEL)
			.renderer(() -> KineticBlockEntityRenderer::new)
			.register();

	public static final BlockEntityEntry<MultiMeterBlockEntity> MULTIMETER = REGISTRATE
			.blockEntity("multimeter", MultiMeterBlockEntity::new)
			.instance(() -> ShaftInstance::new)
			.validBlocks(DesiresBlocks.MULTIMETER)
			.renderer(() -> ShaftRenderer::new)
			.register();

	public static final BlockEntityEntry<RedstoneDividerBlockEntity> REDSTONE_DIVIDER = REGISTRATE
			.blockEntity("redstone_divider", RedstoneDividerBlockEntity::new)
			.instance(() -> SplitShaftInstance::new, false)
			.validBlocks(DesiresBlocks.REDSTONE_DIVIDER)
			.renderer(() -> SplitShaftRenderer::new)
			.register();

	public static final BlockEntityEntry<BrassGearboxBlockEntity> BRASS_GEARBOX = REGISTRATE
			.blockEntity("brass_gearbox", BrassGearboxBlockEntity::new)
			.instance(() -> BrassGearboxInstance::new, false)
			.validBlocks(DesiresBlocks.BRASS_GEARBOX)
			.renderer(() -> BrassGearboxRenderer::new)
			.register();

	public static final BlockEntityEntry<WormGearBlockEntity> WORM_GEAR = REGISTRATE
			.blockEntity("worm_gear", WormGearBlockEntity::new)
			.instance(() -> SingleRotatingInstance::new, false)
			.validBlocks(DesiresBlocks.WORM_GEAR)
			.renderer(() -> KineticBlockEntityRenderer::new)
			.register();

	public static final BlockEntityEntry<HelmBlockEntity> HELM = REGISTRATE
			.blockEntity("helm", HelmBlockEntity::new)
			.validBlocks(DesiresBlocks.HELM)
			.register();

	public static final BlockEntityEntry<ModularDrillBlockEntity> DRILL = REGISTRATE
			.blockEntity("drill", ModularDrillBlockEntity::new)
			.instance(() -> ModularDrillInstance::new, false)
			.validBlocks(DesiresBlocks.MODULAR_DRILL)
			.renderer(() -> ModularDrillRenderer::new)
			.register();

	public static final BlockEntityEntry<BurdenChuteBlockEntity> BURDEN_CHUTE = REGISTRATE
			.blockEntity("burden_chute", BurdenChuteBlockEntity::new)
			.validBlocks(DesiresBlocks.BURDEN_CHUTE)
			.renderer(() -> ChuteRenderer::new)
			.register();

	public static void register() {}
}
