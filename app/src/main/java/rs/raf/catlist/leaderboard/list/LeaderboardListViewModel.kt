package rs.raf.catlist.leaderboard.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catlist.auth.AuthStore
import rs.raf.catlist.leaderboard.mappers.asLeaderboardUiModel
import rs.raf.catlist.leaderboard.repository.LeaderboardRepository
import javax.inject.Inject

@HiltViewModel
class LeaderboardListViewModel  @Inject constructor(
    private val repository: LeaderboardRepository,
    private val authStore: AuthStore
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardListContract.LeaderboardListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: LeaderboardListContract.LeaderboardListState.() -> LeaderboardListContract.LeaderboardListState) = _state.update(reducer)


    init {
        fetchLeaderboard()
    }

    private fun fetchLeaderboard() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val userProfile = authStore.data.first()
                val data = withContext(Dispatchers.IO) {
                    repository.fetchLeaderboard().mapIndexed { index, it ->
                        it.asLeaderboardUiModel(index + 1)
                    }
                }
                Log.e("FETCH", "Fetch Leaderboard")
                setState { copy(results = data) }
                setState { copy(nickname = userProfile.nickname) }
            } catch (error: Exception) {
                Log.d("FETCH", "Fetch Leaderboard Error", error)
            } finally {
                setState { copy(loading = false) }
            }
        }
    }
}