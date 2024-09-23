package life.thoms.mods.animal_trades;

import life.thoms.mods.animal_trades.config.ModConfig;
import life.thoms.mods.animal_trades.event.ModPlayerEvents;
import life.thoms.mods.animal_trades.event.ModConnectionEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for the Animal Trades mod.
 * This class is responsible for initializing the mod, loading configurations
 * and registering event listeners.
 */
@Mod(AnimalTrades.MOD_ID)
public class AnimalTrades {

    // Initialize logger
    public static final Logger MOD_LOGGER = LoggerFactory.getLogger(AnimalTrades.class);

    // Initialize mod id, logger and mark mod as enabled
    public static final String MOD_ID = "animal_trades";
    public static boolean MOD_ENABLED = true;

    /**
     * Constructor for the AnimalTrades class.
     * This method registers the mod's configuration and event listeners.
     */
    public AnimalTrades() {
        // Load config
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON,
                ModConfig.SPEC, MOD_ID + ".toml");

        // Get eventBus and register this class and ModEvents as event listeners
        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.register(this);
        eventBus.register(ModPlayerEvents.class);
        eventBus.register(ModConnectionEvents.class);
    }
}
