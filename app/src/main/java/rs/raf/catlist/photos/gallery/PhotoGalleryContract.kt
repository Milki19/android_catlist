package rs.raf.catlist.photos.gallery

import rs.raf.catlist.photos.grid.model.PhotoUiModel

interface PhotoGalleryContract {
    data class PhotoGalleryState(
        val photos: List<PhotoUiModel> = emptyList(),
    )
}