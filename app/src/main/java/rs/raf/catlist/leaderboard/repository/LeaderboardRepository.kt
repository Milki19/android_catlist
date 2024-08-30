package rs.raf.catlist.leaderboard.repository

import rs.raf.catlist.leaderboard.api.LeaderboardApi
import rs.raf.catlist.leaderboard.api.module.LeaderboardPost
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val leaderboardApi: LeaderboardApi,
) {
    suspend fun fetchLeaderboard() = leaderboardApi.fetchLeaderBoard()
    suspend fun postLeaderboard(leaderboardPost : LeaderboardPost) =
        leaderboardApi.postLeaderBoard(leaderboardPost)
}