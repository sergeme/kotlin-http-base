package me.sersch.http.components.user

//Class used to send replies
data class UserAPI(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val mail: String,
    val role: Int
)