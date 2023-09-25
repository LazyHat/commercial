package com.lazyhat.work.tracker.data.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.lang.IllegalArgumentException

@Serializable(with = IpAddress.Companion.Serializer::class)
@OptIn(ExperimentalUnsignedTypes::class)
class IpAddress constructor(ipParts: List<UByte>) {

    constructor(vararg ip: UByte) : this(ip.toList())

    companion object {
        val Zero = IpAddress(0u, 0u, 0u, 0u)

        fun Parse(ip: String): IpAddress = IpAddress(ip.split('.').apply {
            if (this.any { it.isEmpty() })
                throw IllegalArgumentException("ip part is empty")
        }.map { it.toInt() }.apply {
            if (this.any { it < 0 })
                throw IllegalArgumentException("ip part < 0")
            if (this.any { it > 255 })
                throw IllegalArgumentException("ip part > 255")
        }.map { it.toUByte() })

        object Serializer : KSerializer<IpAddress> {
            override val descriptor: SerialDescriptor
                get() = PrimitiveSerialDescriptor("IpAddress", PrimitiveKind.STRING)

            override fun deserialize(decoder: Decoder): IpAddress {
                return Parse(decoder.decodeString())
            }

            override fun serialize(encoder: Encoder, value: IpAddress) {
                encoder.encodeString(value.toString())
            }

        }
    }

    private val parts: List<UByte> =
        ipParts.let {
            if (it.size != 4)
                throw IllegalArgumentException("ip parts size != 4")
            it.toList()
        }

    fun getPart(part: Int): UByte = parts[part]

    fun copy(copy: (index: Int, it: UByte) -> UByte): IpAddress =
        IpAddress(parts.mapIndexed(copy))

    fun copy(index: Int, value: UByte): IpAddress =
        IpAddress(parts.mapIndexed { idx, it -> if (index == idx) value else it })

    override fun toString(): String = parts.joinToString(separator = ".") { it.toString() }
}