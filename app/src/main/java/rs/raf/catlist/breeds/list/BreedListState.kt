package rs.raf.catlist.breeds.list

import rs.raf.catlist.breeds.domain.BreedData

data class BreedListState(
    val fetching: Boolean = false,
    val breeds: List<BreedData> = emptyList(),
    val error: ListError? = null,
    val searchQuery: String = "",
    val searchedBreeds: List<BreedData> = emptyList()
) {
    sealed class ListError {
        data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
    }
}
