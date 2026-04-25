package de.c4vxl.vaylobby.handler

import de.c4vxl.vaylobby.Main
import de.c4vxl.vaylobby.lobby.Lobby
import de.c4vxl.vaylobby.utils.LobbyUtils
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.player.PlayerToggleFlightEvent

/**
 * Handles players in the lobby
 */
class LobbyHandler : Listener {
    init {
        Bukkit.getPluginManager().registerEvents(this, Main.instance)
    }

    private val maxDistance: Int = Main.config.getInt("config.max-distance-to-spawn", 100)
    private val minHeight: Int = Main.config.getInt("config.min-height", -60)

    @EventHandler
    fun onPlayerDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? Player ?: return

        if (damager.gameMode == GameMode.CREATIVE)
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onItemDrop(event: PlayerDropItemEvent) {
        if (event.player.gameMode == GameMode.CREATIVE)
            return

        event.isCancelled = true
    }

    @EventHandler
    fun onFoodChange(event: FoodLevelChangeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onMoveAway(event: PlayerMoveEvent) {
        if (event.player.location.blockY > minHeight &&
            event.to.distance(Lobby.spawn()) < maxDistance)
            return

        Lobby.sendPlayer(event.player)
    }

    @EventHandler
    fun onInvClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        if (player.gameMode == GameMode.CREATIVE) return

        event.isCancelled = true
    }

    @EventHandler
    fun onOffhand(event: PlayerSwapHandItemsEvent) {
        if (event.player.gameMode == GameMode.CREATIVE) return

        event.isCancelled = true
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.player.gameMode == GameMode.CREATIVE) return

        event.isCancelled = true
    }

    @EventHandler
    fun onBoost(event: PlayerToggleFlightEvent) {
        if (event.player.gameMode == GameMode.CREATIVE)
            return

        if (!event.isFlying)
            return

        if (!Main.config.getBoolean("config.enable-booster"))
            return

        if (event.player.location.subtract(0.0, 2.0, 0.0).block.type == Material.AIR && event.player.isFlying)
            return

        LobbyUtils.applyBooster(event.player)

        event.isCancelled = true
    }
}