package com.lazyhat.work.tracker.data.store

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.lazyhat.work.tracker.data.model.AppData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AppDataSerializer : Serializer<AppData> {
    override val defaultValue: AppData
        get() = AppData()

    override suspend fun readFrom(input: InputStream): AppData = try {
        Json.decodeFromString(AppData.serializer(), input.readBytes().decodeToString())
    } catch (e: SerializationException) {
        throw CorruptionException("Unable to read DataStore", e)
    }

    override suspend fun writeTo(t: AppData, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(AppData.serializer(), t).encodeToByteArray())
        }
    }
}