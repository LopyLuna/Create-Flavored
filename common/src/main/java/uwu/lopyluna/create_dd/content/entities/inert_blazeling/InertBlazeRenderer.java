package uwu.lopyluna.create_dd.content.entities.inert_blazeling;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.Desires;

import javax.annotation.ParametersAreNonnullByDefault;

@Environment(EnvType.CLIENT)
@ParametersAreNonnullByDefault
public class InertBlazeRenderer extends MobRenderer<InertBlaze, InertBlazeModel<InertBlaze>> {

    private static final ResourceLocation INERT_BLAZELING_LOCATION =
            new ResourceLocation(Desires.MOD_ID, "textures/entity/inert_blazeling.png");

    public InertBlazeRenderer(EntityRendererProvider.Context p_173933_) {
        super(p_173933_, new InertBlazeModel<>(p_173933_.bakeLayer(InertBlazeModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    protected int getSkyLightLevel(InertBlaze pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    protected int getBlockLightLevel(InertBlaze pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(InertBlaze pEntity) {
        return INERT_BLAZELING_LOCATION;
    }
}
