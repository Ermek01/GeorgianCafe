package kg.smartpost.georgiancafe.data.network.category.model

data class ModelCategory(
    val cat_dish: CatDish
) {
    data class CatDish(
        val category: List<Category>
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
    }
}