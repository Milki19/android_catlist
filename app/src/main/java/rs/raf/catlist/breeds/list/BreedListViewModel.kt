package rs.raf.catlist.breeds.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catlist.breeds.api.model.BreedApiModel
import rs.raf.catlist.breeds.domain.BreedData
import rs.raf.catlist.breeds.repository.BreedRepository
import java.io.IOException

class BreedListViewModel (
    private val repository: BreedRepository = BreedRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BreedListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedListState.() -> BreedListState) = _state.getAndUpdate(reducer)


    init {
        fetchBreeds()
    }
    
    private fun fetchBreeds() {
        viewModelScope.launch {
            _state.value = _state.value.copy(fetching = true)
            try {
                val breeds = withContext(Dispatchers.IO) {
                    repository.fetchBreeds().map {it.allBreeds()}
                }
                setState { copy(breeds = breeds) }
            } catch (error: IOException) {
                _state.value = _state.value.copy(error = BreedListState.ListError.ListUpdateFailed(cause = error))
            } finally {
                _state.value = _state.value.copy(fetching = false)
            }
        }
    }

    private fun BreedApiModel.allBreeds() = BreedData(
        id = this.id,
        weight = this.weight,

        name = this.name,
        alternativeNames = this.alternativeNames,
        description = this.description,
        temperament = this.temperament,
        origin = this.origin,
        lifeSpan = this.lifeSpan,
        adaptability = this.adaptability,
        affectionLevel = this.affectionLevel,
        childFriendly = this.childFriendly,
        dogFriendly = this.dogFriendly,
        energyLevel = this.energyLevel,
        grooming = this.grooming,
        healthIssues = this.healthIssues,
        intelligence = this.intelligence,
        sheddingLevel = this.sheddingLevel,
        socialNeeds = this.socialNeeds,
        strangerFriendly = this.strangerFriendly,
        vocalisation = this.vocalisation,

        rare = this.rare,
        wikipediaURL = this.wikipediaURL,
        referenceImageId = this.referenceImageId
    )

    fun searchBreedByName(nameQuery: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(fetching = true)
            try {
                val filteredBreed = withContext(Dispatchers.IO) {
                    repository.searchBreedByName(nameQuery).map { it.allBreeds() }
                }
                setState { copy(breeds = filteredBreed) }
            } catch (error: IOException) {
                _state.value =
                    _state.value.copy(error = BreedListState.ListError.ListUpdateFailed(cause = error))
            } finally {
                _state.value = _state.value.copy(fetching = false)
            }
        }
    }

    fun clearSearch(){
        viewModelScope.launch{
            fetchBreeds()
        }
    }
    
    
}