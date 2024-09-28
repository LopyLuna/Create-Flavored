package uwu.lopyluna.create_dd.infrastructure.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.config.ui.ConfigHelper;
import com.simibubi.create.foundation.config.ui.ConfigModListScreen;
import com.simibubi.create.foundation.config.ui.SubMenuConfigScreen;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.DelegatedStencilElement;
import com.simibubi.create.foundation.gui.element.TextStencilElement;
import com.simibubi.create.foundation.gui.widget.BoxWidget;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.lwjgl.glfw.GLFW;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.UnaryOperator;

import static uwu.lopyluna.create_dd.Desires.MOD_ID;

@SuppressWarnings("deprecation")
public class DesiresBaseConfigScreen extends DesiresConfigScreen {

    public static final DelegatedStencilElement.ElementRenderer DISABLED_RENDERER = (ms, width, height, alpha) -> UIRenderHelper.angledGradient(ms, 0, 0, height / 2, height, width, Theme.p(Theme.Key.BUTTON_DISABLE));
    private static final Map<String, UnaryOperator<DesiresBaseConfigScreen>> DEFAULTS = new HashMap<>();

    static {
        DEFAULTS.put(MOD_ID, (base) -> base
                .withTitles("Client Settings", "World Generation Settings", "Gameplay Settings")
                .withSpecs(DesiresConfigs.client().specification, DesiresConfigs.common().specification, DesiresConfigs.server().specification)
        );
    }

    public static DesiresBaseConfigScreen forDesires(Screen parent) {
        return new DesiresBaseConfigScreen(parent);
    }

    BoxWidget clientConfigWidget;
    BoxWidget commonConfigWidget;
    BoxWidget serverConfigWidget;
    BoxWidget goBack;
    BoxWidget others;
    BoxWidget title;

    ForgeConfigSpec clientSpec;
    ForgeConfigSpec commonSpec;
    ForgeConfigSpec serverSpec;
    String clientTitle = "Client Config";
    String commonTitle = "Common Config";
    String serverTitle = "Server Config";
    String modID;
    protected boolean returnOnClose;

    public DesiresBaseConfigScreen(Screen parent, @Nonnull String modID) {
        super(parent);
        this.modID = modID;

        if (DEFAULTS.containsKey(modID))
            DEFAULTS.get(modID).apply(this);
        else {
            this.searchForSpecsInModContainer();
        }
    }

    private DesiresBaseConfigScreen(Screen parent) {
        this(parent, MOD_ID);
    }

    public void searchForSpecsInModContainer() {
        if (!ConfigHelper.hasAnyForgeConfig(this.modID)){
            return;
        }

        try {
            clientSpec = ConfigHelper.findForgeConfigSpecFor(ModConfig.Type.CLIENT, this.modID);
        } catch (Exception e) {
            Desires.LOGGER.debug("Unable to find ClientConfigSpec for mod: {}", this.modID);
        }

        try {
            commonSpec = ConfigHelper.findForgeConfigSpecFor(ModConfig.Type.COMMON, this.modID);
        } catch (Exception e) {
            Desires.LOGGER.debug("Unable to find CommonConfigSpec for mod: {}", this.modID);
        }

        try {
            serverSpec = ConfigHelper.findForgeConfigSpecFor(ModConfig.Type.SERVER, this.modID);
        } catch (Exception e) {
            Desires.LOGGER.debug("Unable to find ServerConfigSpec for mod: {}", this.modID);
        }

    }

    public DesiresBaseConfigScreen withSpecs(@Nullable ForgeConfigSpec client, ForgeConfigSpec common, @Nullable ForgeConfigSpec server) {
        clientSpec = client;
        commonSpec = common;
        serverSpec = server;
        return this;
    }

    public DesiresBaseConfigScreen withTitles(@Nullable String client, String common, @Nullable String server) {
        if (client != null)
            clientTitle = client;

        if (common != null)
            commonTitle = common;

        if (server != null)
            serverTitle = server;

        return this;
    }

