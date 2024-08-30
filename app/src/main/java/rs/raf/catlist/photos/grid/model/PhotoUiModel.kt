package rs.raf.catlist.photos.grid.model

data class PhotoUiModel(
    val photoId: String,
    val url: String? = "",
    val breedId: String,
    val width: Int,
    val height: Int
)
