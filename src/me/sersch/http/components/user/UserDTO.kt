package me.sersch.http.components.user

//Class used to receive requests
data class UserDTO(
    val id: Int?,
    val firstName: String,
    val lastName: String,
    val mail: String,
    val hash: String = "",
    val role: Int
)