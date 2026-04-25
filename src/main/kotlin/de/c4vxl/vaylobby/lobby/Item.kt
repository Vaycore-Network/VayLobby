package de.c4vxl.vaylobby.lobby

import de.c4vxl.vaycoreapi.language.Lang.Companion.getLang
import de.c4vxl.vaycoreapi.utils.ItemBuilder
import de.c4vxl.vaylobby.Main
import de.c4vxl.vaylobby.ui.ServerSelector
import de.c4vxl.vaylobby.utils.LobbyUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

/**
 * List of items
 */
object Item {
    /**
     * Returns the material of a specific item according to config
     * @param item The name of the item
     */
    private fun getMaterial(item: String): Material {
        val materialName = Main.config.getString("config.item.$item.material")?.lowercase()
        return Material.entries.find { it.name.lowercase() == materialName } ?: Material.AIR
    }

    private fun ItemBuilder.rightClick(cooldown: Int, onClick: (PlayerInteractEvent) -> Unit): ItemBuilder {
        return this.onEvent(PlayerInteractEvent::class.java) { event ->
            if (!event.action.isRightClick)
                return@onEvent

            if (event.player.hasCooldown(event.item!!.type))
                return@onEvent

            event.player.setCooldown(event.item!!.type, cooldown)
            onClick(event)
        }
    }

    private val spawnTpItemCooldown = Main.config.getInt("config.item.spawn-tp-cooldown", 5) * 20
    private val serverSelectorItemCooldown = Main.config.getInt("config.item.server-selector", 1) * 20

    private fun item(name: String, item: ItemStack): Pair<Int, ItemStack>? {
        val slot = Main.config.getInt("config.item.$name.slot", -1)
        
        if (slot < 0)
            return null

        return slot to item
    }

    fun SPAWN_TP(player: Player) = item(
        "spawn-tp",
        ItemBuilder(
            getMaterial("spawn-tp"),
            player.getLang<Main>().getCmp("item.spawn-tp.name")
        )
            .rightClick(spawnTpItemCooldown) { Lobby.sendPlayer(player) }
            .build())

    fun SERVER_SELECTOR(player: Player) = item(
        "server-selector",
        ItemBuilder(
            getMaterial("server-selector"),
            player.getLang<Main>().getCmp("item.server-selector.name")
        )
            .rightClick(serverSelectorItemCooldown) { ServerSelector(player).open() }
            .build()
    )

    fun BOOSTER(player: Player) = item(
        "booster",
        ItemBuilder(
            getMaterial("booster"),
            player.getLang<Main>().getCmp("item.booster.name")
        )
            .rightClick(serverSelectorItemCooldown) { LobbyUtils.applyBooster(player) }
            .build()
    )
}