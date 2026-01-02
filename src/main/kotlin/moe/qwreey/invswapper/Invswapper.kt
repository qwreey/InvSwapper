package moe.qwreey.invswapper

import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.plugin.java.JavaPlugin

class Invswapper : JavaPlugin() {

    override fun onEnable() {
        // Plugin startup logic

    }

    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        System.out.println(event.from.world.getName())
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
