package rs.raf.catlist.breeds.db

import androidx.room.Embedded
import androidx.room.Relation

data class BreedWithPhotos(

    @Embedded val user: Breed,

    @Relation(
        parentColumn = "id",
        entityColumn = "breedOwnerId",
    )
    val albums: List<String>

)
