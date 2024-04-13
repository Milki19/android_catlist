package rs.raf.catlist.breeds.details

import rs.raf.catlist.breeds.api.model.ImageModel
import rs.raf.catlist.breeds.domain.BreedData
import rs.raf.catlist.breeds.list.BreedListState

data class BreedDetailsState(
    val breedId: String,
    val imageModel: ImageModel? = null,
    val fetching: Boolean = false,
    val data: BreedData? = null,
    val error: BreedListState.ListError.ListUpdateFailed? = null,
){
    sealed class DetailsError {
        data class DataUpdateFailed(val cause: Throwable? = null) : DetailsError()
    }
}
