package kg.smartpost.georgiancafe.data.local

interface UserPreferences {
    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String): String?
}