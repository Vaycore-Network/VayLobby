package de.c4vxl.vaylobby

import de.c4vxl.vaycoreapi.language.Lang
import de.c4vxl.vaycoreapi.utils.ResourceUtils
import de.c4vxl.vaylobby.command.SetSpawnCommand
import de.c4vxl.vaylobby.handler.ConnectionHandler
import de.c4vxl.vaylobby.handler.LobbyHandler
import de.c4vxl.vaylobby.handler.ScoreboardHandler
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIPaperConfig
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: Main
        lateinit var logger: Logger
        lateinit var config: FileConfiguration
    }

    override fun onLoad() {
        instance = this
        Main.logger = this.logger

        // Load config
        saveResource("config.yml", false)
        Main.config = this.config

        // Load CommandAPI
        CommandAPI.onLoad(
            CommandAPIPaperConfig(this)
                .silentLogs(true)
                .verboseOutput(false)
        )
    }

    override fun onEnable() {
        // Enable CommandAPI
        CommandAPI.onEnable()

        // Register translations
        ResourceUtils.readResource("langs", Main::class.java).split("\n")
            .forEach { langName ->
                Lang.provideLanguageTranslations<Main>(
                    langName,
                    ResourceUtils.readResource("lang/$langName.yml", Main::class.java)
                )
            }

        // Register handlers
        ConnectionHandler()
        LobbyHandler()
        ScoreboardHandler()

        // Register commands
        SetSpawnCommand

        logger.info("[+] $name has been enabled!")
    }

    override fun onDisable() {
        // Disable CommandAPI
        CommandAPI.onDisable()

        logger.info("[+] $name has been disabled!")
    }
}