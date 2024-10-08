package rs.raf.catlist.quiz.di

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: Quiz)

    @Query("SELECT * FROM quiz_results WHERE nickname = :nickname ORDER BY date DESC")
    fun getQuizResultsForUser(nickname: String): Flow<List<Quiz>>

    @Query("SELECT MAX(score) FROM quiz_results WHERE nickname = :nickname")
    suspend fun getBestScore(nickname: String): Float?

    @Query("SELECT MIN(ranking) FROM quiz_results WHERE nickname = :nickname")
    suspend fun getBestRanking(nickname: String): Int?
}