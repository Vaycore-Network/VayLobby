package de.c4vxl.vaylobby.handler

import de.c4vxl.ranksystem.player.RankPlayer.Companion.rankPlayer
import de.c4vxl.vaycoreapi.language.Lang.Companion.getLang
import de.c4vxl.vaylobby.Main
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Team

/**
 * Handles scoreboards
 */
class ScoreboardHandler : Listener {
    init {
        Bukkit.getPluginManager().registerEvents(this, Main.instance)
    }

    private fun createObjective(player: Player) =
        "lobby_${player.uniqueId}".let {
            // Unregister old objective
            player.scoreboard.getObjective(it)?.unregister()

            // Create new objective
            player.scoreboard.registerNewObjective(it, Criteria.DUMMY, player.getLang<Main>().getCmp("sidebar.title"))
        }.apply { displaySlot = DisplaySlot.SIDEBAR }

    /**
     * Displays a scoreboard to a player
     * @param player The player
     * @param lines The lines to display
     */
    private fun display(player: Player, vararg lines: Component) {
        // Create new scoreboard
        if (player.scoreboard == Bukkit.getScoreboardManager().mainScoreboard)
            player.scoreboard = Bukkit.getScoreboardManager().newScoreboard

        // Disable collision in lobby
        (player.scoreboard.getTeam(player.name) ?: player.scoreboard.registerNewTeam(player.name)).apply {
            setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
            addPlayer(player)
        }

        val objective = createObjective(player)

        lines.reversed().forEachIndexed { i, cmp ->
            objective.getScore(LegacyComponentSerializer.legacySection().serialize(cmp)).score = i
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val lang = event.player.getLang<Main>()
        display(
            event.player,
            Component.empty(),
            lang.getCmp("sidebar.line.1", event.player.name),
            Component.text(" "),
            lang.getCmp("sidebar.line.2"),
            lang.getCmp("sidebar.line.3", event.player.rankPlayer.highestRank?.prefix ?: "/")
        )
    }
}