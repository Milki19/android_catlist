package rs.raf.catlist.breeds.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rs.raf.catlist.breeds.api.model.BreedApiModel
import rs.raf.catlist.breeds.api.model.ImageModel

interface BreedApi {

    @GET("breeds")
    suspend fun getAllBreeds(): List<BreedApiModel>

    @GET("breeds/{id}")
    suspend fun getBreed(
        @Path("id") breedId: String,
    ): BreedApiModel

    @GET("images/{id}")
    suspend fun getImage(
        @Path("id") imageId: String,
    ): ImageModel

    @GET("breeds/search")
    suspend fun getSearch(
        @Query("q") query: String,
    ): List<BreedApiModel>

}