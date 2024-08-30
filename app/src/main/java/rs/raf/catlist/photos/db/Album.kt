package rs.raf.catlist.photos.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Album(
    @PrimaryKey val photoId: String,
    val breedId: String,
    val url: String? = "",
    val width: Int,
    val height: Int,
)
