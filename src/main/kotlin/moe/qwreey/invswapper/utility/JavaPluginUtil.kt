package moe.qwreey.invswapper.utility

import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

fun JavaPlugin.registerEvents(listener: Listener): JavaPlugin {
    server.pluginManager.registerEvents(listener, this)
    return this
}
