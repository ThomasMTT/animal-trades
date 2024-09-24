package life.thoms.mods.animal_trades.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static life.thoms.mods.animal_trades.AnimalTrades.MOD_LOGGER;
import static life.thoms.mods.animal_trades.util.ValidatorUtils.isValidTrade;

/**
 * Configuration class for the Animal Trades mod.
 * This class handles the configuration settings for the mod,
 * including enabling particle effects and sound effects, and defining
 * animal trade entries.
 */
public class ModConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModConfig.class);

    // Initialize builder and declare spec
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // Declare configs
    public static final ForgeConfigSpec.BooleanValue enableParticlesConfig;
    public static final ForgeConfigSpec.BooleanValue enableSFX;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> tradeStrList;

    // List of trades after parsing trades from strings to nested list
    public static final List<List<String>> tradesList = new ArrayList<>();

    // Config logic
    static {
        // Settings
        BUILDER.push("Config File");
        enableParticlesConfig = BUILDER
                .comment("Shows particles at animal when interacting with selected animals")
                .define("particle_effects", true);
        enableSFX = BUILDER
                .comment("Plays sounds when interacting with selected animals")
                .define("sound_effects", true);
        BUILDER.pop();

        // Trades
        BUILDER.push("Animal Trades");
        tradeStrList = BUILDER
                .comment("List of animal trades: entity_id,currency_item_id,price,effect_id,effect_duration,max_stacked_effect_duration (1200 ticks == 1 min)")
                .defineList("trades",
                        Arrays.asList(
                                "minecraft:bat,minecraft:spider_eye,5,minecraft:night_vision,6000,36000",
                                "minecraft:fox,minecraft:emerald,1,minecraft:speed,3600,36000",
                                "minecraft:squid,minecraft:nautilus_shell,2,minecraft:dolphins_grace,3600,10800",
                                "minecraft:turtle,minecraft:kelp,64,minecraft:water_breathing,1200,6000",
                                "minecraft:bee,minecraft:honey_bottle,4,minecraft:regeneration,1200,4800",
                                "minecraft:panda,minecraft:melon_slice,32,minecraft:resistance,6000,30000",
                                "minecraft:polar_bear,minecraft:salmon,32,minecraft:strength,6000,30000",
                                "minecraft:rabbit,minecraft:golden_carrot,1,minecraft:jump_boost,3600,24000"
                        ),
                        obj -> obj instanceof String // Validation function to check if entries are strings
                );
        BUILDER.pop();

        // Define spec from builder
        SPEC = BUILDER.build();
    }

    /**
     * Parses the trades defined in the configuration into a nested list structure.
     * This method reads the trade strings defined in the configuration,
     * validates them, and stores the valid trades in the tradesList.
     */
    public static void parseTrades() {
        // Clear previous trades list
        tradesList.clear();

        for (String entry : tradeStrList.get()) {
            // Split by comma
            String[] parts = entry.split(",");

            // Validate the number of parts
            if (parts.length != 6) {
                MOD_LOGGER.error("Invalid trade entry (wrong number of parts): {}", entry);
                continue;
            }

            // Check if parts are valid
            if (!isValidTrade(parts)) {
                MOD_LOGGER.error("Invalid trade entry found: {}", entry);
                continue;
            }

            // Add trades to trade list, preventing duplicates
            List<String> trade = Arrays.stream(parts).map(String::trim).toList();
            if (!tradesList.contains(trade)) {
                tradesList.add(trade);
            }
        }
    }
}
