package rs.raf.catlist.leaderboard.list

import rs.raf.catlist.leaderboard.list.model.LeaderboardUiModel

interface LeaderboardListContract {
    data class LeaderboardListState(
        val nickname: String = "",
        val loading: Boolean = true,
        val results: List<LeaderboardUiModel> = emptyList(),
        val error: Boolean = false,
    )
}