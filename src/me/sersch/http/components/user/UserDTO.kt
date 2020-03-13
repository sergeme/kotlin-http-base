package me.sersch.http.components.user

data class UserDTO(
    override val firstName: String,
    override val lastName: String,
    override val mail: String
):UserDTOInterface