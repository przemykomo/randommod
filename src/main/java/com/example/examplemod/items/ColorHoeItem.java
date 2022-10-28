package com.example.examplemod.items;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.GeneratedObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

public class ColorHoeItem extends HoeItem implements GeneratedObject {

    public final int color;

    public ColorHoeItem(Tier tier, int attackDamageBaseline, float attackSpeed, Properties properties, int color) {
        super(tier, attackDamageBaseline, attackSpeed, properties);
        this.color = color;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public ResourceLocation getModel() {
        return ExampleMod.HOE_MODEL;
    }
}
