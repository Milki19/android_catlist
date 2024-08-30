package rs.raf.catlist.photos.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Query("SELECT * FROM Album")
    suspend fun getAll(): List<Album>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Album>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: Album)

    @Query("SELECT * FROM Album WHERE Album.breedId = :breedId")
    fun observeBreedPhotos(breedId: String): Flow<List<Album>>

    @Query("SELECT * FROM Album")
    fun observePhotos(): Flow<List<Album>>

    @Query("SELECT * FROM Album WHERE Album.breedId = :breedId")
    suspend fun getPhotosByBreedId(breedId: String): List<Album>

    @Query("SELECT * FROM Album WHERE Album.photoId = :photoId")
    fun observePhoto(photoId: String): Flow<Album>
}