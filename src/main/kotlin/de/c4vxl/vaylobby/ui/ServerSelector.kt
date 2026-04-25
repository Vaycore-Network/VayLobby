package de.c4vxl.vaylobby.ui

import de.c4vxl.vaycoreapi.language.Lang.Companion.getLang
import de.c4vxl.vaycoreapi.utils.ItemBuilder
import de.c4vxl.vaylobby.Main
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

/**
 * The Server selection ui
 */
class ServerSelector(
    val player: Player
) {
    val baseInventory: Inventory get() = Bukkit.createInventory(null, 5 * 9, player.getLang<Main>().getCmp("ui.server_selector.title"))
        .apply {
            for (i in 0..44)
                setItem(i, ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, Component.empty()).build())


        }

    fun open() {
        player.playSound(player.location, Sound.BLOCK_SCAFFOLDING_PLACE, 3f, 1f)
        player.openInventory(baseInventory)
    }
}