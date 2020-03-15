package me.sersch.http.components.user

data class UserDTO(
    val firstName: String,
    val lastName: String,
    val mail: String,
    val hash: String
)