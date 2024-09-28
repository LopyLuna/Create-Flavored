package uwu.lopyluna.create_dd.content.blocks.kinetics.brass_gearbox;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LightLayer;
import uwu.lopyluna.create_dd.registry.DesiresPartialModels;


public class BrassGearboxInstance extends KineticBlockEntityInstance<BrassGearboxBlockEntity> {

    ModelData top_panel;
    RotatingData top_shaft;
    ModelData bottom_panel;
    RotatingData bottom_shaft;
    ModelData north_panel;
    RotatingData north_shaft;
    ModelData east_panel;
    RotatingData east_shaft;
    ModelData south_panel;
    RotatingData south_shaft;
    ModelData west_panel;
    RotatingData west_shaft;

    public BrassGearboxInstance(MaterialManager materialManager, BrassGearboxBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = world.getBrightness(LightLayer.SKY, pos);

        if (!blockState.getValue(BrassGearboxBlock.UP_SHAFT)) {
            top_panel = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(DesiresPartialModels.TOP_BRASS_PANEL, blockState).createInstance();
            top_panel.setBlockLight(blockLight)
                    .setSkyLight(blockLight);
        }
        if (blockState.getValue(BrassGearboxBlock.UP_SHAFT)) {
            top_shaft = materialManager.defaultSolid().material(AllMaterialSpecs.ROTATING)
                    .getModel(AllPartialModels.SHAFT_HALF, blockState, Direction.UP).createInstance();
            top_shaft.setRotationAxis(Direction.get(Direction.AxisDirection.POSITIVE, axis).step())
                    .setRotationalSpeed(getSpeed(blockEntity))
                    .setRotationOffset(getRotationOffset(axis))
                    .setPosition(getInstancePosition())
                    .setBlockLight(blockLight)
                    .setSkyLight(skyLight);
        }
        if (!blockState.getValue(BrassGearboxBlock.DOWN_SHAFT)) {
            bottom_panel = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                    .getModel(DesiresPartialModels.BOTTOM_BRASS_PANEL, blockState).createInstance();
            bottom_panel.setBlockLight(blockLight)
                    .setSkyLight(blockLight);
        }
        if (blockState.getValue(BrassGearboxBlock.DOWN_SHAFT)) {
            bottom_shaft = materialManager.defaultSolid().material(AllMaterialSpecs.ROTATING)
                    .getModel(AllPartialModels.SHAFT_HALF, blockState, Direction.DOWN).createInstance();
            bottom_shaft.setRotationAxis(Direction.get(Direction.AxisDirection.POSITIVE, axis).step())
                    .setRotationalSpeed(getSpeed(blockEntity))
                    .setRotationOffset(getRotationOffset(axis))
                    .setPosition(getInstancePosition())
                    .setBlockLight(blockLight)
                    .setSkyLight(skyLight);
        }
        if (!blockState.getValue(BrassGearboxBlock.NORTH_SHAFT)) {
            north_panel = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                    .getModel(DesiresPartialModels.NORTH_BRASS_PANEL, blockState).createInstance();
            north_panel.setBlockLight(blockLight)
                    .setSkyLight(blockLight);
        }
        if (blockState.getValue(BrassGearboxBlock.NORTH_SHAFT)) {
            north_shaft = materialManager.defaultSolid().material(AllMaterialSpecs.ROTATING)
                    .getModel(AllPartialModels.SHAFT_HALF, blockState, Direction.NORTH).createInstance();
            north_shaft.setRotationAxis(Direction.get(Direction.AxisDirection.POSITIVE, axis).step())
                    .setRotationalSpeed(getSpeed(blockEntity))
                    .setRotationOffset(getRotationOffset(axis))
                    .setPosition(getInstancePosition())
                    .setBlockLight(blockLight)
                    .setSkyLight(skyLight);
        }
        if (!blockState.getValue(BrassGearboxBlock.EAST_SHAFT)) {
            east_panel = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                    .getModel(DesiresPartialModels.EAST_BRASS_PANEL, blockState).createInstance();
            east_panel.setBlockLight(blockLight)
                    .setSkyLight(blockLight);
        }
        if (blockState.getValue(BrassGearboxBlock.EAST_SHAFT)) {
            east_shaft = materialManager.defaultSolid().material(AllMaterialSpecs.ROTATING)
                    .getModel(AllPartialModels.SHAFT_HALF, blockState, Direction.EAST).createInstance();
            east_shaft.setRotationAxis(Direction.get(Direction.AxisDirection.POSITIVE, axis).step())
                    .setRotationalSpeed(getSpeed(blockEntity))
                    .setRotationOffset(getRotationOffset(axis))
                    .setPosition(getInstancePosition())
                    .setBlockLight(blockLight)
                    .setSkyLight(skyLight);
        }
        if (!blockState.getValue(BrassGearboxBlock.SOUTH_SHAFT)) {
            south_panel = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                    .getModel(DesiresPartialModels.SOUTH_BRASS_PANEL, blockState).createInstance();
            south_panel.setBlockLight(blockLight)
                    .setSkyLight(blockLight);
        }
        if (blockState.getValue(BrassGearboxBlock.SOUTH_SHAFT)) {
            south_shaft = materialManager.defaultSolid().material(AllMaterialSpecs.ROTATING)
                    .getModel(AllPartialModels.SHAFT_HALF, blockState, Direction.SOUTH).createInstance();
            south_shaft.setRotationAxis(Direction.get(Direction.AxisDirection.POSITIVE, axis).step())
                    .setRotationalSpeed(getSpeed(blockEntity))
                    .setRotationOffset(getRotationOffset(axis))
                    .setPosition(getInstancePosition())
                    .setBlockLight(blockLight)
                    .setSkyLight(skyLight);
        }
        if (!blockState.getValue(BrassGearboxBlock.WEST_SHAFT)) {
            west_panel = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                    .getModel(DesiresPartialModels.WEST_BRASS_PANEL, blockState).createInstance();
            west_panel.setBlockLight(blockLight)
                    .setSkyLight(blockLight);
        }
        if (blockState.getValue(BrassGearboxBlock.WEST_SHAFT)) {
            west_shaft = materialManager.defaultSolid().material(AllMaterialSpecs.ROTATING)
                    .getModel(AllPartialModels.SHAFT_HALF, blockState, Direction.WEST).createInstance();
            west_shaft.setRotationAxis(Direction.get(Direction.AxisDirection.POSITIVE, axis).step())
                    .setRotationalSpeed(getSpeed(blockEntity))
                    .setRotationOffset(getRotationOffset(axis))
                    .setPosition(getInstancePosition())
                    .setBlockLight(blockLight)
                    .setSkyLight(skyLight);
        }
    }



    private float getSpeed(BrassGearboxBlockEntity blockEntity) {
        return blockEntity.getSpeed();
    }

    @Override
    public void update() {
        updateRotation(top_shaft, axis, getSpeed(blockEntity));
        updateRotation(bottom_shaft, axis, getSpeed(blockEntity));
        updateRotation(north_shaft, axis, getSpeed(blockEntity));
        updateRotation(east_shaft, axis, getSpeed(blockEntity));
        updateRotation(south_shaft, axis, getSpeed(blockEntity));
        updateRotation(west_shaft, axis, getSpeed(blockEntity));
    }

    @Override
    public void updateLight() {
        relight(pos,
                top_panel,
                top_shaft,
                bottom_panel,
                bottom_shaft,
                north_panel,
                north_shaft,
                east_panel,
                east_shaft,
                south_panel,
                south_shaft,
                west_panel,
                west_shaft);
    }

    @Override
    public void remove() {
        top_panel.delete();
        top_shaft.delete();
        bottom_panel.delete();
        bottom_shaft.delete();
        north_panel.delete();
        north_shaft.delete();
        east_panel.delete();
        east_shaft.delete();
        south_panel.delete();
        south_shaft.delete();
        west_panel.delete();
        west_shaft.delete();
    }
}
