package me.sersch.http.components.user

//Class that gets returned, sensitive data excluded
data class User(
    override val id: Int,
    override val firstName: String,
    override val lastName: String,
    override val mail: String,
    override val role: Int
) : UserInterface