    package com.sierravanguard.beyond_oxygen;

    import com.sierravanguard.beyond_oxygen.registry.BODimensions;
    import net.minecraft.ResourceLocationException;
    import net.minecraft.resources.ResourceLocation;
    import net.minecraftforge.common.ForgeConfigSpec;
    import net.minecraftforge.eventbus.api.SubscribeEvent;
    import net.minecraftforge.fml.common.Mod;
    import net.minecraftforge.fml.event.config.ModConfigEvent;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Objects;

    @Mod.EventBusSubscriber(modid = BeyondOxygen.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public class BOConfig {
        private static final ForgeConfigSpec.ConfigValue<Integer> VENT_RANGE;
        private static final ForgeConfigSpec.ConfigValue<Integer> OXYGEN_CONSUMPTION;

        private static final ForgeConfigSpec.ConfigValue<Integer> BUBBLE_MAX_RADIUS;
        private static final ForgeConfigSpec.ConfigValue<Integer> TIME_TO_IMPLODE;

        private static final ForgeConfigSpec.BooleanValue UNBREATHABLE_DIMENSIONS;
        private static final ForgeConfigSpec.ConfigValue<List<? extends String>> UNBREATHABLE_DIMENSIONS_WHITELIST;
        private static final ForgeConfigSpec.ConfigValue<List<? extends String>> UNBREATHABLE_DIMENSIONS_BLACKLIST;
        private static final ForgeConfigSpec.BooleanValue COLD_DIMENSIONS;
        private static final ForgeConfigSpec.ConfigValue<List<? extends String>> COLD_DIMENSIONS_WHITELIST;
        private static final ForgeConfigSpec.ConfigValue<List<? extends String>> COLD_DIMENSIONS_BLACKLIST;
        private static final ForgeConfigSpec.BooleanValue HOT_DIMENSIONS;
        private static final ForgeConfigSpec.ConfigValue<List<? extends String>> HOT_DIMENSIONS_WHITELIST;
        private static final ForgeConfigSpec.ConfigValue<List<? extends String>> HOT_DIMENSIONS_BLACKLIST;
        private static final ForgeConfigSpec.BooleanValue ENABLE_BABY_MODE;
        private static final ForgeConfigSpec.DoubleValue DEFAULT_DENSITY;

        static final ForgeConfigSpec SPEC;

        //we instantiate all config in the class constructor here so that the builder can be released from memory after creating the config spec.
        static {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            VENT_RANGE = builder
                    .comment("Max range of vent, high value CAN AND WILL cause lag.")
                    .define("ventRange", 2048);
            ENABLE_BABY_MODE = builder
                    .comment("Enables baby mode (Also known as ZAVODCHANIN mode, turns off oxygen requirements for vents)")
                    .define("enableBabyMode", false);
            OXYGEN_CONSUMPTION = builder
                    .comment("How many oxygen units in 1 mb")
                    .define("oxygenConsumption", 10);
            DEFAULT_DENSITY = builder
                    .comment("The approximate density of a ships to use in buoyancy calculations. Set to 0 to disable buoyancy.")
                    .defineInRange("shipDensity", 600d, 0, Double.MAX_VALUE);

            BUBBLE_MAX_RADIUS = builder
                    .comment("Maximum radius of bubble generators")
                    .defineInRange("bubbleMaxRadius", 5, 5, 20);
            TIME_TO_IMPLODE = builder
                    .comment("How many ticks (1 second ~ 20 ticks) for someone to IMPLODE from lack of air? Do not set lower than 15, or you may experience flicker.")
                    .define("timeToImplode", 10);

            UNBREATHABLE_DIMENSIONS = builder
                    .comment("Enable Beyond Oxygen unbreathable dimensions")
                    .define("unbreathable_dimensions", true);
            UNBREATHABLE_DIMENSIONS_WHITELIST = builder
                    .comment("Dimensions that should be considered unbreathable. Adds to the list already defined by the tag \"beyond_oxgyen:unbreathable\".")
                    .defineList("unbreathableDimensionsWhitelist", List.of("minecraft:the_end"), BOConfig::isValidResourceLocation);
            UNBREATHABLE_DIMENSIONS_BLACKLIST = builder
                    .comment("Dimensions that should not be considered unbreathable. Removes from the list already defined by the tag \"beyond_oxgyen:unbreathable\", and the \"unbreathableDimensionsWhitelist\" config option.")
                    .defineList("unbreathableDimensionsBlacklist", List.of(), BOConfig::isValidResourceLocation);
            COLD_DIMENSIONS = builder
                    .comment("Enable Beyond Oxygen cold dimensions")
                    .define("cold_dimensions", true);
            COLD_DIMENSIONS_WHITELIST = builder
                    .comment("Dimensions that should be considered cold. Adds to the list already defined by the tag \"beyond_oxgyen:cold\".")
                    .defineList("coldDimensionsWhitelist", List.of("minecraft:the_end"), BOConfig::isValidResourceLocation);
            COLD_DIMENSIONS_BLACKLIST = builder
                    .comment("Dimensions that should not be considered cold. Removes from the list already defined by the tag \"beyond_oxgyen:cold\", and the \"coldDimensionsWhitelist\" config option.")
                    .defineList("coldDimensionsBlacklist", List.of(), BOConfig::isValidResourceLocation);
            HOT_DIMENSIONS = builder
                    .comment("Enable Beyond Oxygen hot dimensions")
                    .define("hot_dimensions", true);
            HOT_DIMENSIONS_WHITELIST = builder
                    .comment("Dimensions that should be considered hot. Adds to the list already defined by the tag \"beyond_oxgyen:cold\".")
                    .defineList("hotDimensionsWhitelist", List.of("minecraft:the_nether"), BOConfig::isValidResourceLocation);
            HOT_DIMENSIONS_BLACKLIST = builder
                    .comment("Dimensions that should not be considered hot. Removes from the list already defined by the tag \"beyond_oxgyen:hot\", and the \"hotDimensionsWhitelist\" config option.")
                    .defineList("hotDimensionsBlacklist", List.of(), BOConfig::isValidResourceLocation);

            SPEC = builder.build();
        }

        private static boolean isValidResourceLocation(Object value) {
            return value instanceof String str && ResourceLocation.isValidResourceLocation(str);
        }

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event) {
            if (event.getConfig().getSpec() == SPEC && SPEC.isLoaded()) loadConfig();
        }

        private static int ventRange;
        private static int oxygenConsumption;
        private static double defaultDensity;

        private static int bubbleMaxRadius;
        private static int timeToImplode;

        private static boolean enableUnbreathableDimensions;
        private static List<ResourceLocation> unbreathableDimensionsWhitelist = List.of();
        private static List<ResourceLocation> unbreathableDimensionsBlacklist = List.of();
        private static boolean enableColdDimensions;
        private static List<ResourceLocation> coldDimensionsWhitelist = List.of();
        private static List<ResourceLocation> coldDimensionsBlacklist = List.of();
        private static boolean enableHotDimensions;
        private static List<ResourceLocation> hotDimensionsWhitelist = List.of();
        private static List<ResourceLocation> hotDimensionsBlacklist = List.of();
        private static boolean babyMode;

        //we load all the config values into more friendly versions here, and can process custom syntax issues.
        public static void loadConfig() {
            babyMode = ENABLE_BABY_MODE.get();
            ventRange = VENT_RANGE.get();
            oxygenConsumption = OXYGEN_CONSUMPTION.get();
            defaultDensity = DEFAULT_DENSITY.get();

            bubbleMaxRadius = BUBBLE_MAX_RADIUS.get();
            timeToImplode = TIME_TO_IMPLODE.get();

            enableUnbreathableDimensions = UNBREATHABLE_DIMENSIONS.get();
            unbreathableDimensionsWhitelist = toRlList(UNBREATHABLE_DIMENSIONS_WHITELIST);
            unbreathableDimensionsBlacklist = toRlList(UNBREATHABLE_DIMENSIONS_BLACKLIST);
            enableColdDimensions = COLD_DIMENSIONS.get();
            coldDimensionsWhitelist = toRlList(COLD_DIMENSIONS_WHITELIST);
            coldDimensionsBlacklist = toRlList(COLD_DIMENSIONS_BLACKLIST);
            enableHotDimensions = HOT_DIMENSIONS.get();
            hotDimensionsWhitelist = toRlList(HOT_DIMENSIONS_WHITELIST);
            hotDimensionsBlacklist = toRlList(HOT_DIMENSIONS_BLACKLIST);

            BODimensions.updateSets();
        }

        public static List<ResourceLocation> toRlList(ForgeConfigSpec.ConfigValue<List<? extends String>> configValue) {
            return toRlList(configValue.get());
        }

        public static List<ResourceLocation> toRlList(List<? extends String> list) {
            return list.stream().map(rl -> {
                try {
                    return ResourceLocation.tryParse(rl);
                } catch (ResourceLocationException e) {
                    BeyondOxygen.LOGGER.warn("Invalid resource location in config", e);
                    return null;
                }
            }).filter(Objects::nonNull).toList();
        }

        public static boolean getBabyMode() {
            return babyMode;
        }

        public static int getVentRange() {
            return ventRange;
        }

        public static int getOxygenConsumption() {
            return oxygenConsumption;
        }

        public static double getDefaultDensity() {
            return defaultDensity;
        }

        public static int getTimeToImplode() {
            return timeToImplode;
        }

        public static int getBubbleMaxRadius() {
            return bubbleMaxRadius;
        }

        public static boolean enableUnbreathableDimensions() {
            return enableUnbreathableDimensions;
        }

        public static List<ResourceLocation> unbreathableDimensionsWhitelist() {
            return unbreathableDimensionsWhitelist;
        }

        public static List<ResourceLocation> unbreathableDimensionsBlacklist() {
            return unbreathableDimensionsBlacklist;
        }

        public static boolean enableColdDimensions() {
            return enableColdDimensions;
        }

        public static List<ResourceLocation> coldDimensionsWhitelist() {
            return coldDimensionsWhitelist;
        }

        public static List<ResourceLocation> coldDimensionsBlacklist() {
            return coldDimensionsBlacklist;
        }

        public static boolean enableHotDimensions() {
            return enableHotDimensions;
        }

        public static List<ResourceLocation> hotDimensionsWhitelist() {
            return hotDimensionsWhitelist;
        }

        public static List<ResourceLocation> hotDimensionsBlacklist() {
            return hotDimensionsBlacklist;
        }
    }
