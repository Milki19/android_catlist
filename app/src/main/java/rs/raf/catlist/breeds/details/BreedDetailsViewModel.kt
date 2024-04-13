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
import rs.raf.catlist.breeds.api.model.BreedApiModel
import rs.raf.catlist.breeds.domain.BreedData
import rs.raf.catlist.breeds.list.BreedListState
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
        fetchBreedDetails()
    }

    private fun fetchBreedDetails() {
        viewModelScope.launch {
            _state.value = _state.value.copy(fetching = true)
            try {
                val breedDetails = repository.fetchBreedDetails(breedId = breedId).breed()
                setState { copy(breedId = breedDetails.id) }
                setState { copy(data = breedDetails) }
                fetchImage(breedDetails.referenceImageId)
            } catch (error: IOException) {
                _state.value = _state.value.copy(error = BreedListState.ListError.ListUpdateFailed(cause = error))
            } finally {
                _state.value = _state.value.copy(fetching = false)
            }
        }
    }

    private fun fetchImage(referenceImageId: String){
        viewModelScope.launch {
            _state.value = _state.value.copy(fetching = true)
            try {
                val imageModel = repository.fetchImage(imageId = referenceImageId)
                setState { copy(imageModel = imageModel) }
            } catch (error: IOException) {
                _state.value = _state.value.copy(error = BreedListState.ListError.ListUpdateFailed(cause = error))
            } finally {
                _state.value = _state.value.copy(fetching = false)
            }
        }
    }

    private fun BreedApiModel.breed() = BreedData(
        weight = this.weight,
        id = this.id,
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
        referenceImageId = this.referenceImageId,
    )

}