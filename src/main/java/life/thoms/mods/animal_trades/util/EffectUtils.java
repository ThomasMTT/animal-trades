package life.thoms.mods.animal_trades.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData; // Update to IParticleData for particles
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.server.ServerWorld;

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
    public static void sendSoundAndParticles(PlayerEntity player, Entity targetEntity,
                                             SoundEvent sound, IParticleData particle) { // Update to IParticleData

        // Check if player and target entity are not null
        if (player == null || targetEntity == null) {
            // Handle error
            return;
        }

        // Check if sound effects are enabled
        if (enableSFX.get()) {
            player.level.playSound(null, player.blockPosition(), sound, SoundCategory.NEUTRAL, 1.0F, 0.5F); // Correct usage of SoundSource
        }

        // Check if particle effects are enabled and the current world is a server level
        if (enableParticlesConfig.get() && player.level instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) player.level;
            world.sendParticles(particle, // Particles
                    targetEntity.getX(), targetEntity.getY() + 1.0, targetEntity.getZ(), // Location
                    5,  // Number of particles
                    0.3, 0.3, 0.3,  // Spread (X, Y, Z)
                    0.1  // Particle speed or motion
            );
        }
    }
}
