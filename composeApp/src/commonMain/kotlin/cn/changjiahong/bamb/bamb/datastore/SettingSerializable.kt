package cn.changjiahong.bamb.bamb.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okio.BufferedSink
import okio.BufferedSource
import okio.use
import kotlin.reflect.KType

/**
 *
 * @author ChangJiahong
 * @date 2024/12/27
 */
class SettingSerializable<T : Any>(private val kType: KType, private val value:T) : OkioSerializer<T> {

    override val defaultValue: T
        get() = value

    @Suppress("UNCHECKED_CAST")
    override suspend fun readFrom(source: BufferedSource): T {
        try {
            val t = Json.decodeFromString(serializer(kType), source.readUtf8())
            return t as T
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read $kType", serialization)
        }
    }

    override suspend fun writeTo(t: T, sink: BufferedSink) {
        sink.use {
            it.writeUtf8(Json.encodeToString(serializer(kType), t))
        }
    }
}