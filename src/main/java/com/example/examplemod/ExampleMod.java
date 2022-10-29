package com.example.examplemod;

import com.example.examplemod.items.*;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "examplemod";

    public static final ArrayList<GeneratedEntry> generatedEntries = new ArrayList<>();
    public static final ArrayList<ColorBlock> generatedBlocks = new ArrayList<>();
    public static final ArrayList<Item> generatedItems = new ArrayList<>();
    public static final ResourceLocation PICKAXE_MODEL = new ResourceLocation(MODID, "item/pickaxe");
    public static final ResourceLocation AXE_MODEL = new ResourceLocation(MODID, "item/axe");
    public static final ResourceLocation SHOVEL_MODEL = new ResourceLocation(MODID, "item/shovel");
    public static final ResourceLocation SWORD_MODEL = new ResourceLocation(MODID, "item/sword");
    public static final ResourceLocation HOE_MODEL = new ResourceLocation(MODID, "item/hoe");
    public static final ResourceLocation RAW_ORE = new ResourceLocation(MODID, "item/raw_ore");
    public static final ResourceLocation INGOT = new ResourceLocation(MODID, "item/ingot");
    public static final ResourceLocation ORE_TEMPLATE = new ResourceLocation(MODID, "block/ore_template");

    public record GeneratedEntry(String id, int color, Tier toolTier) {}

    public ExampleMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::registerContent);
        modEventBus.addListener(this::registerModels);
        modEventBus.addListener(this::bakeModels);
        modEventBus.addListener(this::registerItemColorHandlers);
        modEventBus.addListener(this::registerBlockColorHandlers);
        modEventBus.addListener(this::addPackFinders);

        Random random = new Random(20);
        for (int i = 0; i < 10; i++) {
            ForgeTier tier = new ForgeTier(2, random.nextInt(20, 4000), random.nextFloat(2.0f, 14.0f), random.nextFloat(0.0f, 7.0f), random.nextInt(5, 25), BlockTags.create(new ResourceLocation(MODID, "needs_generated" + i + "_tool")), () -> Ingredient.EMPTY);
            generatedEntries.add(new GeneratedEntry("generated" + i,
                    Color.HSBtoRGB(random.nextFloat(), random.nextFloat(0.5f, 1.0f), random.nextFloat(0.5f, 1.0f)),
                    tier));
            switch (random.nextInt(3)) {
                case 0 -> TierSortingRegistry.registerTier(tier, new ResourceLocation(MODID, "generated" + i), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));
                case 1 -> TierSortingRegistry.registerTier(tier, new ResourceLocation(MODID, "generated" + i), List.of(Tiers.DIAMOND), List.of(Tiers.NETHERITE));
                case 2 -> TierSortingRegistry.registerTier(tier, new ResourceLocation(MODID, "generated" + i), List.of(Tiers.NETHERITE), List.of());
            }
        }
    }

    private void addPackFinders(AddPackFindersEvent event) {
        event.addRepositorySource(new RandomRepositorySource());
    }

    private void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        for (Item colorItem : generatedItems) {
            event.register((itemStack, tintIndex) -> {
                if (tintIndex != 0) {
                    return -1;
                }

                return ((GeneratedObject) colorItem).getColor();
            }, colorItem);
        }

        for (ColorBlock colorBlock : generatedBlocks) {
            Item item = colorBlock.asItem();
            if (item instanceof BlockItem) {
                event.register((itemStack, tintIndex) -> colorBlock.color, item);
            }
        }
    }

    private void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        for (ColorBlock colorBlock : generatedBlocks) {
            event.register((BlockState blockState, @Nullable BlockAndTintGetter blockAndTintGetter, @Nullable BlockPos blockPos, int layer) -> colorBlock.color,
                    colorBlock);
        }
    }

    private void registerModels(ModelEvent.RegisterAdditional event) {
        event.register(PICKAXE_MODEL);
        event.register(AXE_MODEL);
        event.register(SHOVEL_MODEL);
        event.register(SWORD_MODEL);
        event.register(HOE_MODEL);
        event.register(RAW_ORE);
        event.register(INGOT);
        event.register(ORE_TEMPLATE);
    }

    private void bakeModels(ModelEvent.BakingCompleted event) {
        Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();
        BakedModel bakedOreModel = modelRegistry.get(ORE_TEMPLATE);

        for (Item colorItem : generatedItems) {
            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(ForgeRegistries.ITEMS.getKey(colorItem), "inventory");
            modelRegistry.put(modelResourceLocation, modelRegistry.get(((GeneratedObject) colorItem).getModel()));
        }

        for (ColorBlock colorBlock : generatedBlocks) {
            colorBlock.getStateDefinition().getPossibleStates().forEach(blockState ->
                    modelRegistry.put(BlockModelShaper.stateToModelLocation(blockState), bakedOreModel));
            Item item = colorBlock.asItem();
            if (item instanceof BlockItem) {
                ModelResourceLocation modelResourceLocation = new ModelResourceLocation(ForgeRegistries.ITEMS.getKey(item), "inventory");
                modelRegistry.put(modelResourceLocation, bakedOreModel);
            }
        }
    }

    private static final CreativeModeTab creativeModeTab = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return Items.ACACIA_DOOR.getDefaultInstance();
        }
    };

    private void registerContent(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS)) {
            for (GeneratedEntry generatedEntry : generatedEntries) {
                event.register(ForgeRegistries.Keys.BLOCKS, new ResourceLocation(MODID, generatedEntry.id + "_ore"), () -> {
                    ColorBlock block = new ColorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE), generatedEntry.color);
                    generatedBlocks.add(block);
                    return block;
                });
            }
        }

        if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
            for (GeneratedEntry generatedEntry : generatedEntries) {
                event.register(ForgeRegistries.Keys.ITEMS, new ResourceLocation(MODID, generatedEntry.id + "_pickaxe"), () -> {
                    ColorPickaxeItem item = new ColorPickaxeItem(generatedEntry.toolTier, 1, -2.8f, new Item.Properties().tab(creativeModeTab), generatedEntry.color);
                    generatedItems.add(item);
                    return item;
                });

                event.register(ForgeRegistries.Keys.ITEMS, new ResourceLocation(MODID, generatedEntry.id + "_axe"), () -> {
                    ColorAxeItem item = new ColorAxeItem(generatedEntry.toolTier, 6.0f, -3.1f, new Item.Properties().tab(creativeModeTab), generatedEntry.color);
                    generatedItems.add(item);
                    return item;
                });

                event.register(ForgeRegistries.Keys.ITEMS, new ResourceLocation(MODID, generatedEntry.id + "_shovel"), () -> {
                    ColorShovelItem item = new ColorShovelItem(generatedEntry.toolTier, 1.5f, -3.0f, new Item.Properties().tab(creativeModeTab), generatedEntry.color);
                    generatedItems.add(item);
                    return item;
                });

                event.register(ForgeRegistries.Keys.ITEMS, new ResourceLocation(MODID, generatedEntry.id + "_sword"), () -> {
                    ColorSwordItem item = new ColorSwordItem(generatedEntry.toolTier, 3, -2.4f, new Item.Properties().tab(creativeModeTab), generatedEntry.color);
                    generatedItems.add(item);
                    return item;
                });

                event.register(ForgeRegistries.Keys.ITEMS, new ResourceLocation(MODID, generatedEntry.id + "_hoe"), () -> {
                    ColorHoeItem item = new ColorHoeItem(generatedEntry.toolTier, -2, -1.0f, new Item.Properties().tab(creativeModeTab), generatedEntry.color);
                    generatedItems.add(item);
                    return item;
                });

                event.register(ForgeRegistries.Keys.ITEMS, new ResourceLocation(MODID, generatedEntry.id + "_raw_ore"), () -> {
                    ColorItem item = new ColorItem(new Item.Properties().tab(creativeModeTab), generatedEntry.color, RAW_ORE);
                    generatedItems.add(item);
                    return item;
                });

                event.register(ForgeRegistries.Keys.ITEMS, new ResourceLocation(MODID, generatedEntry.id + "_ingot"), () -> {
                    ColorItem item = new ColorItem(new Item.Properties().tab(creativeModeTab), generatedEntry.color, INGOT);
                    generatedItems.add(item);
                    return item;
                });
            }

            for (Block block : generatedBlocks) {
                event.register(ForgeRegistries.Keys.ITEMS, new ResourceLocation(MODID, ForgeRegistries.BLOCKS.getKey(block).getPath()), () -> new BlockItem(block, new Item.Properties().tab(creativeModeTab)));
            }
        }
    }
}
