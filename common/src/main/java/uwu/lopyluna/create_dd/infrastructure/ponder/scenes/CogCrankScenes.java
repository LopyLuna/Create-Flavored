package uwu.lopyluna.create_dd.infrastructure.ponder.scenes;

import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.utility.Pointing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class CogCrankScenes {
    
    public static void cogCrank(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("cog_crank", "Generating Rotational Force using Cog Cranks");
        scene.configureBasePlate(0, 0, 5);
        scene.world.showSection(util.select.layer(0), Direction.UP);
        scene.idle(5);
        
        BlockPos gaugePos = util.grid.at(1, 3, 3);
        BlockPos handlePos = util.grid.at(2, 2, 2);
        Selection handleSelect = util.select.position(handlePos);
        
        scene.world.showSection(util.select.layersFrom(1)
                .substract(handleSelect), Direction.DOWN);
        scene.idle(10);
        scene.world.showSection(handleSelect, Direction.DOWN);
        scene.idle(20);
        
        Vec3 centerOf = util.vector.centerOf(handlePos);
        Vec3 sideOf = centerOf.add(-0.5, 0, 0);
        
        scene.overlay.showText(70)
                .text("Cog Cranks can be used by players to apply rotational force manually")
                .placeNearTarget()
                .pointAt(sideOf);
        scene.idle(80);
        
        scene.overlay.showControls(new InputWindowElement(centerOf, Pointing.DOWN).rightClick(), 40);
        scene.idle(7);
        scene.world.setKineticSpeed(util.select.everywhere(), 32);
        scene.world.modifyKineticSpeed(util.select.column(1, 3), f -> f * -2);
        scene.effects.rotationDirectionIndicator(handlePos);
        scene.effects.indicateSuccess(gaugePos);
        scene.idle(10);
        scene.overlay.showText(50)
                .text("Hold Right-Click to rotate it Counter-Clockwise")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(sideOf);
        
        scene.idle(35);
        scene.world.setKineticSpeed(util.select.everywhere(), 0);
        scene.idle(15);
        
        scene.overlay.showControls(new InputWindowElement(centerOf, Pointing.DOWN).rightClick()
                .whileSneaking(), 40);
        scene.idle(7);
        scene.world.setKineticSpeed(util.select.everywhere(), -32);
        scene.world.modifyKineticSpeed(util.select.column(1, 3), f -> f * -2);
        scene.effects.rotationDirectionIndicator(handlePos);
        scene.effects.indicateSuccess(gaugePos);
        scene.idle(10);
        scene.overlay.showText(90)
                .text("Sneak and Hold Right-Click to rotate it Clockwise")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(sideOf);
        
        scene.idle(35);
        scene.world.setKineticSpeed(util.select.everywhere(), 0);
        scene.idle(45);
    }
    
}
