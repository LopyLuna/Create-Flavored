package uwu.lopyluna.create_dd.infrastructure.registrate_classes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.util.entry.LazyRegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class AbstractBuilder<R, T extends R, P, S extends AbstractBuilder<R, T, P, S>> implements Builder<R, T, P, S> {
    private final AbstractRegistrate<?> owner;
    private final P parent;
    private final String name;
    private final BuilderCallback callback;
    private final ResourceKey<Registry<R>> registryKey;
    private final Multimap<ProviderType<? extends RegistrateTagsProvider<?>>, TagKey<?>> tagsByType = HashMultimap.create();
    private final LazyRegistryEntry<T> safeSupplier = new LazyRegistryEntry(this);

    protected abstract @NonnullType T createEntry();

    public RegistryEntry<T> register() {
        return this.callback.accept(this.name, this.registryKey, this, this::createEntry, this::createEntryWrapper);
    }

    protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
        return new RegistryEntry(this.getOwner(), delegate);
    }

    public NonNullSupplier<T> asSupplier() {
        return this.safeSupplier;
    }

    @SafeVarargs
    public final S tag(ProviderType<? extends RegistrateTagsProvider<R>> type, TagKey<R>... tags) {
        if (!this.tagsByType.containsKey(type)) {
            this.setData(type, (ctx, prov) -> {
                Stream<TagKey<R>> var10000 = this.tagsByType.get(type).stream().map(t -> (TagKey<R>) t);
                Objects.requireNonNull(prov);
                var10000.map(prov::tag).forEach((b) -> {
                    b.add(this.asSupplier().get());
                });
            });
        }

        this.tagsByType.putAll(type, Arrays.asList(tags));
        return (S) this;
    }

    @SafeVarargs
    public final S removeTag(ProviderType<RegistrateTagsProvider<R>> type, TagKey<R>... tags) {
        if (this.tagsByType.containsKey(type)) {
            TagKey[] var3 = tags;
            int var4 = tags.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                TagKey<R> tag = var3[var5];
                this.tagsByType.remove(type, tag);
            }
        }

        return (S) this;
    }

    public S lang(NonNullFunction<T, String> langKeyProvider) {
        return this.lang(langKeyProvider, (p, t) -> {
            return p.getAutomaticName(t, this.getRegistryKey());
        });
    }

    public S lang(NonNullFunction<T, String> langKeyProvider, String name) {
        return this.lang(langKeyProvider, (p, s) -> {
            return name;
        });
    }

    private S lang(NonNullFunction<T, String> langKeyProvider, NonNullBiFunction<RegistrateLangProvider, NonNullSupplier<? extends T>, String> localizedNameProvider) {
        return this.setData(ProviderType.LANG, (ctx, prov) -> {
            String var10001 = langKeyProvider.apply(ctx.getEntry());
            Objects.requireNonNull(ctx);
            prov.add(var10001, localizedNameProvider.apply(prov, ctx::getEntry));
        });
    }

    public AbstractBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceKey<Registry<R>> registryKey) {
        this.owner = owner;
        this.parent = parent;
        this.name = name;
        this.callback = callback;
        this.registryKey = registryKey;
    }

    public AbstractRegistrate<?> getOwner() {
        return this.owner;
    }

    public P getParent() {
        return this.parent;
    }

    public String getName() {
        return this.name;
    }

    protected BuilderCallback getCallback() {
        return this.callback;
    }

    public ResourceKey<Registry<R>> getRegistryKey() {
        return this.registryKey;
    }
}
