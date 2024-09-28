package uwu.lopyluna.create_dd.infrastructure.registrate_classes;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.fabric.EnvExecutor;
import com.tterrag.registrate.fabric.FluidHelper;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluidBuilder<T extends SimpleFlowableFluid, P> extends AbstractBuilder<Fluid, T, P, FluidBuilder<T, P>> {

    /**
     * Create a new {@link FluidBuilder} and configure data. The created builder will use a default fluid class ({@link SimpleFlowableFluid.Flowing}).
     *
     * @param <P>
     *            Parent object type
     * @param owner
     *            The owning {@link AbstractRegistrate} object
     * @param parent
     *            The parent object
     * @param name
     *            Name of the entry being built
     * @param callback
     *            A callback used to actually register the built entry
     * @param stillTexture
     *            The texture to use for still fluids
     * @param flowingTexture
     *            The texture to use for flowing fluids
     * @return A new {@link FluidBuilder} with reasonable default data generators.
     * @see #create(AbstractRegistrate, Object, String, BuilderCallback, ResourceLocation, ResourceLocation, NonNullFunction)
     */
    public static <P> FluidBuilder<SimpleFlowableFluid.Flowing, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
        return create(owner, parent, name, callback, stillTexture, flowingTexture, SimpleFlowableFluid.Flowing::new);
    }

    /**
     * Create a new {@link FluidBuilder} and configure data. Used in lieu of adding side-effects to constructor, so that alternate initialization strategies can be done in subclasses.
     * <p>
     * The fluid will be assigned the following data:
     * <ul>
     * <li>The default translation (via {@link #defaultLang()})</li>
     * <li>A default {@link SimpleFlowableFluid.Source source fluid} (via {@link #defaultSource})</li>
     * <li>A default block for the fluid, with its own default blockstate and model that configure the particle texture (via {@link #defaultBlock()})</li>
     * <li>A default bucket item, that uses a simple generated item model with a texture of the same name as this fluid (via {@link #defaultBucket()})</li>
     * </ul>
     *
     * @param <T>
     *            The type of the builder
     * @param <P>
     *            Parent object type
     * @param owner
     *            The owning {@link AbstractRegistrate} object
     * @param parent
     *            The parent object
     * @param name
     *            Name of the entry being built
     * @param callback
     *            A callback used to actually register the built entry
     * @param stillTexture
     *            The texture to use for still fluids
     * @param flowingTexture
     *            The texture to use for flowing fluids
     * @param fluidFactory
     *            A factory that creates the flowing fluid
     * @return A new {@link FluidBuilder} with reasonable default data generators.
     */
    public static <T extends SimpleFlowableFluid, P> FluidBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture,
        NonNullFunction<SimpleFlowableFluid.Properties, T> fluidFactory) {
        FluidBuilder<T, P> ret = new FluidBuilder<>(owner, parent, name, callback, stillTexture, flowingTexture, fluidFactory)
            .defaultLang().defaultSource().defaultBlock().defaultBucket();
        return ret;
    }

    private final String sourceName, bucketName;

    private final ResourceLocation stillTexture, flowingTexture;
    private final NonNullFunction<SimpleFlowableFluid.Properties, T> fluidFactory;

    @Nullable // has sane defaults
    private NonNullSupplier<FluidVariantAttributeHandler> attributeHandler = null;

    @Nullable
    private Boolean defaultSource, defaultBlock, defaultBucket;

    private NonNullConsumer<SimpleFlowableFluid.Properties> fluidProperties;

    private @Nullable Supplier<Supplier<RenderType>> layer = null;

    @Nullable
    private NonNullSupplier<? extends SimpleFlowableFluid> source;
    private final List<TagKey<Fluid>> tags = new ArrayList<>();

    public FluidBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction<SimpleFlowableFluid.Properties, T> fluidFactory) {
        super(owner, parent, "flowing_" + name, callback, Registry.FLUID_REGISTRY);
        this.sourceName = name;
        this.bucketName = name + "_bucket";
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.fluidFactory = fluidFactory;

        String bucketName = this.bucketName;
        this.fluidProperties = p -> p.bucket(() -> owner.get(bucketName, Registry.ITEM_REGISTRY).get())
                .block(() -> owner.<Block, LiquidBlock>get(name, Registry.BLOCK_REGISTRY).get());
    }

    /**
     * Register a {@link FluidVariantAttributeHandler} for this fluid.
     *
     * @param handler a supplier for the handler that will be registered for this fluid
     * @return this {@link FluidBuilder}
     */
    public FluidBuilder<T, P> fluidAttributes(NonNullSupplier<FluidVariantAttributeHandler> handler) {
        if (attributeHandler == null) {
            this.attributeHandler = handler;
        }
        return this;
    }

    /**
     * Modify the properties of the flowing fluid. Modifications are done lazily, but the passed function is composed with the current one, and as such this method can be called multiple times to perform
     * different operations.
     *
     * @param cons
     *            The action to perform on the attributes
     * @return this {@link FluidBuilder}
     */
    public FluidBuilder<T, P> fluidProperties(NonNullConsumer<SimpleFlowableFluid.Properties> cons) {
        fluidProperties = fluidProperties.andThen(cons);
        return this;
    }

    /**
     * Assign the default translation, as specified by {@link RegistrateLangProvider#toEnglishName(String)}. This is the default, so it is generally not necessary to call, unless for
     * undoing previous changes.
     *
     * @return this {@link FluidBuilder}
     */
    public FluidBuilder<T, P> defaultLang() {
        return lang(RegistrateLangProvider.toEnglishName(sourceName));
    }

    /**
     * Set the translation for this fluid.
     *
     * @param name
     *            A localized English name
     * @return this {@link FluidBuilder}
     */
    public FluidBuilder<T, P> lang(String name) {
        return lang(flowing -> FluidHelper.getDescriptionId(flowing.getSource()), name);
    }

    public FluidBuilder<T, P> renderType(Supplier<Supplier<RenderType>> layer) {
        if (this.layer == null) {
            onRegister(this::registerRenderType);
        }
        this.layer = layer;
        return this;
    }

    protected void registerRenderType(T entry) {
        EnvExecutor.runWhenOn(EnvType.CLIENT, () -> () -> BlockRenderLayerMap.INSTANCE.putFluids(layer.get().get(), entry, getSource()));
    }

    /**
     * Create a standard {@link SimpleFlowableFluid.Source} for this fluid which will be built and registered along with this fluid.
     *
     * @return this {@link FluidBuilder}
     * @see #source(NonNullFunction)
     * @throws IllegalStateException
     *             If {@link #source(NonNullFunction)} has been called before this method
     */
    public FluidBuilder<T, P> defaultSource() {
        if (this.defaultSource != null) {
            throw new IllegalStateException("Cannot set a default source after a custom source has been created");
        }
        this.defaultSource = true;
        return this;
    }

    /**
     * Create a {@link SimpleFlowableFluid} for this fluid, which is created by the given factory, and which will be built and registered along with this fluid.
     *
     * @param factory
     *            A factory for the fluid, which accepts the properties and returns a new fluid
     * @return this {@link FluidBuilder}
     */
    public FluidBuilder<T, P> source(NonNullFunction<SimpleFlowableFluid.Properties, ? extends SimpleFlowableFluid> factory) {
        this.defaultSource = false;
        this.source = NonNullSupplier.lazy(() -> factory.apply(makeProperties()));
        return this;
    }

    /**
     * Create a standard {@link LiquidBlock} for this fluid, building it immediately, and not allowing for further configuration.
     *
     * @return this {@link FluidBuilder}
     * @see #block()
     * @throws IllegalStateException
     *             If {@link #block()} or {@link #block(NonNullBiFunction)} has been called before this method
     */
    public FluidBuilder<T, P> defaultBlock() {
        if (this.defaultBlock != null) {
            throw new IllegalStateException("Cannot set a default block after a custom block has been created");
        }
        this.defaultBlock = true;
        return this;
    }

    /**
     * Create a standard {@link LiquidBlock} for this fluid, and return the builder for it so that further customization can be done.
     *
     * @return the {@link BlockBuilder} for the {@link LiquidBlock}
     */
    public BlockBuilder<LiquidBlock, FluidBuilder<T, P>> block() {
        return block1(LiquidBlock::new);
    }

    /**
     * Create a {@link LiquidBlock} for this fluid, which is created by the given factory, and return the builder for it so that further customization can be done.
     *
     * @param <B>
     *            The type of the block
     * @param factory
     *            A factory for the block, which accepts the block object and properties and returns a new block
     * @return the {@link BlockBuilder} for the {@link LiquidBlock}
     */
    public <B extends LiquidBlock> BlockBuilder<B, FluidBuilder<T, P>> block(NonNullBiFunction<NonNullSupplier<? extends T>, BlockBehaviour.Properties, ? extends B> factory) {
        if (this.defaultBlock == Boolean.FALSE) {
            throw new IllegalStateException("Only one call to block/noBlock per builder allowed");
        }
        this.defaultBlock = false;
        NonNullSupplier<T> supplier = asSupplier();
        return getOwner().<B, FluidBuilder<T, P>>block(this, sourceName, p -> factory.apply(supplier, p))
            .properties(p -> BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable())
                // fabric: luminance is fluid-sensitive, can't do this easily.
                // default impl will try to get it from the fluid's block, thus causing a loop.
                // if you want to do this, override getLuminance in FluidVariantAttributeHandler
                //.properties(p -> p.lightLevel(blockState -> fluidType.get().getLightLevel()))
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getBuilder(sourceName)
                .texture("particle", stillTexture)));
    }

    @SuppressWarnings("unchecked")
    public <B extends LiquidBlock> BlockBuilder<B, FluidBuilder<T, P>> block1(NonNullBiFunction<? extends T, BlockBehaviour.Properties, ? extends B> factory) {
        return block((supplier, settings) -> ((NonNullBiFunction<T, BlockBehaviour.Properties, ? extends B>) factory).apply(supplier.get(), settings));
    }

    @Beta
    public FluidBuilder<T, P> noBlock() {
        if (this.defaultBlock == Boolean.FALSE) {
            throw new IllegalStateException("Only one call to block/noBlock per builder allowed");
        }
        this.defaultBlock = false;
        return this;
    }

    /**
     * Create a standard {@link BucketItem} for this fluid, building it immediately, and not allowing for further configuration.
     *
     * @return this {@link FluidBuilder}
     * @see #bucket()
     * @throws IllegalStateException
     *             If {@link #bucket()} or {@link #bucket(NonNullBiFunction)} has been called before this method
     */
    public FluidBuilder<T, P> defaultBucket() {
        if (this.defaultBucket != null) {
            throw new IllegalStateException("Cannot set a default bucket after a custom bucket has been created");
        }
        defaultBucket = true;
        return this;
    }

    /**
     * Create a standard {@link BucketItem} for this fluid, and return the builder for it so that further customization can be done.
     *
     * @return the {@link ItemBuilder} for the {@link BucketItem}
     */
    public ItemBuilder<BucketItem, FluidBuilder<T, P>> bucket() {
        return bucket(BucketItem::new);
    }

    /**
     * Create a {@link BucketItem} for this fluid, which is created by the given factory, and return the builder for it so that further customization can be done.
     *
     * @param <I>
     *            The type of the bucket item
     * @param factory
     *            A factory for the bucket item, which accepts the fluid object supplier and properties and returns a new item
     * @return the {@link ItemBuilder} for the {@link BucketItem}
     */
    public <I extends BucketItem> ItemBuilder<I, FluidBuilder<T, P>> bucket(NonNullBiFunction<? extends SimpleFlowableFluid, Item.Properties, ? extends I> factory) {
        if (this.defaultBucket == Boolean.FALSE) {
            throw new IllegalStateException("Only one call to bucket/noBucket per builder allowed");
        }
        this.defaultBucket = false;
        NonNullSupplier<? extends SimpleFlowableFluid> source = this.source;
        // TODO: Can we find a way to circumvent this limitation?
        if (source == null) {
            throw new IllegalStateException("Cannot create a bucket before creating a source block");
        }
        return getOwner().<I, FluidBuilder<T, P>>item(this, bucketName, p -> ((NonNullBiFunction<SimpleFlowableFluid, Item.Properties, ? extends I>) factory).apply(this.source.get(), p))
            .properties(p -> p.craftRemainder(Items.BUCKET).stacksTo(1))
            .model((ctx, prov) -> prov.generated(ctx::getEntry, new ResourceLocation(getOwner().getModid(), "item/" + bucketName)));
    }

    @Beta
    public FluidBuilder<T, P> noBucket() {
        if (this.defaultBucket == Boolean.FALSE) {
            throw new IllegalStateException("Only one call to bucket/noBucket per builder allowed");
        }
        this.defaultBucket = false;
        return this;
    }

    /**
     * Assign {@link TagKey}{@code s} to this fluid and its source fluid. Multiple calls will add additional tags.
     *
     * @param tags
     *            The tags to assign
     * @return this {@link FluidBuilder}
     */
    @SafeVarargs
    public final FluidBuilder<T, P> tag(TagKey<Fluid>... tags) {
        FluidBuilder<T, P> ret = this.tag(ProviderType.FLUID_TAGS, tags);
        if (this.tags.isEmpty()) {
            ret.getOwner().<RegistrateTagsProvider<Fluid>, Fluid>setDataGenerator(ret.sourceName, getRegistryKey(), ProviderType.FLUID_TAGS,
                prov -> this.tags.stream().map(prov::tag).forEach(p -> p.add(getSource())));
        }
        this.tags.addAll(Arrays.asList(tags));
        return ret;
    }

    /**
     * Remove {@link TagKey}{@code s} from this fluid and its source fluid. Multiple calls will remove additional tags.
     *
     * @param tags
     *            The tags to remove
     * @return this {@link FluidBuilder}
     */
    @SafeVarargs
    public final FluidBuilder<T, P> removeTag(TagKey<Fluid>... tags) {
        this.tags.removeAll(Arrays.asList(tags));
        return this.removeTag(ProviderType.FLUID_TAGS, tags);
    }

    private SimpleFlowableFluid getSource() {
        NonNullSupplier<? extends SimpleFlowableFluid> source = this.source;
        Preconditions.checkNotNull(source, "Fluid has no source block: " + sourceName);
        return source.get();
    }

    private SimpleFlowableFluid.Properties makeProperties() {
        NonNullSupplier<? extends SimpleFlowableFluid> source = this.source;
        SimpleFlowableFluid.Properties ret = new SimpleFlowableFluid.Properties(source == null ? null : source::get, asSupplier());
        fluidProperties.accept(ret);
        return ret;
    }

    @Override
    protected T createEntry() {
        return fluidFactory.apply(makeProperties());
    }

    @Environment(EnvType.CLIENT)
    protected void registerDefaultRenderer(T flowing) {
        FluidRenderHandlerRegistry.INSTANCE.register(getSource(), flowing, new SimpleFluidRenderHandler(stillTexture, flowingTexture));
        ClientSpriteRegistryCallback.event(InventoryMenu.BLOCK_ATLAS).register((atlas, registry) -> {
            registry.register(stillTexture);
            registry.register(flowingTexture);
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Additionally registers the source fluid and the fluid type (if constructed).
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public FluidEntry<T> register() {
        // Check the fluid has a type.
        if (this.attributeHandler != null) {
            // Register the type.
            onRegister(entry -> {
                FluidVariantAttributeHandler handler = attributeHandler.get();
                FluidVariantAttributes.register(entry, handler);
                FluidVariantAttributes.register(getSource(), handler);
            });
        }

        EnvExecutor.runWhenOn(EnvType.CLIENT, () -> () -> onRegister(this::registerDefaultRenderer));

        if (defaultSource == Boolean.TRUE) {
            source(SimpleFlowableFluid.Source::new);
        }
        if (defaultBlock == Boolean.TRUE) {
            block().register();
        }
        if (defaultBucket == Boolean.TRUE) {
            bucket().register();
        }

        NonNullSupplier<? extends SimpleFlowableFluid> source = this.source;
        if (source != null) {
            getCallback().accept(sourceName, Registry.FLUID_REGISTRY, (FluidBuilder) this, source::get);
        } else {
            throw new IllegalStateException("Fluid must have a source version: " + getName());
        }

        return (FluidEntry<T>) super.register();
    }

    @Override
    protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
        return new FluidEntry<>(getOwner(), delegate);
    }
}
