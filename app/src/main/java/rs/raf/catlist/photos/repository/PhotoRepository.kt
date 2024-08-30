package rs.raf.catlist.photos.repository

import androidx.room.withTransaction
import rs.raf.catlist.db.AppDatabase
import rs.raf.catlist.photos.api.PhotosApi
import rs.raf.catlist.photos.mappers.asAlbumDbModel
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val photosApi: PhotosApi,
    private val database: AppDatabase,
) {



    suspend fun fetchBreedPhotos(breedId: String) {
        val allPhotos = photosApi.fetchPhotos(breedId = breedId)
            .map { it.asAlbumDbModel(breedId) }
            .toMutableList()

        database.withTransaction {
            database.albumDao().insertAll(list = allPhotos)
        }
    }

    suspend fun fetchPhoto(photoId: String, breedId: String) {
        val photo = photosApi.fetchPhotoById(photoId = photoId).asAlbumDbModel(breedId)
        database.albumDao().insert(photo)
    }

    suspend fun getAllPhotos() = database.albumDao().getAll()

    suspend fun getPhotosByBreedId(breedId: String) = database.albumDao().getPhotosByBreedId(breedId = breedId)

    fun observeBreedPhotos(breedId: String) = database.albumDao().observeBreedPhotos(breedId = breedId)
}