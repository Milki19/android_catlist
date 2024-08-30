package rs.raf.catlist.photos.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rs.raf.catlist.photos.api.model.PhotoApiModel

interface PhotosApi {
    @GET("images/search?limit=25&format=json")
    suspend fun fetchPhotos(@Query("breed_ids") breedId: String): List<PhotoApiModel>

    @GET("images/{photoId}")
    suspend fun fetchPhotoById(
        @Path("photoId") photoId: String,
    ): PhotoApiModel

}