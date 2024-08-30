package rs.raf.catlist.quiz.repository

import rs.raf.catlist.db.AppDatabase
import rs.raf.catlist.quiz.di.Quiz
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val database: AppDatabase
){
    suspend fun insertQuizResult(result: Quiz) {
        database.quizDao().insertQuizResult(result)
    }

    fun getAllQuizResults(nickname: String) = database.quizDao().getQuizResultsForUser(nickname)

    suspend fun getBestScore(userId: String) = database.quizDao().getBestScore(userId)

    suspend fun getBestPosition(userId: String) = database.quizDao().getBestRanking(userId)

}