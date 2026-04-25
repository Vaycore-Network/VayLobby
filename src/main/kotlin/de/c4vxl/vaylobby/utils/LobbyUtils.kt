package de.c4vxl.vaylobby.utils

import de.c4vxl.vaylobby.Main
import org.bukkit.GameMode
import org.bukkit.GameRules
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.util.Vector

object LobbyUtils {
    /**
     * Resets a player
     */
    fun Player.reset() {
        this.isFlying = false
        this.exp = 0F
        this.totalExperience = 0
        this.level = 0
        this.inventory.clear()
        this.activePotionEffects.forEach { this.removePotionEffect(it.type) }
        this.fireTicks = 0
        this.resetMaxHealth()
        this.health = this.maxHealth
        this.gameMode = GameMode.SURVIVAL
    }

    /**
     * Applies lobby game rules to the spawn world
     * @param world The world to apply the game rules to
     */
    fun applyGameRules(world: World) {
        world.apply {
            setGameRule(GameRules.ADVANCE_TIME, false)
            setGameRule(GameRules.ADVANCE_WEATHER, false)
            setGameRule(GameRules.FALL_DAMAGE, false)
            setGameRule(GameRules.DROWNING_DAMAGE, false)
            setGameRule(GameRules.IMMEDIATE_RESPAWN, true)
            setGameRule(GameRules.KEEP_INVENTORY, true)
            setGameRule(GameRules.SPAWN_MOBS, false)
            setGameRule(GameRules.MOB_DROPS, false)
            setGameRule(GameRules.FIRE_DAMAGE, false)
            setGameRule(GameRules.RANDOM_TICK_SPEED, 0)
        }
    }

    private val boosterOffset = Main.config.getDouble("config.booster.y-offset", 0.5)
    private val boosterStrength = Main.config.getDouble("config.booster.strength", 1.5)

    /**
     * Applies the boost effect on a player
     * @param player The player to apply the effect on to
     */
    fun applyBooster(player: Player) {
        player.velocity = player.eyeLocation.direction.add(Vector(0.0, boosterOffset, 0.0)).multiply(boosterStrength)
        player.playSound(player.location, Sound.ENTITY_PHANTOM_HURT, 3f, 1f)
    }
}