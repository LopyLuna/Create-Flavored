package uwu.lopyluna.create_dd.content.blocks.contraptions.contraption_block;

import com.simibubi.create.content.contraptions.render.ContraptionEntityRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import uwu.lopyluna.create_dd.registry.DesiresContraptionType;

public class BoatContraptionRenderer extends ContraptionEntityRenderer<BoatContraptionEntity> {
    public BoatContraptionRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(BoatContraptionEntity entity, Frustum clippingHelper, double cameraX, double cameraY, double cameraZ) {
        if (!super.shouldRender(entity, clippingHelper, cameraX, cameraY, cameraZ))
            return false;
        return entity.getContraption()
                .getType() != DesiresContraptionType.BOAT || entity.getVehicle() != null;
    }


}
