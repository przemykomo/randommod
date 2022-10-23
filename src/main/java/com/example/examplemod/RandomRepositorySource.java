package com.example.examplemod;

import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;

import java.util.function.Consumer;

public class RandomRepositorySource implements RepositorySource {

    private final RandomPackResources randomPackResources = new RandomPackResources();

    @Override
    public void loadPacks(Consumer<Pack> packConsumer, Pack.PackConstructor packConstructor) {
        Pack pack = Pack.create(ExampleMod.MODID + "_generated", false, () -> randomPackResources, packConstructor, Pack.Position.TOP, PackSource.DEFAULT);

        if (pack == null) {
            System.out.println(ExampleMod.MODID + "is null!!!!!");
        } else {
            packConsumer.accept(pack);
        }
    }
}
