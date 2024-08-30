package rs.raf.catlist.breeds.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.breedlist.breeds.repository.BreedRepository
import rs.raf.catlist.auth.AuthStore
import rs.raf.catlist.breeds.list.BreedListContract.BreedListUiEvent
import rs.raf.catlist.breeds.list.BreedListContract.BreedListState
import rs.raf.catlist.breeds.mappers.asBreedUiModel


import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class BreedListViewModel @Inject constructor(
    private val repository: BreedRepository,
    private val authStore: AuthStore
) : ViewModel() {

    private val _state = MutableStateFlow(BreedListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedListState.() -> BreedListState) = _state.update(reducer)

    private val events = MutableSharedFlow<BreedListUiEvent>()
    fun setEvent(event: BreedListUiEvent) = viewModelScope.launch { events.emit(event) }


    init {
        observeEvents()
        fetchAllBreeds()
        observeSearchQuery()
        observeBreeds()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            events
                .filterIsInstance<BreedListUiEvent.SearchQueryChanged>()
                .debounce(2.seconds)
                .collect {
                    // Called only when search query was changed
                    // and once 2 seconds have passed after the last char


                    setState {
                        copy(filteredBreeds = state.value.breed.filter { item ->
                            item.name.contains(state.value.query, ignoreCase = true)
                        })
                    }

                    setState { copy(loading = false) }
                }
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
//                    BreedListUiEvent.ClearSearch -> Unit
                    BreedListUiEvent.CloseSearchMode -> setState { copy(isSearchMode = false) }

                    is BreedListUiEvent.SearchQueryChanged -> {
                        println("search query changed")
                        println(it.query)
                        setState { copy(query = it.query) }
                        setState { copy(isSearchMode = true) }
                        setState { copy(loading = true) }
                        // onValueChange from OutlinedTextField is called for every character

                        // We should handle the query text state update here, but make the api call
                        // or any other expensive call somewhere else where we debounce the text changes
//                        it.query // this should be added to state
                    }

                    BreedListUiEvent.Dummy -> Unit

//                    is UserListUiEvent.RemoveUser -> {
////                        handleUserDeletion(userId = it.userId)
//                    }
                }
            }
        }
    }

    private fun observeBreeds() {
        viewModelScope.launch {
//            setState { copy(initialLoading = true) }
            repository.observeBreeds()
                .distinctUntilChanged()
                .collect {
                    val userProfile = authStore.data.first()
                    Log.i("VIDEO", "Users table updated.")
                    setState {
                        copy(
//                            initialLoading = false,
                            breed = it.map { it.asBreedUiModel() },
                        )
                    }
                    setState { copy(nickname = userProfile.nickname) }
                }
        }
    }

    private fun fetchAllBreeds() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchAllBreeds()
                }

            } catch (error: Exception) {
                // TODO Handle error
            } finally {
                setState { copy(loading = false) }
            }
        }
    }

}
