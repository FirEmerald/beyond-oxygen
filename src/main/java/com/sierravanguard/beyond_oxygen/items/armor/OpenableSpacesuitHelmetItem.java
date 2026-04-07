package com.sierravanguard.beyond_oxygen.items.armor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class OpenableSpacesuitHelmetItem extends SpacesuitArmorItem implements IOpenableSpacesuitHelmetItem {
    public OpenableSpacesuitHelmetItem(ArmorMaterial material, Type type, Item.Properties properties) {
        super(material, type, properties);
    }

    public ResourceLocation getHelmetTexture(ItemStack stack, Entity entity) {
        String material = getMaterial().getName();
        String domain;
        int domainSepIndex = material.indexOf(':');
        if (domainSepIndex != -1) {
            domain = material.substring(0, domainSepIndex);
            material = material.substring(domainSepIndex + 1);
        } else domain = "minecraft";
        return ResourceLocation.fromNamespaceAndPath(domain, "textures/models/armor/" + material + "_helmet_closed.png");
    }
}
