package moe.qwreey.invswapper.utility

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.util.Optional

open class NullableListSerializer<P : Any, C : Any>(
    val elementSer: PersistentDataType<P, C>
): PersistentDataType<PersistentDataContainer, MutableList<C?>> {
    override fun getPrimitiveType(): Class<PersistentDataContainer> {
        return PersistentDataContainer::class.java
    }
    override fun getComplexType(): Class<MutableList<C?>> {
        return MutableList::class.java as Class<MutableList<C?>>
    }
    override fun toPrimitive(
        complex: MutableList<C?>,
        context: PersistentDataAdapterContext
    ): PersistentDataContainer {
        val container = context.newPersistentDataContainer()
        container.set(
            NamespacedKey("nullable-list", "size"),
            PersistentDataType.INTEGER,
            complex.size
        )
        complex.forEachIndexed { index, c ->
            if (c != null) {
                container.set(
                    NamespacedKey("nullable-list", "_" + index.toString()),
                    elementSer,
                    c
                )
            }
        }
        return container
    }
    override fun fromPrimitive(
        primitive: PersistentDataContainer,
        context: PersistentDataAdapterContext
    ): MutableList<C?> {
        val size = primitive.get(
            NamespacedKey("nullable-list", "size"),
            PersistentDataType.INTEGER
        )!!
        return MutableList(size, { idx ->
            val key = NamespacedKey("nullable-list", "_" + idx.toString())
            if (primitive.has(key)) {
                primitive.get(
                    key,
                    elementSer
                )
            } else { null }
        })
    }
}

open class ListSerializer<P : Any, C : Any>(
    val elementSer: PersistentDataType<P, C>
): PersistentDataType<PersistentDataContainer, MutableList<C>> {
    override fun getPrimitiveType(): Class<PersistentDataContainer> {
        return PersistentDataContainer::class.java
    }
    override fun getComplexType(): Class<MutableList<C>> {
        return MutableList::class.java as Class<MutableList<C>>
    }
    override fun toPrimitive(
        complex: MutableList<C>,
        context: PersistentDataAdapterContext
    ): PersistentDataContainer {
        val container = context.newPersistentDataContainer()
        container.set(
            NamespacedKey("list", "size"),
            PersistentDataType.INTEGER,
            complex.size
        )
        complex.forEachIndexed { index, c ->
            container.set(
                NamespacedKey("list", "_" + index.toString()),
                elementSer,
                c
            )
        }
        return container
    }
    override fun fromPrimitive(
        primitive: PersistentDataContainer,
        context: PersistentDataAdapterContext
    ): MutableList<C> {
        val size = primitive.get(
            NamespacedKey("list", "size"),
            PersistentDataType.INTEGER
        )!!
        return MutableList(size, { idx ->
            val key = NamespacedKey("list", "_" + idx.toString())
            primitive.get(
                key,
                elementSer
            )!!
        })
    }
}

class OptionalSerializer<P : Any, C : Any>(
    val elementSer: PersistentDataType<P, C>
): PersistentDataType<PersistentDataContainer, Optional<C>> {
    override fun getPrimitiveType(): Class<PersistentDataContainer> {
        return PersistentDataContainer::class.java
    }
    override fun getComplexType(): Class<Optional<C>> {
        return Optional::class.java as Class<Optional<C>>
    }
    override fun toPrimitive(
        complex: Optional<C>,
        context: PersistentDataAdapterContext
    ): PersistentDataContainer {
        val container = context.newPersistentDataContainer()
        container.set(
            NamespacedKey("optional","is-null"),
            PersistentDataType.BOOLEAN,
            complex.isEmpty
        )
        if (complex.isPresent) {
            container.set(
                NamespacedKey("optional","value"),
                elementSer,
                complex.get()
            )
        }
        return container
    }
    override fun fromPrimitive(
        primitive: PersistentDataContainer,
        context: PersistentDataAdapterContext
    ): Optional<C> {
        val isNull = primitive.get(
            NamespacedKey("optional","is-null"),
            PersistentDataType.BOOLEAN
        )
        return if (isNull == null || isNull) {
            Optional.empty<C>()
        } else {
            Optional.of<C>(primitive.get(
                NamespacedKey("optional","value"),
                elementSer
            )!!)
        }
    }
}

object LocationSerializer: PersistentDataType<PersistentDataContainer, Location> {
    override fun getPrimitiveType(): Class<PersistentDataContainer> {
        return PersistentDataContainer::class.java
    }
    override fun getComplexType(): Class<Location> {
        return Location::class.java
    }
    override fun toPrimitive(complex: Location, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()
        container.set(
            NamespacedKey("location","x"),
            PersistentDataType.STRING,
            complex.x.toString()
        )
        container.set(
            NamespacedKey("location","y"),
            PersistentDataType.STRING,
            complex.y.toString()
        )
        container.set(
            NamespacedKey("location","z"),
            PersistentDataType.STRING,
            complex.z.toString()
        )
        container.set(
            NamespacedKey("location","yaw"),
            PersistentDataType.STRING,
            complex.yaw.toString()
        )
        container.set(
            NamespacedKey("location","pitch"),
            PersistentDataType.STRING,
            complex.pitch.toString()
        )
        container.set(
            NamespacedKey("location","world"),
            PersistentDataType.STRING,
            complex.world.name
        )
        return container
    }
    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): Location {
        return Location(
            Bukkit.getWorld(primitive.get(
                NamespacedKey("location","world"),
                PersistentDataType.STRING
            )!!),
            primitive.get(
                NamespacedKey("location","x"),
                PersistentDataType.STRING,
            )!!.toDoubleOrNull() ?: 0.0,
            primitive.get(
                NamespacedKey("location","y"),
                PersistentDataType.STRING,
            )!!.toDoubleOrNull() ?: 0.0,
            primitive.get(
                NamespacedKey("location","z"),
                PersistentDataType.STRING,
            )!!.toDoubleOrNull() ?: 0.0,
            primitive.get(
                NamespacedKey("location","yaw"),
                PersistentDataType.STRING,
            )!!.toFloatOrNull() ?: 0.0f,
            primitive.get(
                NamespacedKey("location","pitch"),
                PersistentDataType.STRING,
            )!!.toFloatOrNull() ?: 0.0f,
        )
    }
}

object ItemStackSerializer: PersistentDataType<ByteArray, ItemStack> {
    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }
    override fun getComplexType(): Class<ItemStack> {
        return ItemStack::class.java
    }
    override fun toPrimitive(complex: ItemStack, context: PersistentDataAdapterContext): ByteArray {
        return complex.serializeAsBytes()
    }
    override fun fromPrimitive(primitive: ByteArray, context: PersistentDataAdapterContext): ItemStack {
        return ItemStack.deserializeBytes(primitive)
    }
}

object ItemStackListSerializer: NullableListSerializer<ByteArray, ItemStack>(ItemStackSerializer) {}
