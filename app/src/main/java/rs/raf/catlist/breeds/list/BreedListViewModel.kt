package rs.raf.catlist.breeds.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catlist.breeds.repository.BreedRepository
import java.io.IOException

class BreedListViewModel (
    private val repository: BreedRepository = BreedRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BreedListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedListState.() -> BreedListState) = _state.getAndUpdate(reducer)


    init {
        observeBreeds()
        fetchBreeds()
    }

    /**
     * This will observe all breeds and update state whenever
     * underlying data changes. We are using viewModelScope which
     * will cancel the subscription when view model dies.
     */
    private fun observeBreeds() {
        // We are launching a new coroutine
        viewModelScope.launch {
            // Which will observe all changes to our breeds
            repository.observeBreeds().collect {
                setState { copy(breeds = it) }
            }
        }
    }

    /**
     * Fetches breeds from simulated api endpoint and
     * replaces existing breeds with "downloaded" breeds.
     */
    private fun fetchBreeds() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchBreeds()
                }
            } catch (error: IOException) {
                setState { copy(error = BreedListState.ListError.ListUpdateFailed(cause = error)) }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }
}