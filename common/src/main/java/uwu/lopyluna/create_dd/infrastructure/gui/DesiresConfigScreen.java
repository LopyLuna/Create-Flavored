package uwu.lopyluna.create_dd.infrastructure.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.config.ui.ConfigScreen;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.element.StencilElement;
import com.simibubi.create.foundation.utility.animation.Force;
import com.simibubi.create.foundation.utility.animation.PhysicalFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.util.TriConsumer;
import org.lwjgl.opengl.GL30;
import uwu.lopyluna.create_dd.content.blocks.kinetics.giant_gear.GiantGearBlock;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;

import java.util.HashMap;
import java.util.Map;

public class DesiresConfigScreen extends ConfigScreen {

    /*
     *
     * TODO
     *
     * fix any issues that may come from Create's Config Screen
     *
     */

    public static final Map<String, TriConsumer<Screen, PoseStack, Float>> backgrounds = new HashMap<>();
    public static final PhysicalFloat cogSpin = PhysicalFloat.create().withLimit(10f).withDrag(0.3).addForce(new Force.Static(.2f));
    public static final BlockState cogwheelState = DesiresBlocks.LARGE_COG_CRANK.getDefaultState().setValue(GiantGearBlock.AXIS, Direction.Axis.Y);
    public static String modID = null;
    protected final Screen parent;

    public DesiresConfigScreen(Screen parent) {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void tick() {
        super.tick();
        cogSpin.tick();
    }

    @Override
    protected void renderWindowBackground(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        if (this.minecraft != null && this.minecraft.level != null) {
            //in game
            fill(ms, 0, 0, this.width, this.height, 0xb0_282c34);
        } else {
            //in menus
            renderMenuBackground(ms, partialTicks);
        }

        new StencilElement() {
            @Override
            protected void renderStencil(PoseStack ms) {
                renderCog(ms, partialTicks);
            }

            @Override
            protected void renderElement(PoseStack ms) {
                fill(ms, -200, -200, 200, 200, 0x60_000000);
            }
        }.at(width * 0.5f, height * 0.5f, 0).render(ms);

        super.renderWindowBackground(ms, mouseX, mouseY, partialTicks);

    }

    @Override
    protected void prepareFrame() {
        assert minecraft != null;
        UIRenderHelper.swapAndBlitColor(minecraft.getMainRenderTarget(), UIRenderHelper.framebuffer);
        RenderSystem.clear(GL30.GL_STENCIL_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);
    }

    @Override
    protected void endFrame() {
        assert minecraft != null;
        UIRenderHelper.swapAndBlitColor(UIRenderHelper.framebuffer, minecraft.getMainRenderTarget());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        cogSpin.bump(3, -delta * 5);

        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    /**
     * By default ConfigScreens will render the Create Panorama as
     * their background when opened from the Main- or ModList-Menu.
     * If your addon wants to render something else, please add to the
     * backgrounds Map in this Class with your modID as the key.
     */
    @Override
    protected void renderMenuBackground(PoseStack ms, float partialTicks) {
        TriConsumer<Screen, PoseStack, Float> customBackground = backgrounds.get(modID);
        if (customBackground != null) {
            customBackground.accept(this, ms, partialTicks);
            return;
        }

        assert minecraft != null;
        float elapsedPartials = minecraft.getDeltaFrameTime();
        DesiresMainMenuScreen.PANORAMA.render(elapsedPartials, 1);

        RenderSystem.setShaderTexture(0, DesiresMainMenuScreen.PANORAMA_OVERLAY_TEXTURES);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        blit(ms, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);

        fill(ms, 0, 0, this.width, this.height, 0x90_282c34);
    }

    @Override
    protected void renderCog(PoseStack ms, float partialTicks) {
        ms.pushPose();

        ms.translate(-100, 100, -100);
        ms.scale(200, 200, 1);
        GuiGameElement.of(cogwheelState)
                .rotateBlock(22.5, cogSpin.getValue(partialTicks), 22.5)
                .render(ms);

        ms.popPose();
    }
}