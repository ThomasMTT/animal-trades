package life.thoms.mods.animal_trades.event;

import life.thoms.mods.animal_trades.AnimalTrades;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

import static life.thoms.mods.animal_trades.AnimalTrades.MOD_ENABLED;
import static life.thoms.mods.animal_trades.util.EffectUtils.sendSoundAndParticles;
import static life.thoms.mods.animal_trades.util.TradeUtils.getAvailableTrade;

/**
 * Event handler class for the Animal Trades mod.
 * This class listens for player interactions with entities to facilitate trading.
 */
@Mod.EventBusSubscriber(modid = AnimalTrades.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModPlayerEvents {

    private static final String TRADE_BUSY_TAG = "TradeBusy";
    private static final String LAST_TRADE_TIME_TAG = "LastTradeTime";
    private static final long COOLDOWN_TIME = 100; // Cooldown time in milliseconds

    /**
     * Handles the event when a player right-clicks on an entity.
     * Facilitates the trading process with the specified animal.
     *
     * @param event The event containing interaction data between the player and the entity.
     */
    @SubscribeEvent
    public static void onEntityRightClick(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!MOD_ENABLED) {
            return;
        }

        // Get hand
        Hand hand = event.getHand();

        // Only allow the event to proceed for the main hand
        if (hand != Hand.MAIN_HAND) {
            return;
        }

        PlayerEntity player = event.getPlayer();

        LivingEntity animal = (LivingEntity) event.getTarget();

        // Check if the target is a living entity (animal)
        if (!(event.getTarget() instanceof LivingEntity )) {
            return;
        }

        // Check if the animal is currently busy with a trade
        if (animal.getPersistentData().getBoolean(TRADE_BUSY_TAG)) {
            return; // Exit if the animal is busy
        }

        // Check if the player is on cooldown
        long currentTime = player.level.getGameTime() * 50; // Convert to milliseconds
        long lastTradeTime = player.getPersistentData().getLong(LAST_TRADE_TIME_TAG);

        if (currentTime - lastTradeTime < COOLDOWN_TIME) {
            return; // Exit if within cooldown period
        }

        // Mark the animal as busy for trading
        animal.getPersistentData().putBoolean(TRADE_BUSY_TAG, true);

        List<String> trade = getAvailableTrade(player, animal, hand);

        // If trade is available
        if (trade == null || trade.size() < 5) {
            animal.getPersistentData().putBoolean(TRADE_BUSY_TAG, false); // Reset tag if trade not available
            return;
        }

        // Initialize variables for trade processing
        Item animalCurrency = ForgeRegistries.ITEMS.getValue(new ResourceLocation(trade.get(1)));
        int price;
        Effect playerEffect;
        int effectDuration;

        // Validate and parse values
        try {
            price = Integer.parseInt(trade.get(2));
            playerEffect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(trade.get(3)));
            effectDuration = Integer.parseInt(trade.get(4));
        } catch (NumberFormatException | NullPointerException e) {
            animal.getPersistentData().putBoolean(TRADE_BUSY_TAG, false); // Reset tag on exception
            return;
        }

        // Validate values and process trade
        if (animalCurrency != null && price > 0 && playerEffect != null && effectDuration > 0) {
            // Check if player has enough currency
            if (!player.isCreative() && player.getItemInHand(hand).getCount() < price) {
                animal.getPersistentData().putBoolean(TRADE_BUSY_TAG, false); // Reset tag if not enough currency
                return; // Exit if player doesn't have enough currency
            }

            // Remove currency from player inventory if not creative
            if (!player.isCreative()) {
                player.getItemInHand(hand).shrink(price);
            }

            // Remove existing effect before applying the new one
            player.removeEffect(playerEffect);
            player.addEffect(new EffectInstance(playerEffect, effectDuration, 0));
            sendSoundAndParticles(player, animal, SoundEvents.NOTE_BLOCK_CHIME, ParticleTypes.HEART);

            // Set the last trade time to the current time
            player.getPersistentData().putLong(LAST_TRADE_TIME_TAG, currentTime);
        }

        // Reset the busy tag after processing the trade
        animal.getPersistentData().putBoolean(TRADE_BUSY_TAG, false);
    }
}
