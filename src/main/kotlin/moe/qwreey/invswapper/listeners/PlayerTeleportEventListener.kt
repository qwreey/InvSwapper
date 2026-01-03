package moe.qwreey.invswapper.listeners

import moe.qwreey.invswapper.Invswapper
import moe.qwreey.invswapper.utility.InvSaver
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent

class PlayerTeleportEventListener(
    plugin: Invswapper
): Invswapper.Companion.PluginAssessor(plugin), Listener {
    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        val slotFrom = configReader.getSlotByWorldName(event.from.world.name)
        val slotTo = configReader.getSlotByWorldName(event.to.world.name)
        val saver = InvSaver(plugin)

        // 같은 시공간을 여행
        if (slotFrom?.name == slotTo?.name) return

        if (slotFrom != null) {
            saver.saveInv(event.player, slotFrom.name)
            saver.savePos(event.player, slotFrom.name, event.from)
            saver.saveProps(event.player, slotFrom.name)
        }
        if (slotTo != null) {
            saver.loadInv(event.player, slotTo.name)
            val loadedPos = saver.loadPos(event.player, slotTo.name)
            if (loadedPos != null)
                event.to = loadedPos
            saver.loadProps(event.player, slotTo.name)
        }
    }
}