    @Override
    protected void init() {
        super.init();
        returnOnClose = true;

        TextStencilElement clientText = new TextStencilElement(font, Components.literal(clientTitle)).centered(true, true);
        addRenderableWidget(clientConfigWidget = new BoxWidget(width / 2 - 100, height / 2 - 15 - 30, 200, 16).showingElement(clientText));

        if (clientSpec != null) {
            clientConfigWidget.withCallback(() -> linkTo(new SubMenuConfigScreen(this, ModConfig.Type.CLIENT, clientSpec)));
            clientText.withElementRenderer(BoxWidget.gradientFactory.apply(clientConfigWidget));
        } else {
            clientConfigWidget.active = false;
            clientConfigWidget.updateColorsFromState();
            clientText.withElementRenderer(DISABLED_RENDERER);
        }

        TextStencilElement commonText = new TextStencilElement(font, Components.literal(commonTitle)).centered(true, true);
        addRenderableWidget(commonConfigWidget = new BoxWidget(width / 2 - 100, height / 2 - 15, 200, 16).showingElement(commonText));

        if (commonSpec != null) {
            commonConfigWidget.withCallback(() -> linkTo(new SubMenuConfigScreen(this, ModConfig.Type.COMMON, commonSpec)));
            commonText.withElementRenderer(BoxWidget.gradientFactory.apply(commonConfigWidget));
        } else {
            commonConfigWidget.active = false;
            commonConfigWidget.updateColorsFromState();
            commonText.withElementRenderer(DISABLED_RENDERER);
        }

        TextStencilElement serverText = new TextStencilElement(font, Components.literal(serverTitle)).centered(true, true);
        addRenderableWidget(serverConfigWidget = new BoxWidget(width / 2 - 100, height / 2 - 15 + 30, 200, 16).showingElement(serverText));

        if (serverSpec == null) {
            serverConfigWidget.active = false;
            serverConfigWidget.updateColorsFromState();
            serverText.withElementRenderer(DISABLED_RENDERER);
        } else {
            assert minecraft != null;
            if (minecraft.level == null) {
                serverText.withElementRenderer(DISABLED_RENDERER);
                serverConfigWidget.getToolTip()
                        .add(Components.literal("Stored individually per World"));
                serverConfigWidget.getToolTip()
                        .addAll(TooltipHelper.cutTextComponent(
                                Components.literal(
                                        "Gameplay settings can only be accessed from the in-game menu after joining a World or Server."),
                                TooltipHelper.Palette.ALL_GRAY));
            } else {
                serverConfigWidget.withCallback(() -> linkTo(new SubMenuConfigScreen(this, ModConfig.Type.SERVER, serverSpec)));
                serverText.withElementRenderer(BoxWidget.gradientFactory.apply(serverConfigWidget));
            }
        }

        TextStencilElement titleText = new TextStencilElement(font, modID.toUpperCase(Locale.ROOT))
                .centered(true, true)
                .withElementRenderer((ms, w, h, alpha) -> {
                    UIRenderHelper.angledGradient(ms, 0, 0, h / 2, h, w / 2, Theme.p(Theme.Key.CONFIG_TITLE_A));
                    UIRenderHelper.angledGradient(ms, 0, w / 2, h / 2, h, w / 2, Theme.p(Theme.Key.CONFIG_TITLE_B));
                });
        int boxWidth = width + 10;
        int boxHeight = 39;
        int boxPadding = 4;
        title = new BoxWidget(-5, height / 2 - 110, boxWidth, boxHeight)
                //.withCustomBackground(new Color(0x20_000000, true))
                .withBorderColors(Theme.p(Theme.Key.BUTTON_IDLE))
                .withPadding(0, boxPadding)
                .rescaleElement(boxWidth / 2f, (boxHeight - 2 * boxPadding) / 2f)//double the text size by telling it the element is only half as big as the available space
                .showingElement(titleText.at(0, 7));
        title.active = false;

        addRenderableWidget(title);


        DesiresConfigScreen.modID = this.modID;

        goBack = new BoxWidget(width / 2 - 134, height / 2, 20, 20).withPadding(2, 2)
                .withCallback(() -> linkTo(parent));
        goBack.showingElement(AllIcons.I_CONFIG_BACK.asStencil()
                .withElementRenderer(BoxWidget.gradientFactory.apply(goBack)));
        goBack.getToolTip()
                .add(Components.literal("Go Back"));
        addRenderableWidget(goBack);

        TextStencilElement othersText = new TextStencilElement(font, Components.literal("Access Configs of other Mods")).centered(true, true);
        others = new BoxWidget(width / 2 - 100, height / 2 - 15 + 90, 200, 16).showingElement(othersText);
        othersText.withElementRenderer(BoxWidget.gradientFactory.apply(others));
        others.withCallback(() -> linkTo(new ConfigModListScreen(this)));
        addRenderableWidget(others);
    }

    @Override
    protected void renderWindow(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        drawCenteredString(ms, font, "Access Configs for Mod:", width / 2, height / 2 - 105, Theme.i(Theme.Key.TEXT_ACCENT_STRONG));
    }

    private void linkTo(Screen screen) {
        returnOnClose = false;
        ScreenOpener.open(screen);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers))
            return true;
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            linkTo(parent);
        }
        return false;
    }

}
