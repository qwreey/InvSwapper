package moe.qwreey.invswapper

import org.bukkit.configuration.file.FileConfiguration

class Slot(
    val name: String,
    val allowPortals: Boolean,
    val defaultGamemode: String,
    val worlds: List<String>,
    val defaultWorld: String,
    val savePosition: Boolean,
    val saveGamemode: Boolean,
    val saveInventory: Boolean,
    val saveEnderChest: Boolean,
    val savePotion: Boolean,
)

class TeleportCommand(
    val name: String,
    val world: String,
)

class ConfigReader {
    var slots: List<Slot>? = null
    var teleportCommands: List<TeleportCommand>? = null

    fun reload(config: FileConfiguration) {
        teleportCommands = buildList {
            for (key in config.getConfigurationSection("teleport-commands")!!.getKeys(false)) {
                val teleportCommand = config.getConfigurationSection("teleport-commands.${key}")!!
                add(TeleportCommand(
                    name = key,
                    world = teleportCommand.getString("world", "world")!!,
                ))
            }
        }
        slots = buildList {
            for (key in config.getConfigurationSection("slots")!!.getKeys(false)) {
                val slot = config.getConfigurationSection("slots.${key}")!!
                add(Slot(
                    name = key,
                    allowPortals = slot.getBoolean("allow-portals", true),
                    defaultGamemode = slot.getString("default-gamemode", "survival")!!,
                    worlds = slot.getStringList("worlds"),
                    defaultWorld = slot.getString("default-world")!!,
                    savePosition = slot.getBoolean("save-position", true),
                    saveGamemode = slot.getBoolean("save-gamemode", true),
                    saveInventory = slot.getBoolean("save-inventory", true),
                    saveEnderChest = slot.getBoolean("save-enderchest", true),
                    savePotion = slot.getBoolean("save-potion", true),
                ))
            }
        }
    }
    fun unload() {
        slots = null
        teleportCommands = null
    }
    fun getSlotByWorldName(worldName: String?): Slot? {
        if (worldName == null) return null
        return slots?.find { it.worlds.contains(worldName) }
    }
    fun getSlotBySlotName(slotName: String?): Slot? {
        if (slotName == null) return null
        return slots?.find { it.name == slotName }
    }
}
