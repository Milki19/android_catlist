package rs.raf.catlist.photos.grid

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catlist.navigation.breedId
import rs.raf.catlist.photos.repository.PhotoRepository
import javax.inject.Inject
import rs.raf.catlist.photos.grid.AlbumGridContract.AlbumGridUiState
import rs.raf.catlist.photos.mappers.asPhotoUiModel


@HiltViewModel
class AlbumsGridViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val breedId: String = savedStateHandle.breedId

    private val _state = MutableStateFlow(AlbumGridUiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: AlbumGridUiState.() -> AlbumGridUiState) = _state.update(reducer)

    init {
        fetchAlbums()
        observeAlbums()
    }


    private fun fetchAlbums() {
        viewModelScope.launch {
            setState { copy(updating = true) }
            try {
                withContext(Dispatchers.IO) {
                    photoRepository.fetchBreedPhotos(breedId = breedId)
                    Log.e("FETCH", "Fetch Photos")
                }
            } catch (error: Exception) {
                Log.d("FETCH", "Fetch Photos Error", error)
            }
            setState { copy(updating = false) }
        }
    }

    private fun observeAlbums() {
        viewModelScope.launch {
            photoRepository.observeBreedPhotos(breedId = breedId)
                .distinctUntilChanged()
                .collect {
                    setState { copy(photos = it.map { it.asPhotoUiModel() }) }
                }
        }
    }



}