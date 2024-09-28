package uwu.lopyluna.create_dd.registry.addons;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import uwu.lopyluna.create_dd.registry.helper.Lang;

import java.util.Optional;
import java.util.function.Supplier;

import dev.architectury.platform.Platform;

public enum DreamsAddons {
    EXTRAS,
    MAGIC,
    ENDGAME;

    private final String id;

    DreamsAddons() {
        id = Lang.asId(name());
    }

    public String id() {
        return id;
    }

    public ResourceLocation rl(String path) {
        return new ResourceLocation(id, path);
    }

    public Block getBlock(String id) {
        return Registry.BLOCK.get(rl(id));
    }

    public boolean isLoaded() {
        return Platform.isModLoaded(id);
    }

    public <T> Optional<T> runIfInstalled(Supplier<Supplier<T>> toRun) {
        if (isLoaded())
            return Optional.of(toRun.get().get());
        return Optional.empty();
    }

    public void executeIfInstalled(Supplier<Runnable> toExecute) {
        if (isLoaded()) {
            toExecute.get().run();
        }
    }
}
