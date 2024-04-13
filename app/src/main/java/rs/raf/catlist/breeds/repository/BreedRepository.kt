package rs.raf.catlist.breeds.repository


import rs.raf.catlist.breeds.api.BreedApi
import rs.raf.catlist.breeds.api.model.BreedApiModel
import rs.raf.catlist.breeds.api.model.ImageModel
import rs.raf.catlist.networking.retrofit

object BreedRepository {

    private val breedApi : BreedApi = retrofit.create(BreedApi::class.java)
    suspend fun fetchBreeds() : List<BreedApiModel> = breedApi.getAllBreeds()
    suspend fun fetchBreedDetails(breedId: String): BreedApiModel = breedApi.getBreed(breedId = breedId)
    suspend fun fetchImage(imageId: String): ImageModel = breedApi.getImage(imageId = imageId)
    suspend fun searchBreedByName(nameQuery: String): List<BreedApiModel> {
        return breedApi.getSearch(nameQuery)
    }
}