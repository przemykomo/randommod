package com.example.examplemod;

import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RandomPackResources implements PackResources {

    public static final ImmutableSet<String> NAMESPACES = ImmutableSet.of("minecraft", "examplemod");

    private final Map<ResourceLocation, byte[]> resourcesMap = new HashMap<>();

    public RandomPackResources() {
        String pickaxeMineable = """
                    {"replace":false,"values":[""" + ExampleMod.generatedBlocks.stream().map(block -> "\"" + ForgeRegistries.BLOCKS.getKey(block).toString() + "\"").collect(Collectors.joining(",")) +
                "]}";
        resourcesMap.put(new ResourceLocation("minecraft", "tags/blocks/mineable/pickaxe.json"), pickaxeMineable.getBytes());

        for (ExampleMod.GeneratedEntry generatedEntry : ExampleMod.generatedEntries) {
            resourcesMap.put(new ResourceLocation("examplemod", "loot_tables/blocks/" + generatedEntry.id() + "_ore.json"),
                    ("""
                            {
                              "type": "minecraft:block",
                              "pools": [
                                {
                                  "bonus_rolls": 0.0,
                                  "entries": [
                                    {
                                      "type": "minecraft:alternatives",
                                      "children": [
                                        {
                                          "type": "minecraft:item",
                                          "conditions": [
                                            {
                                              "condition": "minecraft:match_tool",
                                              "predicate": {
                                                "enchantments": [
                                                  {
                                                    "enchantment": "minecraft:silk_touch",
                                                    "levels": {
                                                      "min": 1
                                                    }
                                                  }
                                                ]
                                              }
                                            }
                                          ],
                                          "name": "examplemod:""" + generatedEntry.id() + "_ore\"" + """
                                        },
                                        {
                                          "type": "minecraft:item",
                                          "functions": [
                                            {
                                              "enchantment": "minecraft:fortune",
                                              "formula": "minecraft:ore_drops",
                                              "function": "minecraft:apply_bonus"
                                            },
                                            {
                                              "function": "minecraft:explosion_decay"
                                            }
                                          ],
                                          "name": "examplemod:""" + generatedEntry.id() + "_raw_ore\"" + """
                                        }
                                      ]
                                    }
                                  ],
                                  "rolls": 1.0
                                }
                              ]
                            }""").getBytes());
        }
    }

    @Nullable
    @Override
    public InputStream getRootResource(String filename) {
//        System.out.println("getRootResource " + filename);
        return InputStream.nullInputStream();
    }

    @Override
    public InputStream getResource(PackType packType, ResourceLocation resourceLocation) {
        byte[] bytes = resourcesMap.get(resourceLocation);
        if (bytes != null) {
            return new ByteArrayInputStream(bytes);
        }
        return InputStream.nullInputStream();
    }

    @Override
    public Collection<ResourceLocation> getResources(PackType packType, String namespace, String directory, Predicate<ResourceLocation> predicate) {
        Set<ResourceLocation> set = new HashSet<>();
        if (packType == PackType.SERVER_DATA) {
            if (namespace.equals("minecraft")) {
                if (directory.equals("tags/blocks")) {
                    set.add(new ResourceLocation("minecraft", "tags/blocks/mineable/pickaxe.json"));
                }
            } else {
                if (directory.equals("loot_tables") && namespace.equals("examplemod")) {
                    for (ExampleMod.GeneratedEntry generatedEntry : ExampleMod.generatedEntries) {
                        set.add(new ResourceLocation("examplemod", "loot_tables/blocks/" + generatedEntry.id() + "_ore.json"));
                    }
                }
            }
        }
        return set;
    }

    @Override
    public boolean hasResource(PackType packType, ResourceLocation resourceLocation) {
        return false;
    }

    @Override
    public Set<String> getNamespaces(PackType packType) {
        if (packType == PackType.SERVER_DATA) {
            return NAMESPACES;
        }
        return Set.of();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializer) {
        return metadataSectionSerializer == PackMetadataSection.SERIALIZER ? (T) ServerPacksSource.BUILT_IN_METADATA : null;
    }

    @Override
    public String getName() {
        return "MyName";
    }

    @Override
    public void close() {}
}
