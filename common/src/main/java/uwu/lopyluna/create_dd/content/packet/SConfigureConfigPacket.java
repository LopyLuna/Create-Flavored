package uwu.lopyluna.create_dd.content.packet;

import com.mojang.logging.LogUtils;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.config.ui.ConfigHelper;
import com.simibubi.create.foundation.config.ui.SubMenuConfigScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.utility.Components;

import dev.architectury.networking.NetworkManager.PacketContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import uwu.lopyluna.create_dd.infrastructure.gui.DesiresBaseConfigScreen;
import uwu.lopyluna.create_dd.infrastructure.ponder.PacketBase;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SConfigureConfigPacket extends PacketBase {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final String option;
    private final String value;

    public SConfigureConfigPacket(FriendlyByteBuf buffer) {
        this.option = buffer.readUtf(32767);
        this.value = buffer.readUtf(32767);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(option);
        buffer.writeUtf(value);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean handle(PacketContext context) {
        context.queue(() -> {
            if (option.startsWith("SET")) {
                trySetConfig(option.substring(3), value);
                return;
            }

            try {
                Actions.valueOf(option)
                        .performAction(value);
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Received ConfigureConfigPacket with invalid Option: " + option);
            }
        });

        return true;
    }

    private static void trySetConfig(String option, String value) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null)
            return;

        ConfigHelper.ConfigPath configPath;
        try {
            configPath = ConfigHelper.ConfigPath.parse(option);
        } catch (IllegalArgumentException e) {
            player.displayClientMessage(Components.literal(e.getMessage()), false);
            return;
        }

        if (configPath.getType() != ModConfig.Type.CLIENT) {
            Create.LOGGER.warn("Received type-mismatched config packet on client");
            return;
        }

        try {
            ConfigHelper.setConfigValue(configPath, value);
            player.displayClientMessage(Components.literal("Great Success!"), false);
        } catch (ConfigHelper.InvalidValueException e) {
            player.displayClientMessage(Components.literal("Config could not be set the the specified value!"), false);
        } catch (Exception e) {
            player.displayClientMessage(Components.literal("Something went wrong while trying to set config value. Check the client logs for more information"), false);
            Create.LOGGER.warn("Exception during client-side config value set:", e);
        }

    }

    public enum Actions {
        configScreen(() -> Actions::configScreen);

        private final Supplier<Consumer<String>> consumer;

        Actions(Supplier<Consumer<String>> action) {
            this.consumer = action;
        }

        void performAction(String value) {
            consumer.get()
                    .accept(value);
        }

        @Environment(EnvType.CLIENT)
        private static void configScreen(String value) {
            if (value.isEmpty()) {
                ScreenOpener.open(DesiresBaseConfigScreen.forDesires(null));
                return;
            }

            LocalPlayer player = Minecraft.getInstance().player;
            ConfigHelper.ConfigPath configPath;
            assert player != null;
            try {
                configPath = ConfigHelper.ConfigPath.parse(value);
            } catch (IllegalArgumentException e) {
                player.displayClientMessage(Components.literal(e.getMessage()), false);
                return;
            }

            try {
                ScreenOpener.open(SubMenuConfigScreen.find(configPath));
            } catch (Exception e) {
                player.displayClientMessage(Components.literal("Unable to find the specified config"), false);
            }
        }
    }
}
