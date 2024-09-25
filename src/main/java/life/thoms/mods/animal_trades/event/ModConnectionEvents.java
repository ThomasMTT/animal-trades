package life.thoms.mods.animal_trades.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraft.client.Minecraft;

import static life.thoms.mods.animal_trades.AnimalTrades.MOD_ENABLED;

/**
 * Handles events related to player login and logout in the mod.
 * This class is responsible for disabling or enabling the mod
 * based on whether the player is connected to a multiplayer server.
 */
@Mod.EventBusSubscriber(modid = "animal_trades", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModConnectionEvents {

    /**
     * Called when the player logs into the game.
     * Disables the mod functionality if the player is connected to a multiplayer server.
     *
     * @param event The event containing information about the player login.
     */
    @SubscribeEvent
    public static void onPlayerLoggedIn(ClientPlayerNetworkEvent.LoggedInEvent event) {
        // Check if player is connected to a multiplayer server
        if (Minecraft.getInstance().getCurrentServer() != null) {
            MOD_ENABLED = false;
        }
    }

    /**
     * Called when the player logs out of the game.
     * Re-enables the mod functionality when leaving a server.
     *
     * @param event The event containing information about the player logout.
     */
    @SubscribeEvent
    public static void onPlayerLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        MOD_ENABLED = true;
    }
}
