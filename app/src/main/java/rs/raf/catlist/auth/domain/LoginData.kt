package rs.raf.catlist.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class LoginData(
    val nickname: String,
    val fullName: String,
    val email: String
) {
    companion object {
        val EMPTY = LoginData(nickname = "", fullName = "", email = "")
    }
}