package de.c4vxl.vaylobby.ui

import de.c4vxl.vaycoreapi.language.Lang.Companion.getLang
import de.c4vxl.vaycoreapi.network.Network.connectToService
import de.c4vxl.vaycoreapi.utils.ItemBuilder
import de.c4vxl.vaylobby.Main
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * The Server selection ui
 */
class ServerSelector(
    val player: Player
) {
    val items: Map<Int, ItemStack> =
        Main.config.getConfigurationSection("server-selector")
            ?.let { section ->
                section.getKeys(false).mapNotNull { key ->
                    val asInt = key.toIntOrNull() ?: return@mapNotNull null
                    val service = section.getString("$key.service") ?: return@mapNotNull null
                    val name = MiniMessage.miniMessage().deserialize(section.getString("$key.name") ?: return@mapNotNull null)
                    val material = section.getString("$key.material")?.lowercase()?.let { name ->
                        Material.entries.find { it.name.lowercase() == name }
                    } ?: return@mapNotNull null

                    asInt to ItemBuilder(
                        material,
                        name
                    ).onEvent(InventoryClickEvent::class.java) { event ->
                        val player = event.whoClicked as? Player ?: return@onEvent
                        player.connectToService(service)
                    }.build()
                }.toMap()
            } ?: mapOf()

    val baseInventory: Inventory get() = Bukkit.createInventory(null, 5 * 9, player.getLang<Main>().getCmp("ui.server_selector.title"))
        .apply {
            for (i in 0..44)
                setItem(i, ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, Component.empty()).build())

            items.forEach { (slot, item) -> setItem(slot, item) }
        }

    fun open() {
        player.playSound(player.location, Sound.BLOCK_SCAFFOLDING_PLACE, 3f, 1f)
        player.openInventory(baseInventory)
    }
}