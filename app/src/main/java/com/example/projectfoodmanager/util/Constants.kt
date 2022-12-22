package com.example.projectfoodmanager.util

object FireStoreCollection{
    val USER = "user"
    val RECIPE ="recipe"
    val RECIPE_PROD="recipes_test"
}
object FireStoreDocumentField {
    val RECIPE ="recipe"
    val RECIPE_PROD ="recipes_test"
}

object FireStorePaginations{
    val RECIPE_LIMIT: Long = 25
}

object SharedPrefConstants {
    val LOCAL_SHARED_PREF = "local_shared_pref"
    val USER_SESSION = "user_session"
    val FAVORITE_RECIPES_SESSION = "favorite_recipes_session"

    var METADATA = "metadata"
}

object MetadataConstants{
    val FIRST_TIME_LOGIN = "first_time_login"

}

enum class HomeTabs(val index: Int, val key: String) {
    NOTES(0, "notes"),
    TASKS(1, "tasks"),
}