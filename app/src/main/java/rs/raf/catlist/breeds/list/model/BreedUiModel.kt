package rs.raf.catlist.breeds.list.model

data class BreedUiModel(
    val id: String,
    val name: String,
    val temperament: String,
    val origin: String,
    val description: String,
    val wikipedia_url: String,
    val alt_names: String,
    val life_span: String,
    val weight: String,
    val rare: Int,

    val adaptability: Int,
    val intelligence: Int,
    val affection_level: Int,
    val child_friendly: Int,
    val social_needs: Int,

    val reference_image_id: String,
)
