package moe.qwreey.invswapper

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import moe.qwreey.invswapper.listeners.PlayerTeleportEventListener
import moe.qwreey.invswapper.listeners.PortalEventListener
import moe.qwreey.invswapper.utility.PersistentController
import moe.qwreey.invswapper.utility.registerEvents
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class Invswapper : JavaPlugin() {
    companion object {
        open class PluginAssessor(override val plugin: Invswapper): PersistentController {
            protected val logger: Logger get() { return plugin.logger }
            override val persistentDataContainer: PersistentDataContainer get() {
                return plugin.server.worlds.first().persistentDataContainer
            }
            protected val configFile: FileConfiguration get() { return plugin.config }
            protected val configReader: ConfigReader get() { return plugin.configReader }
        }
    }

    val configReader = ConfigReader()

    override fun onEnable() {
        saveDefaultConfig()
        configReader.reload(config)
        registerEvents(PlayerTeleportEventListener(this))
        registerEvents(PortalEventListener(this))

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, { commands ->
            commands.registrar().register(PluginCommandBuilder(this).build())
        })
    }

    override fun onDisable() {
        configReader.unload()
    }
}
