package me.sersch.http.components.user

import java.util.*

data class User(
    override val id: UUID = UUID.randomUUID(),
    override val firstName: String,
    override val lastName: String,
    override val mail: String,
    override val hash: String
):UserInterface
