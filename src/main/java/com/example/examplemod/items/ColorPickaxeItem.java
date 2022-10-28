package com.example.examplemod.items;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.GeneratedObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

public class ColorPickaxeItem extends PickaxeItem implements GeneratedObject {

    public final int color;

    public ColorPickaxeItem(Tier tier, int attackDamageBaseline, float attackSpeed, Properties properties, int color) {
        super(tier, attackDamageBaseline, attackSpeed, properties);
        this.color = color;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public ResourceLocation getModel() {
        return ExampleMod.PICKAXE_MODEL;
    }
}
