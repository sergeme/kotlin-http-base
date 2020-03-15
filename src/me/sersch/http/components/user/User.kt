package me.sersch.http.components.user

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

data class User(
    val id: EntityID<Int>,
    val firstName: String,
    val lastName: String,
    val mail: String,
    val hash: String
)

