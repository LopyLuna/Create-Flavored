package uwu.lopyluna.create_dd.registry.helper.woodtype;

import static com.simibubi.create.AllInteractionBehaviours.interactionBehaviour;
import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.foundation.data.TagGen.axeOnly;
import static com.simibubi.create.foundation.data.WindowGen.woodenWindowBlock;
import static com.simibubi.create.foundation.data.WindowGen.woodenWindowPane;
import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;
import static uwu.lopyluna.create_dd.Desires.REGISTRATE;
import static uwu.lopyluna.create_dd.registry.DesiresTags.forgeItemTag;
import static uwu.lopyluna.create_dd.registry.DesiresTags.modItemTag;

import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.behaviour.DoorMovingInteraction;
import com.simibubi.create.content.contraptions.behaviour.TrapdoorMovingInteraction;
import com.simibubi.create.content.decoration.palettes.ConnectedGlassPaneBlock;
import com.simibubi.create.content.decoration.palettes.WindowBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.client.model.generators.ModelFile;
import uwu.lopyluna.create_dd.DesireUtil;
import uwu.lopyluna.create_dd.content.blocks.functional.AxisBlock;
import uwu.lopyluna.create_dd.content.blocks.functional.Combustible.*;
import uwu.lopyluna.create_dd.content.blocks.functional.sliding_door.WoodenSlidingDoorBlock;
import uwu.lopyluna.create_dd.infrastructure.utility.ForgeTags;
import uwu.lopyluna.create_dd.registry.DesiresCreativeModeTabs;
import uwu.lopyluna.create_dd.registry.DesiresTags;

public class WoodEntry {
    public WoodType woodType;

    public BlockEntry<CombustibleBlock> plank;
    public BlockEntry<? extends Block> slab;
    public BlockEntry<? extends Block> stairs;
    public BlockEntry<? extends Block> log;
    public BlockEntry<? extends Block> strippedLog;
    public BlockEntry<? extends Block> wood;
    public BlockEntry<? extends Block> strippedWood;

    public BlockEntry<? extends Block> trapdoors;
    public BlockEntry<? extends Block> door;

    //WOOD_SLIDING_DOOR

    public BlockEntry<? extends Block> slidingDoor;

    //fences but no tag

    public BlockEntry<? extends Block> fence;
    public BlockEntry<? extends Block> fenceGate;
    public BlockEntry<? extends Block> pressurePlate;
    public BlockEntry<? extends Block> button;

    //WOOD_SIGN

    public BlockEntityEntry<? extends BlockEntity> signBE;
    public BlockEntry<? extends StandingSignBlock> standingSign;
    public BlockEntry<? extends WallSignBlock> wallSign;
    public BlockEntry<? extends Block> hangingSign1_20;
    public ItemEntry<? extends SignItem> sign;

    //WOOD_BOAT

    public ItemEntry<? extends Item> boat;
    public ItemEntry<? extends Item> chestBoat;

    //WOOD_SAPLING

    public BlockEntry<? extends Block> leaves;
    public BlockEntry<? extends Block> sapling;

    public Holder<ConfiguredFeature<TreeConfiguration, ?>> tree;
    public Holder<PlacedFeature> tree_checked;
    public Holder<ConfiguredFeature<RandomFeatureConfiguration, ?>> tree_spawn;

    public Holder<PlacedFeature>  tree_placed;

    public BlockEntry<WindowBlock> window;
    public BlockEntry<ConnectedGlassPaneBlock> window_pane;

