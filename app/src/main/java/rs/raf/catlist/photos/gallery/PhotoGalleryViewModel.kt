package rs.raf.catlist.photos.gallery

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.raf.catlist.navigation.breedId
import rs.raf.catlist.photos.mappers.asPhotoUiModel
import rs.raf.catlist.photos.repository.PhotoRepository
import javax.inject.Inject

@HiltViewModel
class PhotoGalleryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoRepository: PhotoRepository,
) : ViewModel() {

    private val breedId = savedStateHandle.breedId

    private val _state = MutableStateFlow(PhotoGalleryContract.PhotoGalleryState())
    val state = _state.asStateFlow()
    private fun setState(reducer: PhotoGalleryContract.PhotoGalleryState.() -> PhotoGalleryContract.PhotoGalleryState) = _state.update(reducer)

    init {
        observeCatPhotos()
    }

    private fun observeCatPhotos() {
        viewModelScope.launch {
            photoRepository.observeBreedPhotos(breedId = breedId)
                .distinctUntilChanged()
                .collect {
                    setState { copy(photos = it.map { it.asPhotoUiModel() }) }
                    Log.e("OBSERVE", "Observe cat photos ${state.value.photos}")
                }
        }
    }

}