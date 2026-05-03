package de.c4vxl.vaylobby.handler

import de.c4vxl.vaycoreapi.language.Lang.Companion.getLang
import de.c4vxl.vaylobby.Main
import de.c4vxl.vaylobby.lobby.Lobby
import de.c4vxl.vaylobby.ui.LanguageUI
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * Handles player connection
 */
class ConnectionHandler : Listener {
    init {
        Bukkit.getPluginManager().registerEvents(this, Main.instance)
    }

    private var sendWelcomeMessage = Main.config.getBoolean("config.send-welcome-message")
    private var sendJoinMessage = Main.config.getBoolean("config.send-join-message")
    private var sendQuitMessage = Main.config.getBoolean("config.send-quit-message")

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        // Send welcome message
        if (sendWelcomeMessage)
            event.player.sendMessage(event.player.getLang<Main>().getCmp("msg.welcome", event.player.name))

        // Announce to everyone
        event.joinMessage(null)
        if (sendJoinMessage)
            Bukkit.getOnlinePlayers().forEach {
                if (it != event.player)
                    it.sendMessage(it.getLang<Main>().getCmp("msg.join", event.player.name))
            }

        // Send player to spawn
        Lobby.sendPlayer(event.player)

        // Open language chooser if none is set
        if (!LanguageUI.already.contains(event.player.uniqueId.toString()))
            LanguageUI(event.player).open()
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        Lobby.sendPlayer(event.player)
        event.quitMessage(null)

        if (sendQuitMessage)
            Bukkit.getOnlinePlayers().forEach {
                if (it != event.player)
                    it.sendMessage(it.getLang<Main>().getCmp("msg.quit", event.player.name))
            }
    }
}