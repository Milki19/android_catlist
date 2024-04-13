package rs.raf.catlist.breeds.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BreedApiModel(

    val weight: Weight,
    val id: String,
    val name: String,
    @SerialName("alt_names") val alternativeNames: String = "",
    var description: String,
    val temperament: String,
    val origin: String,
    @SerialName("life_span") val lifeSpan: String = "",



    val adaptability: Int,
    @SerialName("affection_level") val affectionLevel: Int,
    @SerialName("child_friendly") val childFriendly: Int,
    @SerialName("dog_friendly") val dogFriendly: Int,
    @SerialName("energy_level") val energyLevel: Int,
    val grooming: Int,
    @SerialName("health_issues") val healthIssues: Int,
    val intelligence: Int,
    @SerialName("shedding_level") val sheddingLevel: Int,
    @SerialName("social_needs") val socialNeeds: Int,
    @SerialName("stranger_friendly") val strangerFriendly: Int,
    val vocalisation: Int,

    val rare: Int,
    @SerialName("wikipedia_url") val wikipediaURL: String = "",

    @SerialName("reference_image_id") val referenceImageId: String = "",

    )
@Serializable
data class Weight(
    val imperial: String,
    val metric: String
)



