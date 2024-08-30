package rs.raf.catlist.leaderboard.mappers

import rs.raf.catlist.leaderboard.api.module.LeaderboardApiModel
import rs.raf.catlist.leaderboard.list.model.LeaderboardUiModel

fun LeaderboardApiModel.asLeaderboardUiModel(id: Int): LeaderboardUiModel {
    return LeaderboardUiModel(
        id = id,
        nickname = this.nickname,
        result = this.result
    )
}