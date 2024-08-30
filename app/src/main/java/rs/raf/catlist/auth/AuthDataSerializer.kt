package rs.raf.catlist.auth

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import rs.raf.catlist.auth.domain.LoginData
import rs.raf.catlist.networking.serialization.AppJson
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets

class AuthDataSerializer : Serializer<LoginData> {

    override val defaultValue: LoginData = LoginData.EMPTY

    override suspend fun readFrom(input: InputStream): LoginData {
        val text = String(input.readBytes(), charset = StandardCharsets.UTF_8)
        return try {
            AppJson.decodeFromString<LoginData>(text)
        } catch (error: SerializationException) {
            throw CorruptionException(message = "Unable to deserialize file.", cause = error)
        }
    }

    override suspend fun writeTo(t: LoginData, output: OutputStream) {
        val text = AppJson.encodeToString(t)
        withContext(Dispatchers.IO) {
            output.write(text.toByteArray(charset = StandardCharsets.UTF_8))
        }
    }
}