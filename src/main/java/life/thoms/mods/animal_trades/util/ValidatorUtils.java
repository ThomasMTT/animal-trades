package life.thoms.mods.animal_trades.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Utility class for validation functions related to trade entries in the Animal Trades mod.
 * This class provides static methods to validate entity IDs, currency item IDs,
 * effect IDs, and to check if strings represent positive integers.
 */
public class ValidatorUtils {

    /**
     * Validates the components of a trade entry.
     *
     * @param parts An array of strings representing the components of a trade entry.
     * @return true if the trade entry is valid; false otherwise.
     */
    public static boolean isValidTrade(String[] parts) {
        return isValidEntityId(parts[0]) && isValidCurrencyItemId(parts[1]) &&
                isPositiveInteger(parts[2]) && isValidEffectId(parts[3]) &&
                isPositiveInteger(parts[4]) && isPositiveInteger(parts[5]);
    }

    /**
     * Validates whether the given entity ID is a valid Minecraft entity.
     *
     * @param id The entity ID to validate.
     * @return true if the entity ID is valid; false otherwise.
     */
    public static boolean isValidEntityId(String id) {
        return EntityType.byString(id).isPresent();
    }

    /**
     * Validates whether the given effect ID corresponds to a registered MobEffect.
     *
     * @param id The effect ID to validate.
     * @return true if the effect ID is valid; false otherwise.
     */
    public static boolean isValidEffectId(String id) {
        return ForgeRegistries.MOB_EFFECTS.getHolder(new ResourceLocation(id)).isPresent();
    }

    /**
     * Validates whether the given currency item ID corresponds to a valid item.
     *
     * @param id The item ID to validate.
     * @return true if the item ID is valid and not air; false otherwise.
     */
    public static boolean isValidCurrencyItemId(String id) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
        return item != null && item != Items.AIR; // Ensure it's not air
    }

    /**
     * Checks if the given string represents a positive integer.
     *
     * @param str The string to check.
     * @return true if the string is a valid positive integer; false otherwise.
     */
    public static boolean isPositiveInteger(String str) {
        try {
            return Integer.parseInt(str) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
}
