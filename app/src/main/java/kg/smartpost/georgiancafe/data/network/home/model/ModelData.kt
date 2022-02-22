package kg.smartpost.georgiancafe.data.network.home.model

data class ModelData(
    val user: User
) {
    data class User(
        val bonusi: String,
        val cat_hochu: Int,
        val code: Int,
        val eda: String,
        val personal_discount: Int,
        val phone: String,
        val size: String,
        val size_hochu: String,
        val size_bonusi: String,
        val title: String,
        val title_kod: String,
        val text: String,
        val text_size: String,
    )
}