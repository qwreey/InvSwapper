package moe.qwreey.invswapper.utility

import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.NotNull

interface PersistentController {
    val persistentDataContainer: PersistentDataContainer
    val plugin: JavaPlugin

    fun removePersistent(key: String) {
        val namespacedKey = NamespacedKey(plugin, key)
        persistentDataContainer.remove(namespacedKey)
    }
    fun <P : Any, C : Any>setPersistent(
        key: String,
        type: @NotNull PersistentDataType<P, C>,
        value: @NotNull C
    ) {
        val namespacedKey = NamespacedKey(plugin, key)
        persistentDataContainer.set<P, C>(namespacedKey, type, value)
    }
    fun hasPersistent(key: String): Boolean {
        val namespacedKey = NamespacedKey(plugin, key)
        return persistentDataContainer.has(namespacedKey)
    }
    fun <P : Any, C : Any>getPersistent(
        key: String,
        type: @NotNull PersistentDataType<P, C>
    ): C? {
        val namespacedKey = NamespacedKey(plugin, key)
        return persistentDataContainer.get<P, C>(namespacedKey, type)
    }
    fun <P : Any, C : Any>getPersistent(
        key: String,
        type: @NotNull PersistentDataType<P, C>,
        ifNull: () -> C
    ): C {
        return getPersistent(key, type) ?: ifNull()
    }

    fun removePersistent(player: Player, key: String) {
        val namespacedKey = NamespacedKey(plugin, key)
        player.persistentDataContainer.remove(namespacedKey)
    }
    fun <P : Any, C : Any>setPersistent(
        player: Player,
        key: String,
        type: @NotNull PersistentDataType<P, C>,
        value: @NotNull C
    ) {
        val namespacedKey = NamespacedKey(plugin, key)
        player.persistentDataContainer.set<P, C>(namespacedKey, type, value)
    }
    fun hasPersistent(player: Player, key: String): Boolean {
        val namespacedKey = NamespacedKey(plugin, key)
        return player.persistentDataContainer.has(namespacedKey)
    }
    fun <P : Any, C : Any>getPersistent(
        player: Player,
        key: String,
        type: @NotNull PersistentDataType<P, C>
    ): C? {
        val namespacedKey = NamespacedKey(plugin, key)
        return player.persistentDataContainer.get<P, C>(namespacedKey, type)
    }
    fun <P : Any, C : Any>getPersistent(
        player: Player,
        key: String,
        type: @NotNull PersistentDataType<P, C>,
        ifNull: () -> C
    ): C {
        return getPersistent(player, key, type) ?: ifNull()
    }
}