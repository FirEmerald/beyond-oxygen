package com.sierravanguard.beyond_oxygen.data;

import com.sierravanguard.beyond_oxygen.registry.BOBlocks;
import com.sierravanguard.beyond_oxygen.registry.BOItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Set;

public class BOBlockLootProvider extends BlockLootSubProvider {
    public BOBlockLootProvider() {
        super(Set.of(
                BOItems.BUBBLE_GENERATOR.get(),
                BOItems.CRYO_BED.get(),
                BOItems.OXYGEN_HARVESTER.get(),
                BOItems.VENT.get()
        ), FeatureFlags.REGISTRY.allFlags());
    }

    protected Iterable<Block> getKnownBlocks() {
        return List.of(
                BOBlocks.BUBBLE_GENERATOR.get(),
                BOBlocks.CRYO_BED.get(),
                BOBlocks.OXYGEN_HARVESTER.get(),
                BOBlocks.VENT.get()
        );
    }

    @Override
    protected void generate() {
        dropSelf(BOBlocks.BUBBLE_GENERATOR.get());
        dropSelf(BOBlocks.CRYO_BED.get());
        dropSelf(BOBlocks.OXYGEN_HARVESTER.get());
        dropSelf(BOBlocks.VENT.get());
    }
}
