package ru.lazyhat.work.activitytracker.data.store

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import ru.lazyhat.work.activitytracker.data.models.AppSettings
import java.io.InputStream
import java.io.OutputStream

object AppSettingsSerializer : Serializer<AppSettings> {
    override val defaultValue: AppSettings
        get() = AppSettings.Default

    override suspend fun readFrom(input: InputStream): AppSettings {
        return try {
            Json.decodeFromString(AppSettings.serializer(), input.readBytes().decodeToString())
        } catch (e: SerializationException) {
            throw CorruptionException("cant read dataStore file", e)
        }
    }

    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(AppSettings.serializer(), t).encodeToByteArray())
        }
    }
}