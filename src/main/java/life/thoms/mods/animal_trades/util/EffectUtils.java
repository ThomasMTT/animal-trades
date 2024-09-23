package life.thoms.mods.animal_trades.util;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import static life.thoms.mods.animal_trades.config.ModConfig.enableParticlesConfig;
import static life.thoms.mods.animal_trades.config.ModConfig.enableSFX;

/**
 * Utility class for sending sound and particle effects in the Animal Trades mod.
 */
public class EffectUtils {

    /**
     * Sends sound and particle effects to the player and target entity.
     *
     * @param player        The player to receive the effects.
     * @param targetEntity  The entity that the effects will be centered on.
     * @param sound         The sound event to be played.
     * @param particle      The type of particle to be displayed.
     */
    public static void sendSoundAndParticles(Player player, Entity targetEntity,
                                             SoundEvent sound, SimpleParticleType particle) {

        // Check if player and animal are not null
        if (player == null || targetEntity == null) {
            // Handle error
            return;
        }

        // Check if sound effects are enabled
        if (enableSFX.get()) {
            player.level().playSound(null, player.blockPosition(), sound,
                    SoundSource.NEUTRAL, 1.0F, 0.5F);
        }

        // Check if particle effects are enabled and the current world is a server level
        if (enableParticlesConfig.get() && !player.level().isClientSide && player.level() instanceof ServerLevel level) {
            level.sendParticles(particle, // Particles
                    targetEntity.getX(), targetEntity.getY() + 1.0, targetEntity.getZ(), // Location
                    5,  // Number of particles
                    0.3, 0.3, 0.3,  // Spread (X, Y, Z)
                    0.1  // Particle speed or motion
            );
        }
    }
}
