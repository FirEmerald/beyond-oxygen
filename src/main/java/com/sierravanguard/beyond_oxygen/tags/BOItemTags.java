package com.sierravanguard.beyond_oxygen.tags;

import com.sierravanguard.beyond_oxygen.utils.ResourceLocations;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BOItemTags {
    public static final TagKey<Item> SPACE_SUIT_HELMETS = beyondOxygen("armor/space_suit/helmets");
    public static final TagKey<Item> SPACE_SUIT_CHESTPLATES = beyondOxygen("armor/space_suit/chestplates");
    public static final TagKey<Item> SPACE_SUIT_LEGGINGS = beyondOxygen("armor/space_suit/leggings");
    public static final TagKey<Item> SPACE_SUIT_BOOTS = beyondOxygen("armor/space_suit/boots");

    public static final TagKey<Item> CRYO_HELMETS = beyondOxygen("armor/cryo/helmets");
    public static final TagKey<Item> CRYO_CHESTPLATES = beyondOxygen("armor/cryo/chestplates");
    public static final TagKey<Item> CRYO_LEGGINGS = beyondOxygen("armor/cryo/leggings");
    public static final TagKey<Item> CRYO_BOOTS = beyondOxygen("armor/cryo/boots");

    public static final TagKey<Item> THERMAL_HELMETS = beyondOxygen("armor/thermal/helmets");
    public static final TagKey<Item> THERMAL_CHESTPLATES = beyondOxygen("armor/thermal/chestplates");
    public static final TagKey<Item> THERMAL_LEGGINGS = beyondOxygen("armor/thermal/leggings");
    public static final TagKey<Item> THERMAL_BOOTS = beyondOxygen("armor/thermal/boots");

    public static final TagKey<Item> SPACE_SUIT_REPAIR_MATERIAL = beyondOxygen("repair_item/space_suit");
    public static final TagKey<Item> CRYO_REPAIR_MATERIAL = beyondOxygen("repair_item/cryo");
    public static final TagKey<Item> THERMAL_REPAIR_MATERIAL = beyondOxygen("repair_item/thermal");

    public static final TagKey<Item> SPACE_SUIT_EATABLE = beyondOxygen("space_suit_eatable");
    public static final TagKey<Item> BREATHABLES = beyondOxygen("breathables");


    public static final TagKey<Item> FORGE_STEEL_INGOT = forge("ingots/steel");
    public static final TagKey<Item> STEEL_INGOT = common("ingots/steel");
    public static final TagKey<Item> REFINED_OBSIDIAN_INGOT = common("ingots/refined_obsidian");
    public static final TagKey<Item> REFINED_GLOWSTONE_INGOT = common("ingots/refined_glowstone");

    public static final TagKey<Item> IRON_NUGGET = common("nuggets/iron");

    public static final TagKey<Item> REDSTONE_DUST = common("dusts/redstone");

    public static final TagKey<Item> POTATO = common("crops/potato");
    public static final TagKey<Item> BREAD = common("foods/bread");

    public static final TagKey<Item> GLASS_BLOCK = common("glass_blocks");


    //curios compat
    public static final TagKey<Item> CURIOS_BACK_SLOT = curios("back");

    private static TagKey<Item> beyondOxygen(String path) {
        return ItemTags.create(ResourceLocations.beyondOxygen(path));
    }

    private static TagKey<Item> curios(String path) {
        return ItemTags.create(ResourceLocations.curios(path));
    }

    private static TagKey<Item> forge(String path) {
        return ItemTags.create(ResourceLocations.forge(path));
    }

    private static TagKey<Item> common(String path) {
        return ItemTags.create(ResourceLocations.common(path));
    }
}
