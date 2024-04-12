package rs.raf.catlist.breeds.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import rs.raf.catlist.breeds.domain.BreedData
import kotlin.time.Duration.Companion.seconds

object BreedRepository {

    private val breeds = MutableStateFlow(listOf<BreedData>())

    fun allBreeds(): List<BreedData> = breeds.value

    /**
     * Simulates api network request which downloads sample data
     * from network and updates breeds in this repository.
     */
    suspend fun fetchBreeds() {
        delay(2.seconds)
        breeds.update { SampleData.toMutableList() }
    }

    /**
     * Simulates api network request which updates single breed.
     * It does nothing. Just waits for 1 second.
     */
    suspend fun fetchBreedDetails(breedId: String) {
        delay(1.seconds)
    }

    /**
     * Returns StateFlow which holds all breeds.
     */
    fun observeBreeds(): Flow<List<BreedData>> = breeds.asStateFlow()

    /**
     * Returns regular flow with BreedData with given breedId.
     */
    fun observeBreedDetails(breedId: String): Flow<BreedData?> {
        return observeBreeds()
            .map { breeds -> breeds.find { it.id == breedId } }
            .distinctUntilChanged()
    }

    fun getBreedById(id: String): BreedData? {
        return breeds.value.find { it.id == id }
    }

    fun deleteBreed(id: String) {
        breeds.update { list ->
            val index = list.indexOfFirst { it.id == id }
            if (index != -1) {
                list.toMutableList().apply { removeAt(index) }
            } else {
                list
            }
        }
    }

    fun updateOrInsertBreed(id: String, data: BreedData) {
        breeds.update { list ->
            val index = list.indexOfFirst { it.id == id }
            if (index != -1) {
                list.toMutableList().apply {
                    this[index] = data
                }
            } else {
                list.toMutableList().apply {
                    add(data)
                }
            }
        }
    }

}