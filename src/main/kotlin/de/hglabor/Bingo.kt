package de.hglabor

import de.hglabor.commands.BingoCommand
import de.hglabor.commands.SettingsCommand
import de.hglabor.commands.StartCommand
import de.hglabor.core.GameManager
import de.hglabor.listener.inventory.InventoryClickListener
import de.hglabor.listener.player.*
import de.hglabor.localization.Localization
import de.hglabor.rendering.MapListener
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.bukkit.feed
import net.axay.kspigot.extensions.bukkit.feedSaturate
import net.axay.kspigot.extensions.bukkit.heal
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.extensions.pluginManager
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.main.KSpigot
import net.axay.kspigot.runnables.task
import net.axay.kspigot.utils.mark
import org.bukkit.*
import org.bukkit.permissions.Permission
import org.bukkit.plugin.Plugin

class Bingo : KSpigot() {

    companion object {
        lateinit var plugin: Plugin
        lateinit var bingo: Bingo
    }

    override fun load() {
        broadcast("${KColors.GREENYELLOW}ENABLING PLUGIN")
        for (player in onlinePlayers) {
            player.playSound(player.location, Sound.BLOCK_BEACON_ACTIVATE, 10.0f, 1.0f)
            val y = Bukkit.getWorld("lobby")?.getHighestBlockYAt(0,0)?.plus(2)?.toDouble()!!
            player.teleport(Location(Bukkit.getWorld("lobby")!!, 0.0, y, 0.0))
        }
    }

    override fun startup() {
        plugin = this
        bingo = this
        WorldCreator("lobby").createWorld()
        Localization.load()
        MapListener
        InventoryClickListener
        PlayerPickupListener
        DamageListener
        PlayerMapManipulateListener
        PlayerDeathListener
        PlayerLoginListener
        PlayerJoinListener
        StartCommand
        BingoCommand
        SettingsCommand
        pluginManager.addPermission(Permission("hglabor.bingo.startgame"))
        pluginManager.addPermission(Permission("hglabor.bingo.settings"))
        task(
            period = 1,
            delay = 5
        ) {
            if(GameManager.isStarted) {
                it.cancel()
            }
            for (player in onlinePlayers) {
                if(player.location.y < 1) {
                    val y = Bukkit.getWorld("lobby")?.getHighestBlockYAt(0,0)?.plus(2)?.toDouble()!!
                    player.teleport(Location(Bukkit.getWorld("lobby")!!, 0.0, y, 0.0))
                }
                player.heal()
                player.feedSaturate()
                if(player.isOp) {
                    val stack = itemStack(Material.TURTLE_EGG) {
                        meta {
                            name = "${KColors.CORNFLOWERBLUE}${Localization.getUnprefixedMessage("bingo.word.settings", player.locale)}"
                        }
                    }
                    stack.mark("locked")
                    stack.mark("settings")
                    player.inventory.setItem(4, stack)
                }
            }
        }
    }

    override fun shutdown() {
        broadcast("${KColors.TOMATO}DISABLING PLUGIN")
        for (player in onlinePlayers) player.playSound(player.location, Sound.BLOCK_BEACON_DEACTIVATE, 10.0f, 1.0f)
    }

}