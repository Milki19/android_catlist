package rs.raf.breedlist.breeds.repository


import rs.raf.catlist.breeds.api.BreedApi
import rs.raf.catlist.breeds.mappers.asBreedDbModel
import rs.raf.catlist.db.AppDatabase
import javax.inject.Inject

class BreedRepository @Inject constructor (
    private val breedApi: BreedApi,
    private val database: AppDatabase
) {

    suspend fun fetchAllBreeds() {
        val breeds = breedApi.getAllBreeds()
        database.breedDao().insertAll(list = breeds.map { it.asBreedDbModel() })
    }

    suspend fun getAllBreeds() = database.breedDao().getAll()

    suspend fun getBreedDetails(breedId: String) {
        database.breedDao().get(breedId = breedId)
    }

    fun observeBreeds() = database.breedDao().observeAll()
    fun observeBreedDetails(breedId: String) =
        database.breedDao().observeBreedDetails(breedId = breedId)
}