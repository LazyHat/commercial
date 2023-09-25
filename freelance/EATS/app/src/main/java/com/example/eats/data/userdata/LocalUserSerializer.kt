package com.example.eats.data.userdata

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


object LocalUserSerializer : Serializer<LocalUser> {
    override val defaultValue = LocalUser()

    override suspend fun readFrom(input: InputStream): LocalUser {
        try {
            return Json.decodeFromString(
                LocalUser.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read UserData", serialization)
        }
    }

    override suspend fun writeTo(t: LocalUser, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(LocalUser.serializer(), t).encodeToByteArray()
            )
        }
    }
}