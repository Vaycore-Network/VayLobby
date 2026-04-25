package de.c4vxl.vaylobby.command

import de.c4vxl.vaycoreapi.language.Lang.Companion.getLang
import de.c4vxl.vaylobby.Main
import de.c4vxl.vaylobby.lobby.Lobby
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.playerExecutor

object SetSpawnCommand {
    val command = commandTree("setspawn") {
        withAliases("ss")
        withPermission("de.c4vxl.vaycore.lobby.cmd.setspawn")
        withUsage("/setspawn")

        playerExecutor { player, args ->
            // Set spawn
            Lobby.setSpawn(player.location)

            player.sendMessage(player.getLang<Main>().getCmp("command.setspawn.success"))
        }
    }
}