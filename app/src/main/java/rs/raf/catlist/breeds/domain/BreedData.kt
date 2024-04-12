package rs.raf.catlist.breeds.domain

data class BreedData(
    val id: String,
    val name: String,
    val alternateName: String,
    val countryCodes: String,
    val lifeExpectancy: String,
    val averageHeight: String,
    val rare: String,
    val wikipedia: String,
    val description: String,
    val temperamet: String
)
