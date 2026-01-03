package moe.qwreey.invswapper.utility

import de.tr7zw.nbtapi.NBT
import moe.qwreey.invswapper.Invswapper
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.event.Listener
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class InvSaver(
    plugin: Invswapper
): Invswapper.Companion.PluginAssessor(plugin), Listener {

// TODO: 아직 이팩트와 통계, 조합법, 도전과제 싱크가 수행되지 않습니다
//       추후 구현될 예정입니다
//        player.compassTarget
//        player.clearTitle()
//        <del>player.setStatistic</del>
//        player.discoveredRecipes
//        Bukkit.recipeIterator()
//        Bukkit.advancementIterator()
//        player.getAdvancementProgress()
//        + 어트리뷰트

    fun saveRecipes(player: Player, invKey: String) {
        val slot = configReader.getSlotBySlotName(invKey) ?: return
        if (!slot.saveRecipes) return

        val list = player.discoveredRecipes.toList().map { it.asString() }.toMutableList()
        setPersistent(
            player,
            "invsave-${invKey}-recipes",
            ListSerializer(PersistentDataType.STRING),
            list
        )
    }
    fun loadRecipes(player: Player, invKey: String) {
        val slot = configReader.getSlotBySlotName(invKey) ?: return
        if (!slot.saveRecipes) return

        val list = getPersistent(
            player,
            "invsave-${invKey}-recipes",
            ListSerializer(PersistentDataType.STRING),
            { emptyList<String>().toMutableList() },
        )
        val savedSet = list.toHashSet()
        val currentSet = player.discoveredRecipes.map { it.asString() }.toHashSet()

        val shouldDiscover = buildList<NamespacedKey> {
            for (savedRecipe in savedSet) {
                if (!currentSet.contains(savedRecipe)) {
                    add(NamespacedKey.fromString(savedRecipe)!!)
                }
            }
        }
        val shouldUndiscover = buildList<NamespacedKey> {
            for (currentRecipe in currentSet) {
                if (!savedSet.contains(currentRecipe)) {
                    add(NamespacedKey.fromString(currentRecipe)!!)
                }
            }
        }
        player.undiscoverRecipes(shouldUndiscover)
        player.discoverRecipes(shouldDiscover)
    }

    fun saveEffects(player: Player, invKey: String) {
        val slot = configReader.getSlotBySlotName(invKey) ?: return
        if (!slot.savePotion) return

        val list = player.activePotionEffects.toList().toMutableList()
        setPersistent(
            player,
            "invsave-${invKey}-potion",
            ListSerializer(PotionEffectSerializer),
            list
        )
    }
    fun loadEffects(player: Player, invKey: String) {
        val slot = configReader.getSlotBySlotName(invKey) ?: return
        if (!slot.savePotion) return

        player.clearActivePotionEffects()
        val list = getPersistent(
            player,
            "invsave-${invKey}-potion",
            ListSerializer(PotionEffectSerializer)
        ) ?: return
        player.addPotionEffects(list)
    }

    fun saveProps(player: Player, invKey: String) {
        val slot = configReader.getSlotBySlotName(invKey) ?: return

        setPersistent(
            player,
            "invsave-${invKey}-sneaking",
            PersistentDataType.BOOLEAN,
            player.isSneaking
        )
        setPersistent(
            player,
            "invsave-${invKey}-health",
            PersistentDataType.DOUBLE,
            player.health
        )
        setPersistent(
            player,
            "invsave-${invKey}-saturation",
            PersistentDataType.FLOAT,
            player.saturation
        )
        setPersistent(
            player,
            "invsave-${invKey}-food-level",
            PersistentDataType.INTEGER,
            player.foodLevel
        )
        setPersistent(
            player,
            "invsave-${invKey}-fire-ticks",
            PersistentDataType.INTEGER,
            player.fireTicks
        )
        setPersistent(
            player,
            "invsave-${invKey}-freeze-ticks",
            PersistentDataType.INTEGER,
            player.freezeTicks
        )
        setPersistent(
            player,
            "invsave-${invKey}-death-screen-score",
            PersistentDataType.INTEGER,
            player.deathScreenScore
        )
        setPersistent(
            player,
            "invsave-${invKey}-total-experience",
            PersistentDataType.INTEGER,
            player.totalExperience
        )
        NBT.get(player, {
            if (it.hasTag("respawn")) {
                val respawn = it.getCompound("respawn")!!
                val pos = respawn.getIntArray("pos")!!
                val dimension = respawn.getString("dimension")!!
                setPersistent(
                    player,
                    "invsave-${invKey}-respawn-pos",
                    PersistentDataType.INTEGER_ARRAY,
                    pos
                )
                setPersistent(
                    player,
                    "invsave-${invKey}-respawn-dimension",
                    PersistentDataType.STRING,
                    dimension
                )
                setPersistent(
                    player,
                    "invsave-${invKey}-respawn-yaw",
                    PersistentDataType.FLOAT,
                    respawn.getFloat("yaw") ?: 0.0f,
                )
                setPersistent(
                    player,
                    "invsave-${invKey}-respawn-pitch",
                    PersistentDataType.FLOAT,
                    respawn.getFloat("pitch") ?: 0.0f,
                )
            } else {
                removePersistent(player, "invsave-${invKey}-respawn-pos")
                removePersistent(player, "invsave-${invKey}-respawn-dimension")
                removePersistent(player, "invsave-${invKey}-respawn-yaw")
                removePersistent(player, "invsave-${invKey}-respawn-pitch")
            }
        })
        if (slot.saveGamemode) {
            setPersistent(
                player,
                "invsave-${invKey}-gamemode",
                PersistentDataType.STRING,
                player.gameMode.name
            )
        } else {
            removePersistent(player, "invsave-${invKey}-gamemode")
        }
        setPersistent(
            player,
            "invsave-${invKey}-flying",
            PersistentDataType.BOOLEAN,
            player.isFlying
        )
    }
    fun loadProps(player: Player, invKey: String) {
        val slot = configReader.getSlotBySlotName(invKey) ?: return
        player.isSneaking = getPersistent(
            player,
            "invsave-${invKey}-sneaking",
            PersistentDataType.BOOLEAN,
            { false }
        )
        player.health = getPersistent(
            player,
            "invsave-${invKey}-health",
            PersistentDataType.DOUBLE,
            { player.getAttribute(Attribute.MAX_HEALTH)?.value ?: 20.0 }
        )
        player.saturation = getPersistent(
            player,
            "invsave-${invKey}-saturation",
            PersistentDataType.FLOAT,
            { 0.0f }
        )
        player.foodLevel = getPersistent(
            player,
            "invsave-${invKey}-food-level",
            PersistentDataType.INTEGER,
            { 20 }
        )
        player.fireTicks = getPersistent(
            player,
            "invsave-${invKey}-fire-ticks",
            PersistentDataType.INTEGER,
            { 0 }
        )
        player.freezeTicks = getPersistent(
            player,
            "invsave-${invKey}-freeze-ticks",
            PersistentDataType.INTEGER,
            { 0 }
        )
        player.deathScreenScore = getPersistent(
            player,
            "invsave-${invKey}-death-screen-score",
            PersistentDataType.INTEGER,
            { 0 }
        )
        player.totalExperience = getPersistent(
            player,
            "invsave-${invKey}-total-experience",
            PersistentDataType.INTEGER,
            { 0 }
        )
        NBT.modify(player, {
            if (hasPersistent(player, "invsave-${invKey}-respawn-pos")) {
                val respawn = it.getOrCreateCompound("respawn")

                respawn.setIntArray("pos", getPersistent(
                    player, "invsave-${invKey}-respawn-pos",
                    PersistentDataType.INTEGER_ARRAY
                ))
                respawn.setString("dimension", getPersistent(
                    player, "invsave-${invKey}-respawn-dimension",
                    PersistentDataType.STRING
                ))
                respawn.setFloat("yaw", getPersistent(
                    player, "invsave-${invKey}-respawn-yaw",
                    PersistentDataType.FLOAT
                ))
                respawn.setFloat("pitch", getPersistent(
                    player, "invsave-${invKey}-respawn-pitch",
                    PersistentDataType.FLOAT
                ))
            } else {
                it.removeKey("respawn")
            }

        })
        player.gameMode = getPersistent(
            player,
            "invsave-${invKey}-gamemode",
            PersistentDataType.STRING,
        )?.run { GameMode.valueOf(this) } ?: GameMode.valueOf(slot.defaultGamemode.uppercase())
        player.isFlying = getPersistent(
            player,
            "invsave-${invKey}-flying",
            PersistentDataType.BOOLEAN,
            { false }
        )
    }

    fun savePos(player: Player, invKey: String, pos: Location) {
        val slot = configReader.getSlotBySlotName(invKey) ?: return
        if (!slot.savePosition) return
        setPersistent(
            player,
            "invsave-${invKey}-location",
            LocationSerializer,
            pos
        )
    }
    fun loadPos(player: Player, invKey: String): Location? {
        val slot = configReader.getSlotBySlotName(invKey) ?: return null
        if (!slot.savePosition) return null
        val key = "invsave-${invKey}-location"
        return if (hasPersistent(player, key)) {
            getPersistent(
                player,
                key,
                LocationSerializer
            )
        } else {
            val name = configReader.getSlotBySlotName(invKey)?.defaultWorld
            if (name != null) {
                Bukkit.getWorld(name)?.spawnLocation
            } else null
        }
    }

    fun saveInv(player: Player, invKey: String) {
        val slot = configReader.getSlotBySlotName(invKey) ?: return

        if (slot.saveInventory) {
            val contents = player.inventory.contents
            val list = MutableList(
                contents.size,
                { index -> contents[index] }
            )
            setPersistent(
                player,
                "invsave-${invKey}",
                ItemStackListSerializer,
                list
            )
        }
        if (slot.saveEnderChest) {
            val enderChestContents = player.enderChest.contents
            val enderChestList = MutableList(
                enderChestContents.size,
                { index -> enderChestContents[index] }
            )
            setPersistent(
                player,
                "invsave-${invKey}-ender-chest",
                ItemStackListSerializer,
                enderChestList
            )
        }
        logger.info("Saved ${player.name}'s inventory to slot ${invKey}")
    }
    fun loadInv(player: Player, invKey: String) {
        val slot = configReader.getSlotBySlotName(invKey) ?: return

        if (slot.saveInventory) {
            val loaded = getPersistent(
                player,
                "invsave-${invKey}",
                ItemStackListSerializer,
                { MutableList(
                    player.inventory.contents.size,
                    { null }
                ) }
            )
            player.inventory.contents = loaded.toTypedArray()
        }
        if (slot.saveEnderChest) {
            val loadedEnderChest = getPersistent(
                player,
                "invsave-${invKey}-ender-chest",
                ItemStackListSerializer,
                { MutableList(
                    player.enderChest.contents.size,
                    { null }
                ) }
            )
            player.enderChest.contents = loadedEnderChest.toTypedArray()
        }
        logger.info("Loaded ${player.name}'s inventory from slot ${invKey}")
    }
}
