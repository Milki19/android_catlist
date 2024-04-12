package rs.raf.catlist.breeds.details

import rs.raf.catlist.breeds.domain.BreedData

data class BreedDetailsState(
    val breedId: String,
    val fetching: Boolean = false,
    val data: BreedData? = null,
    val error: DetailsError? = null,
){
    sealed class DetailsError {
        data class DataUpdateFailed(val cause: Throwable? = null) : DetailsError()
    }
}
