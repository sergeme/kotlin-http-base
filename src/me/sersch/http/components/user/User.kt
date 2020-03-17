package me.sersch.http.components.user

import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.exposed.dao.IntEntity

import org.jetbrains.exposed.dao.id.EntityID

class User(
    val id: Int,
    override val firstName: String,
    override val lastName: String,
    override val mail: String,
    val hash: String,
    override val role: Int
): UserInterface

