package de.c4vxl.vaylobby.utils

import org.bukkit.GameMode
import org.bukkit.GameRules
import org.bukkit.World
import org.bukkit.entity.Player

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
}