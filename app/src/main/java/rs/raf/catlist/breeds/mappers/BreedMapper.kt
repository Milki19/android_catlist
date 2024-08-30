package rs.raf.catlist.breeds.mappers

import rs.raf.catlist.breeds.api.model.BreedApiModel
import rs.raf.catlist.breeds.db.Breed
import rs.raf.catlist.breeds.list.model.BreedUiModel

fun BreedApiModel.asBreedDbModel(): Breed {
    return Breed(
        id = this.id,
        name = this.name,
        temperament = this.temperament,
        origin = this.origin,
        alt_names = this.alt_names,
        description = this.description,
        life_span = this.life_span,
        weight = this.weight.metric,
        wikipedia_url = this.wikipedia_url,
        reference_image_id = this.reference_image_id,
        rare = this.rare,
        adaptability = this.adaptability,
        intelligence = this.intelligence,
        affection_level = this.affection_level,
        child_friendly = this.child_friendly,
        social_needs = this.social_needs

    )
}

fun Breed.asBreedUiModel(): BreedUiModel {
    return BreedUiModel(
        id = this.id,
        name = this.name,
        temperament = this.temperament,
        origin = this.origin,
        description = this.description,
        life_span = this.life_span,
        weight = this.weight,
        rare = this.rare,
        adaptability = this.adaptability,
        affection_level = this.affection_level,
        intelligence = this.intelligence,
        child_friendly = this.child_friendly,
        social_needs = this.social_needs,
        reference_image_id = this.reference_image_id,
        alt_names = this.alt_names,
        wikipedia_url = this.wikipedia_url

    )
}