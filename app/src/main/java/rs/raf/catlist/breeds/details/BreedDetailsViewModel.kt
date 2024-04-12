package rs.raf.catlist.breeds.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catlist.breeds.repository.BreedRepository
import java.io.IOException

class BreedDetailsViewModel(
    private val breedId: String,
    private val repository: BreedRepository = BreedRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(BreedDetailsState(breedId = breedId))
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedDetailsState.() -> BreedDetailsState) =
        _state.getAndUpdate(reducer)


    init {
        observeBreedDetails()
        fetchBreedDetails()
    }

    /**
     * Observes breed details data from our local data
     * and updates the state.
     */
    private fun observeBreedDetails() {
        viewModelScope.launch {
            repository.observeBreedDetails(breedId = breedId)
                .filterNotNull()
                .collect {
                    setState { copy(data = it) }
                }
        }
    }

    /**
     * Observes events sent to this viewModel from UI.
     */
    /**
     * Triggers updating local breed details by calling "api"
     * to get latest data and update underlying local data we use.
     *
     * Note that we are not updating the state here. This is done
     * from observeBreedDetails(). Both functions are using
     * the single source of truth (BreedRepository) so we can
     * do this. If we break this principle, the app will stop working.
     */
    private fun fetchBreedDetails() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchBreedDetails(breedId = breedId)
                }
            } catch (error: IOException) {
                setState {
                    copy(error = BreedDetailsState.DetailsError.DataUpdateFailed(cause = error))
                }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }

}