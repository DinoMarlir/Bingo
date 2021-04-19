package de.hglabor.core

import com.google.common.collect.ImmutableMap
import de.hglabor.localization.Localization
import de.hglabor.loot.LootSet
import de.hglabor.rendering.LaborMapRenderer
import de.hglabor.settings.Settings
import de.hglabor.utils.checkedItems
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.bukkit.actionBar
import net.axay.kspigot.extensions.bukkit.title
import net.axay.kspigot.extensions.console
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.runnables.task
import net.axay.kspigot.utils.mark
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.map.MapView
import java.util.*
import kotlin.collections.ArrayList

object GameManager {

    var currentGamePhase = GamePhase.WAITING
    var isStarted = false
    private val materialPool: ArrayList<Material> = arrayListOf()
    val materials: ArrayList<Material> = arrayListOf()

    fun addToMaterialPool(lootSet: LootSet) {
        for (material in lootSet.materials.keys) {
            materialPool.add(material)
        }
    }

    fun removeFromMaterialPool(lootSet: LootSet) {
        for (material in lootSet.materials.keys) {
            materialPool.remove(material)
        }
    }

    fun endGame(player: Player) {
        currentGamePhase = GamePhase.END
        isStarted = false
        var i = 0
        task(
            period = 20
        ) {
            i++
            if(i > 19) {
                Localization.broadcastMessage("bingo.serverIsRestarting")
                Bukkit.dispatchCommand(console, "restart")
            } else {
                Localization.broadcastMessage("bingo.playerWins", ImmutableMap.of("player", player.name))
            }
        }
    }

    fun startGame(startDelay: Int) {
        if(materialPool.isEmpty()) {
            for (lootSet in LootSet.values()) {
                if(lootSet.isEnabled) {
                    addToMaterialPool(lootSet)
                }
            }
        }
        currentGamePhase = GamePhase.STARTING
        var seconds = startDelay
        task(
            howOften = seconds.toLong(),
            period = 20,
            delay = 10
        ) {
            seconds-=1
            if(seconds == 0) {
                task(
                    howOften = Settings.itemCount,
                    delay = 0,
                    period = 1
                ) {
                    var randomMaterial = materialPool.random()
                    while (materials.contains(randomMaterial)) {
                        randomMaterial = materialPool.random()
                    }
                    materials.add(randomMaterial)
                }
                for (player in onlinePlayers) {
                    player.inventory.clear()
                    player.title("Bingo", "gl & hf")
                    if(Settings.usingMap) {
                        task(delay = 15) {
                            giveMap(player)
                        }
                    }
                    task(
                        howOften = 20,
                        period = 10
                    ) {
                        player.inventory.remove(Material.TURTLE_EGG)
                    }
                    val world = Bukkit.getWorld("world")!!
                    val x = Random().nextInt(30)-Random().nextInt(30)
                    val z = Random().nextInt(30)-Random().nextInt(30)
                    val y = world.getHighestBlockYAt(x,z)+2
                    world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
                    Bukkit.getWorld("world_nether")?.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false)
                    player.teleport(Location(world, x.toDouble(), y.toDouble(), z.toDouble()))
                    if(!Settings.hitCooldown) {
                        val attackSpeedAttribute: AttributeInstance? = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)
                        if (attackSpeedAttribute != null) {
                            attackSpeedAttribute.baseValue = 100.0
                        }
                    }
                    manageActionBar(player)
                }
                currentGamePhase = GamePhase.IN_GAME
                isStarted = true
                Localization.broadcastMessage("bingo.gameStarted")
            } else {
                Localization.broadcastMessage("bingo.gameStartingIn", ImmutableMap.of("seconds", "$seconds"))
            }
        }
    }

    private fun manageActionBar(player: Player) {
        task(
            period = 40
        ) {
            val list = listOf("${KColors.BLUE}/bingo ${KColors.DARKGRAY}| ${KColors.BLUE}/top", "${KColors.BLUE}${player.checkedItems().size} ${KColors.DARKGRAY}/ ${KColors.BLUE}${Settings.itemCount}", "${KColors.BLUE}PvP${KColors.DARKGRAY}: ${if (Settings.pvp) "§ayes" else "§cno"} ${KColors.DARKGRAY}| ${KColors.BLUE}Hardcore${KColors.DARKGRAY}: ${if (Settings.kickOnDeath) "§ayes" else "§cno"}")
           player.actionBar(list.random())
        }
    }

    private fun giveMap(player: Player) {
        val stack = itemStack(Material.FILLED_MAP) {
            meta<MapMeta> {
                name = "${KColors.CORNFLOWERBLUE}Bingo"
            }
        }
        stack.mark("locked")
        player.inventory.setItemInOffHand(stack)
    }



}