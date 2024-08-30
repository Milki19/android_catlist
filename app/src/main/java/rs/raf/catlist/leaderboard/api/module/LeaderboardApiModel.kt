package rs.raf.catlist.leaderboard.api.module

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardApiModel(
    val nickname: String = "",
    val result: Float = 0f
)
