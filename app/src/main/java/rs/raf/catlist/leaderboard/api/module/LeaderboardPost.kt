package rs.raf.catlist.leaderboard.api.module

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardPost(
    val nickname: String,
    val result: Float,
    val category: Int,
)

@Serializable
data class LeaderboardResponse(
    val result: LeaderboardApiModel,
    val ranking: Int
)
