package com.example.examplemod;

import net.minecraft.world.level.block.Block;

public class ColorBlock extends Block {

    public final int color;

    public ColorBlock(Properties properties, int color) {
        super(properties);
        this.color = color;
    }
}
