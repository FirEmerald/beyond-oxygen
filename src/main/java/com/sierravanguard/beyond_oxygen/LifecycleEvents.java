package com.sierravanguard.beyond_oxygen;

import com.sierravanguard.beyond_oxygen.data.*;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = BeyondOxygen.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LifecycleEvents {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        if (event.includeServer()) {
            event.getGenerator().addProvider(true, (DataProvider.Factory<BOItemTagsProvider>) output -> new BOItemTagsProvider(
                    output,
                    event.getLookupProvider(),
                    event.getExistingFileHelper()
            ));
            event.getGenerator().addProvider(true, (DataProvider.Factory<BOFluidTagsProvider>) output -> new BOFluidTagsProvider(
                    output,
                    event.getLookupProvider(),
                    event.getExistingFileHelper()
            ));
            event.getGenerator().addProvider(true, (DataProvider.Factory<BOEntityTypeTagsProvider>) output -> new BOEntityTypeTagsProvider(
                    output,
                    event.getLookupProvider(),
                    event.getExistingFileHelper()
            ));
            event.getGenerator().addProvider(true, (DataProvider.Factory<BODimensionTagsProvider>) output -> new BODimensionTagsProvider(
                    output,
                    event.getLookupProvider(),
                    event.getExistingFileHelper()
            ));
            event.getGenerator().addProvider(true, (DataProvider.Factory<BORecipesProvider>) BORecipesProvider::new);
            event.getGenerator().addProvider(true, (DataProvider.Factory<DatapackBuiltinEntriesProvider>)output -> new DatapackBuiltinEntriesProvider(
                    output,
                    event.getLookupProvider(),
                    new RegistrySetBuilder()
                            .add(Registries.DAMAGE_TYPE, BODamageTypesProvider::register),
                    Set.of(BeyondOxygen.MODID)
            ));
            event.getGenerator().addProvider(true, (DataProvider.Factory<LootTableProvider>) output -> new LootTableProvider(output,
                    Set.of(),
                    List.of(
                            new LootTableProvider.SubProviderEntry(BOBlockLootProvider::new, LootContextParamSets.BLOCK))
            ));
        }
    }
}