    @SuppressWarnings("removal")
    public static WoodEntry create(String name, WoodTypes type, boolean flammable, boolean hasSlidingDoor, boolean folds, boolean orientable, boolean hasBoat, boolean hasSign, boolean hasTree) {
        boolean isWood = type == WoodTypes.WOOD;
        boolean anyAll = isWood || type == WoodTypes.ALL;

        NonNullSupplier<? extends CreativeModeTab> tab = () -> DesiresCreativeModeTabs.PALETTES_CREATIVE_TAB;
        String id = name.toLowerCase().replace(" ", "_");

        String palettesPath = "palettes/wood_types/" + id + "/";
        String texturePath = "block/" + palettesPath;
        String texturePathID = texturePath + id;

        WoodType woodType;

        BlockEntry<CombustibleBlock> plank;
        BlockEntry<CombustibleSlabBlock> slab;
        BlockEntry<CombustibleStairBlock> stairs;
        BlockEntry<AxisBlock> log;
        BlockEntry<AxisBlock> strippedLog;
        BlockEntry<AxisBlock> wood;
        BlockEntry<AxisBlock> strippedWood;

        BlockEntry<CombustibleTrapdoorBlock> trapdoors;
        BlockEntry<DoorBlock> door;

        BlockEntry<WoodenSlidingDoorBlock> slidingDoor;

        BlockEntry<CombustibleFenceBlock> fence;
        BlockEntry<CombustibleFenceGateBlock> fenceGate;
        BlockEntry<PressurePlateBlock> pressurePlate;
        BlockEntry<WoodButtonBlock> button;

        BlockEntityEntry<? extends BlockEntity> signBE;
        BlockEntry<? extends StandingSignBlock> standingSign;
        BlockEntry<? extends WallSignBlock> wallSign;
        BlockEntry<? extends Block> hangingSign1_20;
        ItemEntry<? extends SignItem> sign;

        ItemEntry<? extends Item> boat;
        ItemEntry<? extends Item> chestBoat;

        BlockEntry<? extends Block> leaves;
        BlockEntry<? extends Block> sapling;

        Holder<ConfiguredFeature<TreeConfiguration, ?>> tree;
        Holder<PlacedFeature> tree_checked;
        Holder<ConfiguredFeature<RandomFeatureConfiguration, ?>> tree_spawn;

        Holder<PlacedFeature>  tree_placed;

        BlockEntry<WindowBlock> window;
        BlockEntry<ConnectedGlassPaneBlock> window_pane;

        if (anyAll) {

            woodType = new CustomWoodType(id);
            plank = REGISTRATE.block(id + "_planks", flammable ? p -> CombustibleBlock.create(p, 5, 20) : CombustibleBlock::create)
                    .initialProperties(() -> Blocks.OAK_PLANKS)
                    .lang(name + " Planks")
                    .blockstate((c, p) -> BlockStateGen.cubeAll(c, p, palettesPath))
                    .transform(axeOnly())
                    .tag(!flammable ? BlockTags.NON_FLAMMABLE_WOOD : DesiresTags.AllBlockTags.FLAMMABLE_WOOD.tag)
                    .tag(BlockTags.PLANKS)
                    .item()
                    .recipe((c, p) -> ShapelessRecipeBuilder.shapeless(c.get(), 4)
                            .requires(modItemTag(id + "_logs")).group("planks")
                            .unlockedBy("has_log", has(modItemTag(id + "_logs")))
                            .save(p)
                    )
                    .tag(ItemTags.PLANKS)
                    .tag(!flammable ? ItemTags.NON_FLAMMABLE_WOOD : DesiresTags.AllItemTags.FLAMMABLE_WOOD.tag)
                    .tab(tab)
                    .build()
                    .register();
            ResourceLocation plankTextures = DesireUtil.asResource(texturePathID + "_planks");
            slab = REGISTRATE.block(id + "_slab", flammable ? p -> CombustibleSlabBlock.create(p, 5, 20) : CombustibleSlabBlock::create)
                    .initialProperties(() -> Blocks.OAK_SLAB)
                    .lang(name + " Slab")
                    .blockstate((c, p) -> p.slabBlock(c.get(), DesireUtil.asResource("block/" + id + "_planks"), plankTextures))
                    .transform(axeOnly())
                    .tag(!flammable ? BlockTags.NON_FLAMMABLE_WOOD : DesiresTags.AllBlockTags.FLAMMABLE_WOOD.tag)
                    .tag(BlockTags.SLABS)
                    .tag(BlockTags.WOODEN_SLABS)
                    .item()
                    .recipe((c, p) -> ShapedRecipeBuilder.shaped(c.get(), 6).define('#', plank.get())
                            .pattern("###")
                            .group("wooden_slab")
                            .unlockedBy("has_planks", has(plank.get())).save(p))
                    .tag(ItemTags.SLABS)
                    .tag(ItemTags.WOODEN_SLABS)
                    .tag(!flammable ? ItemTags.NON_FLAMMABLE_WOOD : DesiresTags.AllItemTags.FLAMMABLE_WOOD.tag)
                    .tab(tab)
                    .build()
                    .register();
            stairs = REGISTRATE.block(id + "_stairs", flammable ? p -> CombustibleStairBlock.create(plank.get().defaultBlockState(), p, 5, 20) : p -> CombustibleStairBlock.create(plank.get().defaultBlockState(), p))
                    .initialProperties(() -> Blocks.OAK_STAIRS)
                    .lang(name + " Stairs")
                    .blockstate((c, p) -> p.stairsBlock(c.get(), plankTextures))
                    .transform(axeOnly())
                    .tag(!flammable ? BlockTags.NON_FLAMMABLE_WOOD : DesiresTags.AllBlockTags.FLAMMABLE_WOOD.tag)
                    .tag(BlockTags.STAIRS)
                    .tag(BlockTags.WOODEN_STAIRS)
                    .item()
                    .recipe((c, p) -> ShapedRecipeBuilder.shaped(c.get(), 4).define('#', plank.get())
                            .pattern("#  ")
                            .pattern("## ")
                            .pattern("###")
                            .group("wooden_stairs")
                            .unlockedBy("has_planks", has(plank.get())).save(p))
                    .tag(ItemTags.STAIRS)
                    .tag(ItemTags.WOODEN_STAIRS)
                    .tag(!flammable ? ItemTags.NON_FLAMMABLE_WOOD : DesiresTags.AllItemTags.FLAMMABLE_WOOD.tag)
                    .tab(tab)
                    .build()
                    .register();
            String strippedSuffix = "block/palettes/wood_types/" + id + "/stripped_" + id;
            strippedLog = REGISTRATE.block("stripped_" + id + "_log", flammable ? p -> AxisBlock.create(p, 5, 5) : AxisBlock::create)
                    .initialProperties(() -> Blocks.STRIPPED_OAK_LOG)
                    .lang("Stripped " + name + " Log")
                    .blockstate((c, p) -> p.axisBlock(c.get(), DesireUtil.asResource(strippedSuffix + "_log"), DesireUtil.asResource(texturePathID + "_log_top")))
                    .transform(axeOnly())
                    .tag(flammable ? BlockTags.LOGS_THAT_BURN : BlockTags.NON_FLAMMABLE_WOOD)
                    .tag(BlockTags.LOGS)
                    .item()
                    .tag(flammable ? ItemTags.LOGS_THAT_BURN : ItemTags.NON_FLAMMABLE_WOOD)
                    .tag(AllTags.AllItemTags.MODDED_STRIPPED_LOGS.tag)
                    .tag(forgeItemTag("stripped_logs"))
                    .tag(modItemTag(id + "_logs"))
                    .tag(ItemTags.LOGS)
                    .tab(tab)
                    .build()
                    .register();
            log = REGISTRATE.block(id + "_log", flammable ? p -> AxisBlock.create(p, 5, 5) : AxisBlock::create)
                    .initialProperties(() -> Blocks.OAK_LOG)
                    .lang(name + " Log")
                    .blockstate((c, p) -> p.axisBlock(c.get(), DesireUtil.asResource(texturePathID + "_log"), DesireUtil.asResource(texturePathID + "_log_top")))
                    .transform(axeOnly())
                    .tag(flammable ? BlockTags.LOGS_THAT_BURN : BlockTags.NON_FLAMMABLE_WOOD)
                    .tag(BlockTags.LOGS)
                    .item()
                    .tag(flammable ? ItemTags.LOGS_THAT_BURN : ItemTags.NON_FLAMMABLE_WOOD)
                    .tag(forgeItemTag("logs"))
                    .tag(modItemTag(id + "_logs"))
                    .tag(ItemTags.LOGS)
                    .tab(tab)
                    .build()
                    .register();
            strippedWood = REGISTRATE.block("stripped_" + id + "_wood", flammable ? p -> AxisBlock.create(p, 5, 5) : AxisBlock::create)
                    .initialProperties(() -> Blocks.STRIPPED_OAK_WOOD)
                    .lang("Stripped " + name + " Wood")
                    .blockstate((c, p) -> p.axisBlock(c.get(), DesireUtil.asResource(strippedSuffix + "_log"), DesireUtil.asResource(strippedSuffix + "_log")))
                    .transform(axeOnly())
                    .tag(flammable ? BlockTags.LOGS_THAT_BURN : BlockTags.NON_FLAMMABLE_WOOD)
                    .tag(BlockTags.LOGS)
                    .item()
                    .recipe((c, p) -> ShapedRecipeBuilder.shaped(c.get(), 3).define('#', strippedLog.get())
                            .pattern("##")
                            .pattern("##")
                            .group("bark")
                            .unlockedBy("has_log", has(strippedLog.get())).save(p)
                    )
                    .tag(flammable ? ItemTags.LOGS_THAT_BURN : ItemTags.NON_FLAMMABLE_WOOD)
                    .tag(AllTags.AllItemTags.MODDED_STRIPPED_WOOD.tag)
                    .tag(forgeItemTag("stripped_wood"))
                    .tag(modItemTag(id + "_logs"))
                    .tag(ItemTags.LOGS)
                    .tab(tab)
                    .build()
                    .register();
            wood = REGISTRATE.block(id + "_wood", flammable ? p -> AxisBlock.create(p, 5, 5) : AxisBlock::create)
                    .initialProperties(() -> Blocks.OAK_WOOD)
                    .lang(name + " Wood")
                    .blockstate((c, p) -> p.axisBlock(c.get(), DesireUtil.asResource(texturePathID + "_log"), DesireUtil.asResource(texturePathID + "_log")))
                    .transform(axeOnly())
                    .tag(flammable ? BlockTags.LOGS_THAT_BURN : BlockTags.NON_FLAMMABLE_WOOD)
                    .tag(BlockTags.LOGS)
                    .item()
                    .recipe((c, p) -> ShapedRecipeBuilder.shaped(c.get(), 3).define('#', log.get())
                            .pattern("##")
                            .pattern("##")
                            .group("bark")
                            .unlockedBy("has_log", has(log.get())).save(p)
                    )
                    .tag(flammable ? ItemTags.LOGS_THAT_BURN : ItemTags.NON_FLAMMABLE_WOOD)
                    .tag(forgeItemTag("wood"))
                    .tag(modItemTag(id + "_logs"))
                    .tag(ItemTags.LOGS)
                    .tab(tab)
                    .build()
                    .register();
            fence = REGISTRATE.block(id + "_fence", flammable ? p -> CombustibleFenceBlock.create(p, 5, 20) : CombustibleFenceBlock::create)
                    .initialProperties(() -> Blocks.OAK_FENCE)
                    .lang(name + " Fence")
                    .blockstate((c, p) -> p.fenceBlock(c.get(), plankTextures))
                    .transform(axeOnly())
                    .tag(!flammable ? BlockTags.NON_FLAMMABLE_WOOD : DesiresTags.AllBlockTags.FLAMMABLE_WOOD.tag)
                    .tag(BlockTags.FENCES)
                    .tag(BlockTags.WOODEN_FENCES)
                    .tag(ForgeTags.Blocks.FENCES)
                    .tag(ForgeTags.Blocks.FENCES_WOODEN)
                    .item()
                    .recipe((c, p) -> ShapedRecipeBuilder.shaped(c.get(), 3).define('#', plank.get()).define('S', Items.STICK)
                            .pattern("#S#")
                            .pattern("#S#")
                            .group("wooden_fence")
                            .unlockedBy("has_planks", has(plank.get())).save(p))
                    .tag(ItemTags.FENCES)
                    .tag(ItemTags.WOODEN_FENCES)
                    .tag(ForgeTags.Items.FENCES)
                    .tag(ForgeTags.Items.FENCES_WOODEN)
                    .tag(!flammable ? ItemTags.NON_FLAMMABLE_WOOD : DesiresTags.AllItemTags.FLAMMABLE_WOOD.tag)
                    .model((c, p) -> p.fenceInventory(c.getName(), plankTextures))
                    .tab(tab)
                    .build()
                    .register();
            fenceGate = REGISTRATE.block(id + "_fence_gate", flammable ? p -> CombustibleFenceGateBlock.create(p, 5, 20) : CombustibleFenceGateBlock::create)
                    .initialProperties(() -> Blocks.OAK_FENCE_GATE)
                    .lang(name + " Fence Gate")
                    .blockstate((c, p) -> p.fenceGateBlock(c.get(), plankTextures))
                    .transform(axeOnly())
                    .tag(!flammable ? BlockTags.NON_FLAMMABLE_WOOD : DesiresTags.AllBlockTags.FLAMMABLE_WOOD.tag)
                    .tag(BlockTags.FENCE_GATES)
                    .tag(ForgeTags.Blocks.FENCE_GATES)
                    .tag(ForgeTags.Blocks.FENCE_GATES_WOODEN)
                    .item()
                    .recipe((c, p) -> ShapedRecipeBuilder.shaped(c.get(), 1).define('#', plank.get()).define('S', Items.STICK)
                            .pattern("S#S")
                            .pattern("S#S")
                            .group("wooden_fence_gate")
                            .unlockedBy("has_planks", has(plank.get())).save(p))
                    .tag(ForgeTags.Items.FENCE_GATES)
                    .tag(ForgeTags.Items.FENCE_GATES_WOODEN)
                    .tag(!flammable ? ItemTags.NON_FLAMMABLE_WOOD : DesiresTags.AllItemTags.FLAMMABLE_WOOD.tag)
                    .tag()
                    .tab(tab)
                    .build()
                    .register();
            pressurePlate = REGISTRATE.block(id + "_pressure_plate", p -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, p))
                    .initialProperties(() -> Blocks.OAK_BUTTON)
                    .lang(name + " Pressure Plate")
                    .blockstate((c, p) -> p.pressurePlateBlock(c.get(), plankTextures))
                    .transform(axeOnly())
                    .tag(!flammable ? BlockTags.NON_FLAMMABLE_WOOD : DesiresTags.AllBlockTags.FLAMMABLE_WOOD.tag)
                    .tag(BlockTags.PRESSURE_PLATES)
                    .tag(BlockTags.WOODEN_PRESSURE_PLATES)
                    .item()
                    .recipe((c, p) -> ShapedRecipeBuilder.shaped(c.get(), 1).define('#', plank.get())
                            .pattern("##")
                            .group("wooden_pressure_plate")
                            .unlockedBy("has_planks", has(plank.get())).save(p))
                    .tag(DesiresTags.optionalTag(Registry.ITEM_REGISTRY, new ResourceLocation("minecraft", "pressure_plates")))
                    .tag(ItemTags.WOODEN_PRESSURE_PLATES)
                    .tag(!flammable ? ItemTags.NON_FLAMMABLE_WOOD : DesiresTags.AllItemTags.FLAMMABLE_WOOD.tag)
                    .tab(tab)
                    .build()
                    .register();
            button = REGISTRATE.block(id + "_button", WoodButtonBlock::new)
                    .initialProperties(() -> Blocks.OAK_BUTTON)
                    .lang(name + " Button")
                    .blockstate((c, p) -> p.buttonBlock(c.get(), plankTextures))
                    .transform(axeOnly())
                    .tag(!flammable ? BlockTags.NON_FLAMMABLE_WOOD : DesiresTags.AllBlockTags.FLAMMABLE_WOOD.tag)
                    .tag(BlockTags.BUTTONS)
                    .tag(BlockTags.WOODEN_BUTTONS)
                    .item()
                    .recipe((c, p) -> ShapelessRecipeBuilder.shapeless(c.get(), 1)
                            .requires(plank.get()).group("wooden_button")
                            .unlockedBy("has_planks", has(plank.get())).save(p))
                    .tag(ItemTags.BUTTONS)
                    .tag(ItemTags.WOODEN_BUTTONS)
                    .tag(!flammable ? ItemTags.NON_FLAMMABLE_WOOD : DesiresTags.AllItemTags.FLAMMABLE_WOOD.tag)
                    .model((c, p) -> p.buttonInventory(c.getName(), plankTextures))
                    .tab(tab)
                    .build()
                    .register();
            trapdoors = REGISTRATE.block(id + "_trapdoor", flammable ? p -> CombustibleTrapdoorBlock.create(p, 5, 20) : CombustibleTrapdoorBlock::create)
                    .initialProperties(() -> Blocks.OAK_TRAPDOOR)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .lang(name + " Trapdoor")
                    .blockstate((c, p) -> p.trapdoorBlock(c.get(), DesireUtil.asResource("block/palettes/wood_types/" + id + "/" + id + "_trapdoor"), orientable))
                    .transform(axeOnly())
                    .tag(!flammable ? BlockTags.NON_FLAMMABLE_WOOD : DesiresTags.AllBlockTags.FLAMMABLE_WOOD.tag)
                    .tag(BlockTags.TRAPDOORS)
                    .tag(BlockTags.WOODEN_TRAPDOORS)
                    .onRegister(interactionBehaviour(new TrapdoorMovingInteraction()))
                    .item()
                    .recipe((c, p) -> ShapedRecipeBuilder.shaped(c.get(), 2).define('#', plank.get())
                            .pattern("###")
                            .pattern("###")
                            .group("wooden_trapdoor")
                            .unlockedBy("has_planks", has(plank.get())).save(p))
                    .model((c, p) -> {
                        ResourceLocation loc = DesireUtil.asResource("block/palettes/wood_types/" + id + "/" + id + "_trapdoor");
                        if (orientable) {
                            p.trapdoorOrientableBottom(c.getName(), loc);
                        } else {
                            p.trapdoorBottom(c.getName(), loc);
                        }
                    })
                    .tag(ItemTags.TRAPDOORS)
                    .tag(ItemTags.WOODEN_TRAPDOORS)
                    .tag(!flammable ? ItemTags.NON_FLAMMABLE_WOOD : DesiresTags.AllItemTags.FLAMMABLE_WOOD.tag)
                    .tab(tab)
                    .build()
                    .register();
            if (hasSlidingDoor) {
                slidingDoor = REGISTRATE.block(id + "_door", p -> new WoodenSlidingDoorBlock(p, true))
                        .initialProperties(() -> Blocks.OAK_DOOR)
                        .properties(BlockBehaviour.Properties::noOcclusion)
                        .blockstate((c, p) -> {
                            ResourceLocation particle = DesireUtil.asResource("block/palettes/wood_types/" + id + "/" + id + "_planks");
                            ResourceLocation bottom = DesireUtil.asResource("block/palettes/wood_types/" + id + "/" + id + "_door_bottom");
                            ResourceLocation top = DesireUtil.asResource("block/palettes/wood_types/" + id + "/" + id + "_door_top");
                            ResourceLocation side = DesireUtil.asResource("block/palettes/wood_types/" + id + "/" + id + "_door_side");

                            ResourceLocation refModelFoldBottom = Create.asResource("block/copper_door/block_bottom");
                            ResourceLocation refModelFoldTop = Create.asResource("block/copper_door/block_top");
                            ResourceLocation refModelFoldLeft = Create.asResource("block/copper_door/fold_left");
                            ResourceLocation refModelFoldRight = Create.asResource("block/copper_door/fold_right");

                            ResourceLocation refModelSlideBottom = Create.asResource("block/brass_door/block_bottom");
                            ResourceLocation refModelSlideTop = Create.asResource("block/brass_door/block_top");

                            ModelFile modelTop = p.models().withExistingParent("block/" + c.getName() + "/block_top", folds ? refModelFoldTop : refModelSlideTop)
                                    .texture("0", side)
                                    .texture("2", top)
                                    .texture("particle", particle);

                            ModelFile modelBottom = p.models().withExistingParent("block/" + c.getName() + "/block_bottom", folds ? refModelFoldBottom : refModelSlideBottom)
                                    .texture("0", side)
                                    .texture("2", bottom)
                                    .texture("particle", particle);
                            if (folds) {
                                p.models().withExistingParent("block/" + c.getName() + "/fold_left", refModelFoldLeft)
                                        .texture("0", side)
                                        .texture("2", top)
                                        .texture("3", bottom)
                                        .texture("particle", particle);
                                p.models().withExistingParent("block/" + c.getName() + "/fold_right", refModelFoldRight)
                                        .texture("0", side)
                                        .texture("2", top)
                                        .texture("3", bottom)
                                        .texture("particle", particle);
                            }
                            p.doorBlock(c.get(), modelBottom, modelBottom, modelBottom, modelBottom, modelTop, modelTop, modelTop, modelTop);
                        })
                        .addLayer(() -> RenderType::cutoutMipped)
                        .transform(axeOnly())
                        .onRegister(interactionBehaviour(new DoorMovingInteraction()))
                        .onRegister(movementBehaviour(new SlidingDoorMovementBehaviour()))
                        .tag(!flammable ? BlockTags.NON_FLAMMABLE_WOOD : DesiresTags.AllBlockTags.FLAMMABLE_WOOD.tag)
                        .tag(BlockTags.DOORS)
                        .tag(BlockTags.WOODEN_DOORS)
                        .tag(AllTags.AllBlockTags.NON_DOUBLE_DOOR.tag)
                        .loot((lr, block) -> lr.add(block, BlockLoot.createDoorTable(block)))
                        .item()
                        .recipe((c, p) -> ShapedRecipeBuilder.shaped(c.get(), 3).define('#', plank.get())
                                .pattern("##")
                                .pattern("##")
                                .pattern("##")
                                .group("wooden_door")
                                .unlockedBy("has_planks", has(plank.get())).save(p))
                        .tag(ItemTags.DOORS)
                        .tag(ItemTags.WOODEN_DOORS)
                        .tag(!flammable ? ItemTags.NON_FLAMMABLE_WOOD : DesiresTags.AllItemTags.FLAMMABLE_WOOD.tag)
                        .tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag)
                        .model((c, p) -> p.blockSprite(c, p.modLoc("item/" + id + "_door")))
                        .tab(tab)
                        .build()
                        .register();

                door = null;
            } else {
                door = REGISTRATE.block(id + "_door", DoorBlock::new)
                        .initialProperties(() -> Blocks.OAK_DOOR)
                        .properties(p -> p.sound(SoundType.WOOD).noOcclusion())
                        .addLayer(() -> RenderType::cutoutMipped)
                        .lang(name + " Door")
                        .loot((lr, block) -> lr.add(block, BlockLoot.createDoorTable(block)))
                        .blockstate((c, p) -> {
                            //ResourceLocation particle = DesireUtil.asResource("block/palettes/wood_types/" + id + "/" + id + "_planks");*
                            //ResourceLocation side = DesireUtil.asResource("block/palettes/wood_types/" + id + "/" + id + "_door_side");*

                            ResourceLocation bottom = DesireUtil.asResource("block/palettes/wood_types/" + id + "/" + id + "_door_bottom");
                            ResourceLocation top = DesireUtil.asResource("block/palettes/wood_types/" + id + "/" + id + "_door_top");

                            p.doorBlock(c.get(), bottom, top);
                        })
                        .transform(axeOnly())
                        .tag(!flammable ? BlockTags.NON_FLAMMABLE_WOOD : DesiresTags.AllBlockTags.FLAMMABLE_WOOD.tag)
                        .tag(BlockTags.DOORS)
                        .tag(BlockTags.WOODEN_DOORS)
                        .onRegister(interactionBehaviour(new DoorMovingInteraction()))
                        .item()
                        .recipe((c, p) -> ShapedRecipeBuilder.shaped(c.get(), 3).define('#', plank.get())
                                .pattern("##")
                                .pattern("##")
                                .pattern("##")
                                .group("wooden_door")
                                .unlockedBy("has_planks", has(plank.get())).save(p))
                        .tag(ItemTags.DOORS)
                        .tag(ItemTags.WOODEN_DOORS)
                        .tag(!flammable ? ItemTags.NON_FLAMMABLE_WOOD : DesiresTags.AllItemTags.FLAMMABLE_WOOD.tag)
                        .model((c, p) -> p.blockSprite(c, p.modLoc("item/" + id + "_door")))
                        .tab(tab)
                        .build()
                        .register();

                slidingDoor = null;
            }
            if (plank.isPresent()) {
                window = woodenWindowBlock(woodType, plank.get());
                window_pane = woodenWindowPane(woodType, window);
            } else {
                window = null;
                window_pane = null;
            }
            if (hasSign) {
                signBE = null;
                standingSign = null;
                wallSign = null;
                sign = null;

                hangingSign1_20 = null;
            } else {
                signBE = null;
                standingSign = null;
                wallSign = null;
                hangingSign1_20 = null;
                sign = null;
            }

