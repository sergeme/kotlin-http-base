package me.sersch.http.components.user

import java.util.*

interface UserInterface {
    val id: Int
    val firstName: String
    val lastName: String
    val mail: String
    val hash: String
}