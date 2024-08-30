package rs.raf.catlist.photos.grid

import rs.raf.catlist.photos.grid.model.PhotoUiModel

interface AlbumGridContract {
    data class AlbumGridUiState(
        val updating: Boolean = false,
        val photos: List<PhotoUiModel> = emptyList(),
    )
}