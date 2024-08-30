package rs.raf.catlist.breeds.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BreedApiModel(

    val id: String,
    val name: String,
    val description: String,
    val wikipedia_url: String = "",
    val temperament: String= "",
    val origin: String= "",
    val alt_names: String = "",
    val life_span: String= "",
    val weight: Weight,
    val rare: Int=0,

    val adaptability: Int=0,
    val intelligence: Int=0,
    val affection_level: Int=0,
    val child_friendly: Int=0,
    val social_needs: Int=0,

    val reference_image_id: String="",
)

@Serializable
data class Weight(
    val imperial: String= "",
    val metric: String= ""
)



