package kg.smartpost.georgiancafe.data.network.dishes.model

data class ModelDishes(
    val cat_dish: CatDish
) {
    data class CatDish(
        val category: List<Category>,
        val dishes: List<Dishes>
    ) {

        data class Category(
            val active: String,
            val created: String,
            val id: String,
            val name: String,
            val order: String,
            val type: String,
            val updated: String
        )

        data class Dishes(
            val active: Any?,
            val cost: String,
            val created: String,
            val description: String,
            val description_kor: String,
            val edinic: String,
            val id: String,
            val id_menu: String,
            val ingredients: String,
            val link_photo: String,
            val min: String,
            val name: String,
            val order: String,
            val recept: String,
            val updated: String,
            val weight: String
        )
    }
}