package rs.raf.catlist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import rs.raf.breedlist.breeds.db.BreedDao
import rs.raf.catlist.breeds.db.Breed
import rs.raf.catlist.photos.db.Album
import rs.raf.catlist.photos.db.AlbumDao
import rs.raf.catlist.quiz.di.Quiz
import rs.raf.catlist.quiz.di.QuizDao

@Database(
    entities = [
        Breed::class,
        Album::class,
        Quiz::class,

    ],
    version = 6,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun breedDao(): BreedDao

    abstract fun albumDao() : AlbumDao
    abstract fun quizDao() : QuizDao

}