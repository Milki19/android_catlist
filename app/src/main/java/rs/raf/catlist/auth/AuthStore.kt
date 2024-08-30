package rs.raf.catlist.auth

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import rs.raf.catlist.auth.domain.LoginData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthStore @Inject constructor(
    private val authStore: DataStore<LoginData>
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    val data = authStore.data
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = runBlocking { authStore.data.first() },
        )

    val isEmpty: Flow<Boolean> = data
        .map { it == LoginData.EMPTY }
        .distinctUntilChanged()

    suspend fun updateProfileData(
        data: LoginData
    ) {
        authStore.updateData { data }
        Log.d("AUTH", "ProfileData = $data")
    }

    suspend fun updateFullName(
        fullName: String
    ): LoginData {
        Log.d("AUTH", "New Full Name = $fullName")
        return authStore.updateData {
            it.copy(fullName = fullName)
        }
    }

    suspend fun updateNickname(
        nickname: String
    ): LoginData {
        Log.d("AUTH", "New Nickname = $nickname")
        return authStore.updateData {
            it.copy(nickname = nickname)
        }
    }

    suspend fun updateEmail(
        email: String
    ): LoginData {
        Log.d("AUTH", "New Email = $email")
        return authStore.updateData {
            it.copy(email = email)
        }
    }
}