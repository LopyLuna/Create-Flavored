package uwu.lopyluna.create_dd.content.entities.seething_ablaze;

import net.minecraft.client.renderer.entity.BlazeRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Blaze;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.Desires;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SeethingBlazeRenderer extends BlazeRenderer {

    private static final ResourceLocation SEETHING_BLAZE_LOCATION =
            new ResourceLocation(Desires.MOD_ID, "textures/entity/seething_blaze.png");

    public SeethingBlazeRenderer(EntityRendererProvider.Context p_173933_) {
        super(p_173933_);
    }


    @Override
    protected int getSkyLightLevel(Blaze pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(Blaze pEntity) {
        return SEETHING_BLAZE_LOCATION;
    }
}
