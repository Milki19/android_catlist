package rs.raf.catlist.leaderboard.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import rs.raf.catlist.leaderboard.api.module.LeaderboardApiModel
import rs.raf.catlist.leaderboard.api.module.LeaderboardPost
import rs.raf.catlist.leaderboard.api.module.LeaderboardResponse

interface LeaderboardApi {

    @GET("leaderboard?category=3")
    suspend fun fetchLeaderBoard(): List<LeaderboardApiModel>

    @POST("leaderboard")
    suspend fun postLeaderBoard(@Body leaderboardPost: LeaderboardPost) : LeaderboardResponse
}