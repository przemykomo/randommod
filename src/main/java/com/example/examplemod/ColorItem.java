package com.example.examplemod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ColorItem extends Item implements GeneratedObject {

    public final int color;
    public final ResourceLocation model;

    public ColorItem(Properties properties, int color, ResourceLocation model) {
        super(properties);
        this.color = color;
        this.model = model;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public ResourceLocation getModel() {
        return model;
    }
}
