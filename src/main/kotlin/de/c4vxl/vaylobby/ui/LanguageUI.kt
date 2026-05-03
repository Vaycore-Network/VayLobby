package de.c4vxl.vaylobby.ui

import com.destroystokyo.paper.profile.ProfileProperty
import de.c4vxl.vaycoreapi.language.Lang
import de.c4vxl.vaycoreapi.language.Lang.Companion.getLang
import de.c4vxl.vaycoreapi.language.Lang.Companion.setLang
import de.c4vxl.vaycoreapi.utils.ItemBuilder
import de.c4vxl.vaylobby.Main
import de.c4vxl.vaylobby.lobby.Lobby
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.meta.SkullMeta
import java.util.*


/**
 * The Language chooser
 */
class LanguageUI(
    val player: Player
) {
    companion object {
        val already = mutableSetOf<String>()

        init {
            YamlConfiguration.loadConfiguration(Lang.langsDB).getKeys(false).forEach {
                already.add(it)
            }
        }
    }

    val baseInventory: Inventory get() = Bukkit.createInventory(null, 3 * 9, player.getLang<Main>().getCmp("ui.language.title"))
        .apply {
            for (i in 0..26)
                setItem(i, ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, Component.empty()).build())

            setItem(10, languageHead("English", "en", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q5MTQ1Njg3N2Y1NGJmMWFjZTI1MWU0Y2VlNDBkYmE1OTdkMmNjNDAzNjJjYjhmNGVkNzExZTUwYjBiZTViMyJ9fX0="))
            setItem(11, languageHead("German", "de", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU3ODk5YjQ4MDY4NTg2OTdlMjgzZjA4NGQ5MTczZmU0ODc4ODY0NTM3NzQ2MjZiMjRiZDhjZmVjYzc3YjNmIn19fQ=="))
        }

    private fun languageHead(name: String, lang: String, texture: String) =
        ItemBuilder(Material.PLAYER_HEAD, Component.text(name).color(NamedTextColor.GOLD))
            .editMeta {
                (it as SkullMeta).apply {
                    this.playerProfile = Bukkit.createProfile(UUID.randomUUID()).apply {
                        setProperties(listOf(ProfileProperty("textures", texture)))
                    }
                    displayName(Component.text(name).color(NamedTextColor.GOLD))
                }
            }
            .onEvent(InventoryClickEvent::class.java) { event ->
                // Set language
                event.whoClicked.setLang(lang)
                event.whoClicked.sendMessage(event.whoClicked.getLang<Main>().getCmp("ui.language.success"))
                event.whoClicked.closeInventory()
                already.add(event.whoClicked.uniqueId.toString())
                Lobby.sendPlayer(event.whoClicked as? Player ?: return@onEvent)
            }
            .build()

    fun open() {
        player.playSound(player.location, Sound.BLOCK_SCAFFOLDING_PLACE, 3f, 1f)
        player.openInventory(baseInventory)
    }
}