package uwu.lopyluna.create_dd.registry;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.utility.Color;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

import static uwu.lopyluna.create_dd.Desires.MOD_ID;

public enum DesiresGuiTextures implements ScreenElement {
    LOGO("logo", 256, 256),

    BLOCK_ZAPPER("curiosities", 234, 103),
    BLOCK_ZAPPER_INACTIVE_PARAM("curiosities", 238, 0, 18, 18),
    ;

    public final ResourceLocation location;
    public final int width, height;
    public final int startX, startY;

    DesiresGuiTextures(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    @SuppressWarnings("unused")
    DesiresGuiTextures(int startX, int startY) {
        this("icons", startX * 16, startY * 16, 16, 16);
    }

    DesiresGuiTextures(String location, int startX, int startY, int width, int height) {
        this(MOD_ID, location, startX, startY, width, height);
    }

    DesiresGuiTextures(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(namespace, "textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    @Environment(EnvType.CLIENT)
    public void bind() {
        RenderSystem.setShaderTexture(0, location);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void render(PoseStack ms, int x, int y) {
        bind();
        GuiComponent.blit(ms, x, y, 0, startX, startY, width, height, 256, 256);
    }

    @Environment(EnvType.CLIENT)
    public void render(PoseStack ms, int x, int y, GuiComponent component) {
        bind();
        component.blit(ms, x, y, startX, startY, width, height);
    }

    @Environment(EnvType.CLIENT)
    public void render(PoseStack ms, int x, int y, Color c) {
        bind();
        UIRenderHelper.drawColoredTexture(ms, c, x, y, startX, startY, width, height);
    }

}