            if (hasBoat) {
                boat = REGISTRATE.item(id + "_boat", p -> new BoatItem(false, Boat.Type.OAK, p))
                        .tab(tab)
                        .register();
                chestBoat = REGISTRATE.item(id + "_chest_boat", p -> new BoatItem(true, Boat.Type.OAK, p))
                        .tab(tab)
                        .register();

            } else {
                boat = null;
                chestBoat = null;
            }
            if (hasTree) {
                tree = null;
                tree_checked = null;
                tree_spawn = null;
                tree_placed = null;

                leaves = REGISTRATE.block(id + "_leaves", p -> CombustibleLeavesBlock.create(p, true))
                        .initialProperties(() -> Blocks.AZALEA_LEAVES)
                        .lang(name + " Leaves")
                        .blockstate((c, p) -> BlockStateGen.cubeAll(c, p, texturePathID + "_leaves"))
                        .simpleItem()
                        .register();
                sapling = null;
            } else {
                tree = null;
                tree_checked = null;
                tree_spawn = null;
                tree_placed = null;

                leaves = null;
                sapling = null;
            }

        } else {
            woodType = null;
            plank = null;
            slab = null;
            stairs = null;
            log = null;
            strippedLog = null;
            wood = null;
            strippedWood = null;
            fence = null;
            fenceGate = null;
            pressurePlate = null;
            button = null;
            trapdoors = null;
            door = null;
            slidingDoor = null;
            signBE = null;
            standingSign = null;
            wallSign = null;
            hangingSign1_20 = null;
            sign = null;
            boat = null;
            chestBoat = null;
            leaves = null;
            sapling = null;
            tree = null;
            tree_checked = null;
            tree_spawn = null;
            tree_placed = null;
            window = null;
            window_pane = null;
        }

        return new WoodEntry(woodType, plank, slab, stairs, log, strippedLog, wood, strippedWood, fence, fenceGate, pressurePlate, button, trapdoors, door, slidingDoor, signBE, standingSign, wallSign, hangingSign1_20, sign, boat, chestBoat, leaves, sapling, tree, tree_checked, tree_spawn, tree_placed, window, window_pane
        );
    }

    @SuppressWarnings("unused")
    private static String name(Block block) {
        return key(block).getPath();
    }
    private static ResourceLocation key(Block block) {
        return Registry.BLOCK.getKey(block);
    }

    public WoodEntry(WoodType woodType,

                     BlockEntry<CombustibleBlock> plank, BlockEntry<? extends Block> slab, BlockEntry<? extends Block> stairs,
                     BlockEntry<? extends Block> log, BlockEntry<? extends Block> strippedLog, BlockEntry<? extends Block> wood, BlockEntry<? extends Block> strippedWood, BlockEntry<? extends Block> fence, BlockEntry<? extends Block> fenceGate, BlockEntry<? extends Block> pressurePlate, BlockEntry<? extends Block> button,

                     BlockEntry<? extends Block> trapdoors, BlockEntry<? extends Block> door, BlockEntry<? extends Block> slidingDoor,
                     BlockEntityEntry<? extends BlockEntity> signBE, BlockEntry<? extends StandingSignBlock> standingSign, BlockEntry<? extends WallSignBlock> wallSign, BlockEntry<? extends Block> hangingSign1_20, ItemEntry<? extends SignItem> sign,
                     ItemEntry<? extends Item> boat, ItemEntry<? extends Item> chestBoat,
                     BlockEntry<? extends Block> leaves, BlockEntry<? extends Block> sapling,
                     Holder<ConfiguredFeature<TreeConfiguration, ?>> tree, Holder<PlacedFeature> tree_checked, Holder<ConfiguredFeature<RandomFeatureConfiguration, ?>> tree_spawn, Holder<PlacedFeature>  tree_placed,
                     BlockEntry<WindowBlock> window, BlockEntry<ConnectedGlassPaneBlock> window_pane
    ) {
        this.woodType = woodType;
        this.plank = plank;
        this.slab = slab;
        this.stairs = stairs;
        this.log = log;
        this.strippedLog = strippedLog;
        this.wood = wood;
        this.strippedWood = strippedWood;
        this.fence = fence;
        this.fenceGate = fenceGate;
        this.pressurePlate = pressurePlate;
        this.button = button;
        this.trapdoors = trapdoors;
        this.door = door;
        this.slidingDoor = slidingDoor;
        this.signBE = signBE;
        this.standingSign = standingSign;
        this.wallSign = wallSign;
        this.hangingSign1_20 = hangingSign1_20;
        this.sign = sign;
        this.boat = boat;
        this.chestBoat = chestBoat;
        this.leaves = leaves;
        this.sapling = sapling;
        this.tree = tree;
        this.tree_checked = tree_checked;
        this.tree_spawn = tree_spawn;
        this.tree_placed = tree_placed;
        this.window = window;
        this.window_pane = window_pane;
    }


}
