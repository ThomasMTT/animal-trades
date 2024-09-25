package life.thoms.mods.animal_trades.util;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import static life.thoms.mods.animal_trades.config.ModConfig.*;
import static life.thoms.mods.animal_trades.util.EffectUtils.sendSoundAndParticles;

/**
 * Utility class for handling trade operations in the Animal Trades mod.
 */
public class TradeUtils {

    /**
     * Retrieves available trades for a given player and animal.
     * <p>
     * This method checks the trades list for valid trades based on the player's inventory,
     * the animal involved, and any existing effects on the player. If a valid trade is found,
     * it returns the details of the trade; otherwise, it returns null.
     *
     * @param player The player attempting to trade.
     * @param animal The animal involved in the trade.
     * @param hand  The players hand
     * @return A list of trade details if a valid trade is found; null otherwise.
     */
    public static List<String> getAvailableTrade(PlayerEntity player, Entity animal, Hand hand) {


        synchronized (tradesList) {
            if (tradesList.isEmpty()) {
                if (!tradeStrList.get().isEmpty()) {
                    parseTrades();
                }
            }

            // Check for all possible trades
            for (List<String> innerListOriginal : tradesList) {

                // Create a copy to avoid overwriting the tradesList
                List<String> innerList = new ArrayList<>(innerListOriginal);

                // Compare names
                if (!innerList.get(0).equals(animal.getEncodeId())) {
                    continue;
                }

                // Get existing effect of this type if exists
                Effect playerEffect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(innerList.get(3)));

                // If it doesn't exist check next trade
                if (playerEffect == null) {
                    continue;
                }

                // Initialize effect variables
                EffectInstance existingEffect = player.getEffect(playerEffect);
                int effectDuration = Integer.parseInt(innerList.get(4));
                int maxEffectDuration = Integer.parseInt(innerList.get(5));

                // Compare items in main hands
                if (!innerList.get(1).equals(player.getItemInHand(hand).getDescriptionId()
                        .replace("item.", "")
                        .replace(".", ":"))) {
                    continue;
                }

                // Check if effect duration is longer than allowed
                if (existingEffect != null) {
                    effectDuration += existingEffect.getDuration();
                    if (effectDuration > maxEffectDuration) {
                        sendSoundAndParticles(player, animal, SoundEvents.NOTE_BLOCK_BASS, ParticleTypes.CLOUD);
                        continue;
                    }
                    innerList.set(4, String.valueOf(effectDuration));
                }

                // Check amount >= price
                int price = Integer.parseInt(innerList.get(2));
                if (player.getItemInHand(hand).getCount() >= price && price > 0) {
                    return innerList; // Return trade details
                } else {
                    sendSoundAndParticles(player, animal, SoundEvents.NOTE_BLOCK_BASS, ParticleTypes.CLOUD);
                }
            }

            // Returns null if nothing is found
            return null;
        }
    }
}
