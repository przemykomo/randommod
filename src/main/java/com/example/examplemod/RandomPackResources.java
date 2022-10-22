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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RandomPackResources implements PackResources {

    public static final ImmutableSet<String> NAMESPACES = ImmutableSet.of("minecraft");

    @Nullable
    @Override
    public InputStream getRootResource(String filename) {
//        System.out.println("getRootResource " + filename);
        return InputStream.nullInputStream();
    }

    @Override
    public InputStream getResource(PackType packType, ResourceLocation resourceLocation) {
        System.out.println("getResource " + packType.getDirectory() + resourceLocation);
        if (resourceLocation.getPath().equals("tags/blocks/mineable/pickaxe.json")) {
            StringBuilder stringBuilder = new StringBuilder("""
                    {"replace":false,"values":[""");
            stringBuilder.append(ExampleMod.generatedBlocks.stream().map(block -> "\"" + ForgeRegistries.BLOCKS.getKey(block).toString() + "\"").collect(Collectors.joining(",")));
//            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("]}");
            return new ByteArrayInputStream(stringBuilder.toString().getBytes());
        }
        return InputStream.nullInputStream();
    }

    @Override
    public Collection<ResourceLocation> getResources(PackType packType, String namespace, String directory, Predicate<ResourceLocation> predicate) {
        Set<ResourceLocation> set = new HashSet<>();
        if (packType == PackType.SERVER_DATA && directory.equals("tags/blocks")) {
            set.add(new ResourceLocation("minecraft", "tags/blocks/mineable/pickaxe.json"));
        }
        return set;
    }

    @Override
    public boolean hasResource(PackType packType, ResourceLocation resourceLocation) {
//        System.out.println("hasResource " + packType.getDirectory() + resourceLocation);
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
