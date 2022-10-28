package com.example.examplemod.items;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.GeneratedObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Tier;

public class ColorAxeItem extends AxeItem implements GeneratedObject {

    public final int color;

    public ColorAxeItem(Tier tier, float attackDamageBaseline, float attackSpeed, Properties properties, int color) {
        super(tier, attackDamageBaseline, attackSpeed, properties);
        this.color = color;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public ResourceLocation getModel() {
        return ExampleMod.AXE_MODEL;
    }
}
