package rs.raf.breedlist.breeds.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import rs.raf.catlist.breeds.db.Breed


@Dao
interface BreedDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(breed: Breed)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Breed>)

    @Query("SELECT * FROM Breed")
    suspend fun getAll(): List<Breed>

    @Query("SELECT * FROM Breed")
    fun observeAll(): Flow<List<Breed>>

    @Query("SELECT * FROM Breed WHERE id = :breedId")
    fun get(breedId: String): Breed

    @Query("SELECT * FROM Breed WHERE id = :breedId")
    fun observeBreedDetails(breedId: String): Flow<Breed>


}