package com.lazyhat.work.aquaphor.data.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.lazyhat.work.aquaphor.data.models.FilterData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
//Конвертирует обьект в Json строку
object FilterDataSerializer : Serializer<FilterData> {
    override val defaultValue: FilterData = FilterData.Empty

    override suspend fun readFrom(input: InputStream): FilterData {
        try {
            return Json.decodeFromString(FilterData.serializer(), input.readBytes().decodeToString())
        } catch (e: SerializationException) {
            throw CorruptionException("Unable to read dataStore", e)
        }
    }

    override suspend fun writeTo(t: FilterData, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(FilterData.serializer(), t).encodeToByteArray())
        }
    }
}