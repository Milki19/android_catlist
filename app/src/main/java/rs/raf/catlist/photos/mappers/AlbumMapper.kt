package rs.raf.catlist.photos.mappers


import rs.raf.catlist.photos.api.model.PhotoApiModel
import rs.raf.catlist.photos.db.Album
import rs.raf.catlist.photos.grid.model.PhotoUiModel

fun PhotoApiModel.asAlbumDbModel(breedId: String): Album {
    return Album(
        photoId = this.photoId,
        breedId = breedId,
        url = this.url,
        width = this.width,
        height = this.height,
    )
}

fun Album.asPhotoUiModel(): PhotoUiModel {
    return PhotoUiModel(
        photoId = this.photoId,
        breedId = this.breedId,
        url = this.url,
        width = this.width,
        height = this.height,
    )
}