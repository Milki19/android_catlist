package rs.raf.catlist.breeds.details

import rs.raf.catlist.breeds.list.model.BreedUiModel
import rs.raf.catlist.photos.grid.model.PhotoUiModel

interface BreedDetailsContract {
    data class BreedDetailsState(
        val fetching: Boolean = true,
        val breed: BreedUiModel? = null,
        val image: PhotoUiModel? = null,
        val error: Boolean = false
    )
}