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
)

class ConfigReader {
    var slots: List<Slot>? = null

    fun reload(config: FileConfiguration) {
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
                ))
            }
        }
    }
    fun unload() {
        slots = null
    }
    fun getSlotByWorldName(worldName: String): Slot? {
        return slots?.find { it.worlds.contains(worldName) }
    }
    fun getSlotBySlotName(slotName: String): Slot? {
        return slots?.find { it.name == slotName }
    }
}
