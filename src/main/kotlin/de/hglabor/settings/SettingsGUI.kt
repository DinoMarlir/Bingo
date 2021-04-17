package de.hglabor.settings

import de.hglabor.loot.LootSet
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.items.*
import org.bukkit.Material
import org.bukkit.entity.Player

class SettingsGUI {

    val gui = kSpigotGUI(GUIType.SIX_BY_NINE) {

        title = "Settings:"
        defaultPage = 0

        page(0) {
            placeholder(Slots.RowFiveSlotFive, itemStack(Material.COMPARATOR) {
                meta {
                    name = "§cGeneral Settings"
                    addLore {
                        +""
                        +"§9Allgemeine Einstellungen wie:"
                        +" §8- §7Map"
                        +" §8- §7Item Anzahl"
                        +" §8- §7Schaden"
                        +" §8- §7Kick wenn Tot"
                        +" §8- §7PVP"
                        +" §8- §7Hit Cooldown"
                        +" §8- §7Items"
                    }
                }
            })

            button(Slots.RowThreeSlotThree, itemStack(Material.MAP) {
                meta {
                    name = if (Settings.usingMap) "§7Map §8- §aAN" else "§7Map §8- §cAUS"
                    addLore {
                        +""
                        +"§7Stelle ein ob eine Karte benutzt"
                        +"§7wird um Items anzuzeigen oder nicht"
                    }
                }
            }) {
                Settings.usingMap = !Settings.usingMap
                it.bukkitEvent.currentItem = itemStack(Material.MAP) {
                    meta {
                        name = if (Settings.usingMap) "§7Map §8- §aAN" else "§cMap §8- §cAUS"
                        addLore {
                            +""
                            +"§7Stelle ein ob eine Karte benutzt"
                            +"§7wird um Items anzuzeigen oder nicht"
                        }
                    }
                }
                //updateGUI(it.player)
            }

            button(Slots.RowThreeSlotFour, itemStack(Material.NAME_TAG) {
                meta {
                    name = "§7Itemzahl: §b${Settings.itemCount}"
                    addLore {
                        +""
                        +"§7Stelle ein, wieviele Items"
                        +"§7benutzt werden."
                        +"§7Min: 1"
                        +"§7Max: 49"
                        +""
                        +"§eLinks Click §7höher"
                        +"§bRechts Click §7niedriger"
                    }
                }
            }) {
                if (it.bukkitEvent.isLeftClick) {
                    if (Settings.itemCount < 49) {
                        Settings.itemCount += 1
                    }
                }
                if (it.bukkitEvent.isRightClick) {
                    if (Settings.itemCount > 1) {
                        Settings.itemCount -= 1
                    }
                }
                it.bukkitEvent.currentItem = itemStack(Material.NAME_TAG) {
                    meta {
                        name = "§7Itemzahl: §b${Settings.itemCount}"
                        addLore {
                            +""
                            +" §7Stelle ein, wieviele Items"
                            +"§7benutzt werden."
                            +"§7Min: 1"
                            +"§7Max: 49"
                            +""
                            +"§eLinks Click §7höher"
                            +"§bRechts Click §7niedriger"
                        }
                    }
                }
                //updateGUI(it.player)
            }

            button(Slots.RowThreeSlotFive, itemStack(Material.SPLASH_POTION) {
                meta {
                    name = "§7Schaden ist: ${if (Settings.damage) "§aAN" else "§cAUS"}"
                    addLore {
                        +""
                        +"§7Stelle ein, ob Spieler Schaden"
                        +"§7nehmen können oder nicht."
                    }
                }
            }) {
                Settings.damage = !Settings.damage
                it.bukkitEvent.currentItem = itemStack(Material.SPLASH_POTION) {
                    meta {
                        name = "§7Schaden ist: ${if (Settings.damage) "§aAN" else "§cAUS"}"
                        addLore {
                            +""
                            +"§7Stelle ein, ob Spieler Schaden"
                            +"§7nehmen können oder nicht."
                        }
                    }
                }
                //updateGUI(it.player)
            }

            button(Slots.RowThreeSlotSix, itemStack(Material.TOTEM_OF_UNDYING) {
                meta {
                    name = "§7Kicken nach Tod: ${if (Settings.kickOnDeath) "§aAN" else "§cAUS"}"
                    addLore {
                        +""
                        +"§7Stelle ein, ob Spieler nach dem"
                        +"§7Tod gekickt werden oder nicht."
                    }
                }
            }) {
                Settings.kickOnDeath = !Settings.kickOnDeath
                it.bukkitEvent.currentItem = itemStack(Material.TOTEM_OF_UNDYING) {
                    meta {
                        name = "§7Kicken nach Tod: ${if (Settings.kickOnDeath) "§aAN" else "§cAUS"}"
                        addLore {
                            +""
                            +"§7Stelle ein, ob Spieler nach dem"
                            +"§7Tod gekickt werden oder nicht."
                        }
                    }
                }
                //updateGUI(it.player)
            }

            button(Slots.RowThreeSlotSeven, itemStack(Material.IRON_SWORD) {
                meta {
                    name = "§7PVP ist: ${if (Settings.pvp) "§aAN" else "§cAUS"}"
                    addLore {
                        +""
                        +"§7Stelle ein, ob PVP erlaubt"
                        +"§7ist oder nicht."
                    }
                }
            }) {
                Settings.pvp = !Settings.pvp
                it.bukkitEvent.currentItem = itemStack(Material.IRON_SWORD) {
                    meta {
                        name = "§7PVP ist: ${if (Settings.pvp) "§aAN" else "§cAUS"}"
                        addLore {
                            +""
                            +"§7Stelle ein, ob PVP erlaubt"
                            +"§7ist oder nicht."
                        }
                    }
                }
                //updateGUI(it.player)
            }

            button(Slots.RowTwoSlotFour, itemStack(Material.SHIELD) {
                meta {
                    name = "§7Hit-Cooldown: ${if (Settings.hitCooldown) "§aAN" else "§cAUS"}"
                    addLore {
                        +""
                        +"§7Stelle Hit-Cooldown an"
                        +"§7oder aus."
                    }
                }
            }) {
                Settings.hitCooldown = !Settings.hitCooldown
                it.bukkitEvent.currentItem = itemStack(Material.SHIELD) {
                    meta {
                        name = "§7Hit-Cooldown: ${if (Settings.hitCooldown) "§aAN" else "§cAUS"}"
                        addLore {
                            +""
                            +"§7Stelle Hit-Cooldown an"
                            +"§7oder aus."
                        }
                    }
                }
            }
            button(Slots.RowTwoSlotFive, itemStack(Material.GRASS_BLOCK) {
                meta {
                    name = "§7Oberwelt Items: ${if (LootSet.OVERWORLD.isEnabled) "§aAN" else "§cAUS"}"
                    addLore {
                        +""
                        +"§7Stelle Oberwelt Items an"
                        +"§7oder aus."
                    }
                }
            }) {
                LootSet.OVERWORLD.isEnabled = !LootSet.OVERWORLD.isEnabled
                it.bukkitEvent.currentItem = itemStack(Material.GRASS_BLOCK) {
                    meta {
                        name = "§7Oberwelt Items: ${if (LootSet.OVERWORLD.isEnabled) "§aAN" else "§cAUS"}"
                        addLore {
                            +""
                            +"§7Stelle Oberwelt Items an"
                            +"§7oder aus."
                        }
                    }
                }
            }

            button(Slots.RowTwoSlotSix, itemStack(Material.NETHERRACK) {
                meta {
                    name = "§7Nether Items: ${if (LootSet.NETHER.isEnabled) "§aAN" else "§cAUS"}"
                    addLore {
                        +""
                        +"§7Stelle Nether Items an"
                        +"§7oder aus."
                    }
                }
            }) {
                LootSet.NETHER.isEnabled = !LootSet.NETHER.isEnabled
                it.bukkitEvent.currentItem = itemStack(Material.NETHERRACK) {
                    meta {
                        name = "§7Nether Items: ${if (LootSet.NETHER.isEnabled) "§aAN" else "§cAUS"}"
                        addLore {
                            +""
                            +"§7Stelle Nether Items an"
                            +"§7oder aus."
                        }
                    }
                }
            }
        }
    }



    private fun updateGUI(player: Player) {
        player.openGUI(SettingsGUI().gui)
    }
}