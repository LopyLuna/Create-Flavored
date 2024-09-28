package uwu.lopyluna.create_dd.content.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.content.recipes.SandingRecipe;

public class FanSandingCategory extends DProcessingViaFanCategory.MultiOutput<SandingRecipe> {

    public FanSandingCategory(Info<SandingRecipe> info) {
        super(info);
    }

    @Override
    protected void renderAttachedBlock(@NotNull IRecipeSlotsView iRecipeSlotsView, @NotNull PoseStack matrixStack, double mouseX, double mouseY) {
        GuiGameElement.of(Blocks.SAND.defaultBlockState())
                .scale(SCALE)
                .atLocal(0, 0, 2)
                .lighting(AnimatedKinetics.DEFAULT_LIGHTING)
                .render(matrixStack);
    }

}
