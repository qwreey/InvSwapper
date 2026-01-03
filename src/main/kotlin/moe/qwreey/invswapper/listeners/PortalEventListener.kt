package moe.qwreey.invswapper.listeners

import moe.qwreey.invswapper.Invswapper
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPortalEvent
import org.bukkit.event.player.PlayerPortalEvent

class PortalEventListener(
    plugin: Invswapper
): Invswapper.Companion.PluginAssessor(plugin), Listener {
    @EventHandler
    fun onEntityPortalEvent(event: EntityPortalEvent) {
        val slot = configReader.getSlotByWorldName(event.from.world.name) ?: return
        if (!slot.allowPortals) {
            event.isCancelled = true
        }
    }
    @EventHandler
    fun onPlayerPortalEvent(event: PlayerPortalEvent) {
        val slot = configReader.getSlotByWorldName(event.from.world.name) ?: return
        if (!slot.allowPortals) {
            event.isCancelled = true
        }
    }
}
