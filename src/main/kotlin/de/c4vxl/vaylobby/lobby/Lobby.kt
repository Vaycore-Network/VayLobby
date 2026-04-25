package de.c4vxl.vaylobby.lobby

import de.c4vxl.vaylobby.Main
import de.c4vxl.vaylobby.utils.LobbyUtils.reset
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player

object Lobby {
    private var spawn: Location = Main.config.getLocation("spawn") ?: Bukkit.getWorlds().first().spawnLocation

    /**
     * Sets the spawn location
     * @param location The location to set the spawn to
     */
    fun setSpawn(location: Location) {
        Main.config.apply {
            set("spawn", location)
            save(Main.instance.dataFolder.resolve("config.yml"))
        }
    }

    /**
     * Returns the spawn location
     */
    fun spawn() =
        spawn

    /**
     * Sends a player to spawn
     * @param player The player to send
     */
    fun sendPlayer(player: Player) {
        player.reset()
        player.teleport(spawn())

        Item.SPAWN_TP(player)?.let { player.inventory.setItem(it.first, it.second) }
        Item.SERVER_SELECTOR(player)?.let { player.inventory.setItem(it.first, it.second) }

        // Play sound
        player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3f, 1f)
    }
}