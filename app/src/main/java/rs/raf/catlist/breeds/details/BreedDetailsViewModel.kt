package rs.raf.catlist.breeds.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.breedlist.breeds.repository.BreedRepository
import rs.raf.catlist.breeds.mappers.asBreedUiModel
import rs.raf.catlist.navigation.breedId
import rs.raf.catlist.photos.repository.PhotoRepository
import rs.raf.catlist.photos.mappers.asPhotoUiModel

import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BreedRepository,
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val breedId: String = savedStateHandle.breedId

    private val _state = MutableStateFlow(BreedDetailsContract.BreedDetailsState())
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedDetailsContract.BreedDetailsState.() -> BreedDetailsContract.BreedDetailsState) =
        _state.getAndUpdate(reducer)




    init {
        fetchCat()
        observeCatDetails()

    }

    private fun fetchCat() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {

                withContext(Dispatchers.IO) {
                    repository.getBreedDetails(breedId = breedId)
                }


            } catch (error: Exception) {
                // TODO Handle error
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }


    private fun fetchImage(photoId: String, breedId: String){
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    photoRepository.fetchPhoto(photoId = photoId, breedId = breedId)
                }
                getImage()
            } catch (error: IOException) {
                setState { copy(error = true) }
            }
        }
    }


    private fun getImage(){
        viewModelScope.launch {
            try {
                val photo = withContext(Dispatchers.IO) {
                    photoRepository.getPhotosByBreedId(breedId)[0].asPhotoUiModel()
                }
                setState { copy(image = photo) }
            } catch (error: IOException) {
                setState { copy(error = true) }
            }
        }
    }


    private fun observeCatDetails() {
        viewModelScope.launch {
            repository.observeBreedDetails(breedId = breedId)
                .distinctUntilChanged()
                .collect {
                    setState { copy(breed = it.asBreedUiModel()) }
                    fetchImage(it.reference_image_id, it.id)
                }
        }
    }

}