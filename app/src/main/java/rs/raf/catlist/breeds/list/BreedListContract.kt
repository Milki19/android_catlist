package rs.raf.catlist.breeds.list

import rs.raf.catlist.breeds.list.model.BreedUiModel

interface BreedListContract {

    data class BreedListState(
        val nickname: String = "",
        val loading: Boolean = false,
        val query: String = "",
        val isSearchMode: Boolean = false,
        val breed: List<BreedUiModel> = emptyList(),
        val filteredBreeds: List<BreedUiModel> = emptyList()

    )

    sealed class BreedListUiEvent {
        data class SearchQueryChanged(val query: String) : BreedListUiEvent()
        data object CloseSearchMode : BreedListUiEvent()
        data object Dummy : BreedListUiEvent()
    }
}