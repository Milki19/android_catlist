package rs.raf.catlist.breeds.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rs.raf.catlist.breeds.api.model.BreedApiModel

interface BreedApi {

    @GET("breeds")
    suspend fun getAllBreeds(): List<BreedApiModel>

    @GET("breeds/{breed_id}")
    suspend fun getBreed(
        @Path("breed_id") breedId: String,
    ): BreedApiModel

}